package org.practise.task.service;

import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch.core.search.FieldCollapse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.practise.task.entity.CSVData;
import org.practise.task.entity.CSVDataDto;
import org.practise.task.repository.CSVRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvElasticsearchService {
    private final CSVRepository csvRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public CsvElasticsearchService(CSVRepository csvRepository, ElasticsearchOperations elasticsearchOperations, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.csvRepository = csvRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(CsvElasticsearchService.class);

    public void insertCsvDataToElasticsearch() {
        String filePath = "/home/zakipoint/Internship/task/src/main/resources/output/mergedData.csv";
        Integer batchSize = 10000;
        try (BufferedReader reader = new BufferedReader(new FileReader(ResourceUtils.getFile(filePath)))) {
            // Skip the first line
            String headerLine = reader.readLine();
            List<CSVData> batch = new ArrayList<>(batchSize);
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                CSVData entity = createEntityFromCsv(data);
                batch.add(entity);
                count++;
                if (count % batchSize == 0) {
                    saveBatch(batch);
                    batch.clear();
                }
            }
            // Save the remaining documents
            if (!batch.isEmpty()) {
                saveBatch(batch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBatch(List<CSVData> batch) {
        csvRepository.saveAll(batch);
    }

    private CSVData createEntityFromCsv(String[] data) {
        CSVData entity = new CSVData();
        entity.setBCls(data[0]);
        entity.setBC(data[1]);
        entity.setBCT(data[2]);
        entity.setNegA(data[3]);
        entity.setNpi(data[4]);
        entity.setTin(data[5]);
        entity.setTinT(data[6]);
        entity.setZip(data[7]);
        entity.setNegT(data[8]);
        entity.setNegR(Double.parseDouble(data[9]));
        entity.setPosH(data[10]);
        entity.setMdH(data[11]);
        entity.setNrP(data[12]);
        entity.set_dT(data[13]);
        return entity;
    }

    public List<CSVDataDto> executeElasticsearchQuery() {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field("bCT.keyword").value("CPT")))
                .withSourceFilter(new FetchSourceFilter(new String[]{"bC", "bCT", "negR"}, new String[]{}))
                .withPageable(PageRequest.of(0, 10))
                .withSort(Sort.by(Sort.Direction.DESC, "negR"))
                .withFieldCollapse(FieldCollapse.of(builder -> builder.field("negR")))
                .build();
        SearchHits<CSVData> searchHits = elasticsearchOperations.search(query, CSVData.class);
        List<CSVData> top10Results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .filter(csvEntity -> csvEntity.getBC() != null && csvEntity.getBCT() != null && csvEntity.getNegR() != null)
                .limit(10)
                .toList();
//        return top10Results.stream()
//                .map(DataDto -> new CSVDataDto(DataDto.getId(), DataDto.getBCT(), DataDto.getBC(),DataDto.getNegR())).toList();
        return top10Results.stream()
                .map(x -> modelMapper.map(x, CSVDataDto.class)).toList();
    }

    public List<CSVDataDto> getData() {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field("bCT.keyword").value("HCPCS")))
                .withSourceFilter(new FetchSourceFilter(new String[]{"bC", "bCT", "negR"}, new String[]{}))
                .withPageable(PageRequest.of(0, 10))
                .withSort(Sort.by(Sort.Direction.DESC, "negR"))
                .withFieldCollapse(FieldCollapse.of(builder -> builder.field("negR")))
                .build();
        SearchHits<CSVData> searchHits = elasticsearchOperations.search(query, CSVData.class);
        List<CSVData> top5Results = searchHits.getSearchHits().stream().map(
                SearchHit::getContent).filter(csvData -> csvData.getBC() != null && csvData.getBCT() != null
                && csvData.getNegR() != null).toList();
        return top5Results.stream().map(DataDto -> new CSVDataDto(DataDto.getId(), DataDto.getBCT(), DataDto.getBC(), DataDto.getNegR())).toList();
    }

    public Map<String, Double> getAggs() {
        NativeQuery query = NativeQuery.builder()
                .withMaxResults(0)
                .withQuery(q -> q.term(t -> t.field("bCT.keyword").value("CPT")))
                .withAggregation("total", Aggregation.of(a -> a.sum(s -> s.field("negR"))))
                .withAggregation("Average", Aggregation.of(a -> a.avg(av -> av.field("negR"))))
                .build();
        SearchHits<CSVData> searchHits = elasticsearchOperations.search(query, CSVData.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        assert aggregations != null;
        List<ElasticsearchAggregation> aggregationList = aggregations.aggregations();
        SumAggregate sumAggregate = aggregationList.get(0).aggregation().getAggregate().sum();
        AvgAggregate avgAggregate = aggregationList.get(1).aggregation().getAggregate().avg();
        Map<String, Double> result = new HashMap<>();
        result.put("sum", sumAggregate.value());
        result.put("avg", avgAggregate.value());
        return result;
    }

    public Map<String, Double>  getAllAggs() throws JsonProcessingException {
        NativeQuery query = NativeQuery.builder()
                .withMaxResults(0)
                .withQuery(q -> q.term(t -> t.field("bCT.keyword").value("CPT")))
                .withAggregation("All", Aggregation.of(a -> a.extendedStats(s -> s.field("negR"))))
                .build();
        SearchHits<CSVData> searchHits = elasticsearchOperations.search(query, CSVData.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) searchHits.getAggregations();
        assert aggregations != null;
        List<ElasticsearchAggregation> aggregationList = aggregations.aggregations();
        ExtendedStatsAggregate extendedStats = aggregationList.get(0).aggregation().getAggregate().extendedStats();

        Map<String, Double> result = new HashMap<>();
        result.put("Sum", extendedStats.sum());
        result.put("Avg", extendedStats.avg());
        result.put("Max", extendedStats.max());
        result.put("Min", extendedStats.min());
        result.put("SD", extendedStats.stdDeviation());
        result.put("Variance", extendedStats.variance());
        return result;
    }


}