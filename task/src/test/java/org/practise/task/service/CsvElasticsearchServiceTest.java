package org.practise.task.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practise.task.entity.CSVData;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CsvElasticsearchServiceTest {
    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    private CSVData csvData1, csvData2;

    @BeforeEach
    public void setUp(){
        csvData1 = new CSVData();
        csvData1.setId("1a");
        csvData1.setBCT("CPT");
        csvData1.setBC("00160");
        csvData1.setNegR(100.45);

        csvData2 = new CSVData();
        csvData1.setId("2a");
        csvData1.setBCT("HCPS");
        csvData1.setBC("00180");
        csvData1.setNegR(200.45);
    }


    @Test
    public void testElasticsearchQuery(){
        List<CSVData> mockSearchResult = new ArrayList<>();
        mockSearchResult.add(csvData1);
        mockSearchResult.add(csvData2);

        // Mocking ElasticsearchOperations behavior
        when(elasticsearchOperations.search(any(), eq(CSVData.class))).thenReturn(getMockSearchHits(mockSearchResults));

}
