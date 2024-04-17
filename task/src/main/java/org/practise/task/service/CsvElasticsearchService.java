package org.practise.task.service;

import org.practise.task.entity.CSVData;
import org.practise.task.entity.CSVDataDto;
import org.practise.task.repository.CSVRepository;
import org.practise.task.utils.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvElasticsearchService {
    private final CSVRepository csvRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public CsvElasticsearchService(CSVRepository csvRepository, ElasticsearchOperations elasticsearchOperations) {
        this.csvRepository = csvRepository;
        this.elasticsearchOperations = elasticsearchOperations;
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
        Criteria criteria = Criteria.where("bCT.keyword").is("CPT");
        CriteriaQuery query = new CriteriaQuery(criteria);
        query.addSourceFilter(new FetchSourceFilter(new String[]{"bC", "bCT", "negR"}, new String[]{}));
        query.setPageable(PageRequest.of(0, 10));
        query.addSort(Sort.by(Sort.Direction.DESC, "negR"));

        SearchHits<CSVData> searchHits = elasticsearchOperations.search(query, CSVData.class);
        List<CSVData> top10Results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .filter(csvEntity -> csvEntity.getBC() != null && csvEntity.getBCT() != null && csvEntity.getNegR() != null)
                .limit(10)
                .toList();
        return top10Results.stream()
                .map(DataDto -> new CSVDataDto(DataDto.getId(), DataDto.getBCT(), DataDto.getBC(),DataDto.getNegR())).toList();
    }


}