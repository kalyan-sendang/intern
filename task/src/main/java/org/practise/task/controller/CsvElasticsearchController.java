package org.practise.task.controller;

import org.practise.task.entity.CSVData;
import org.practise.task.entity.CSVDataDto;
import org.practise.task.service.CsvElasticsearchService;
import org.practise.task.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CsvElasticsearchController {
    private final CsvElasticsearchService csvElasticsearchService;

    public CsvElasticsearchController(CsvElasticsearchService csvElasticsearchService) {
        this.csvElasticsearchService = csvElasticsearchService;
    }

    @GetMapping("/insert-csv-to-elasticsearch")
    public String insertCsvToElasticsearch() {
        csvElasticsearchService.insertCsvDataToElasticsearch();
        return "CSV data inserted into Elasticsearch";
    }

    @GetMapping("/get-data")
    public ResponseEntity<ResponseWrapper<List<CSVDataDto>>> getCSVData() {
        ResponseWrapper<List<CSVDataDto>> response = new ResponseWrapper<>();
        try {
            List<CSVDataDto> csvDataList = csvElasticsearchService.executeElasticsearchQuery();
            if (csvDataList != null) {
                response.setSuccess(true);
                response.setMessage("Data retrieved successfully");
                response.setStatusCode(HttpStatus.OK.value());
                response.setResponse(csvDataList);
                return ResponseEntity.ok(response);
            } else {
                response.setStatusCode(HttpStatus.NO_CONTENT.value());
                response.setMessage("No data found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
