package org.practise.task.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "test")
public class CSVData {
    @Id
    private String id;
    private String bCls;
    private String bC;
    private String bCT;
    private String negA;
    private String npi;
    private String tin;
    private String tinT;
    private String zip;
    private String negT;
    private Double negR;
    private String posH;
    private String mdH;
    private String nrP;
    private String _dT;
}
