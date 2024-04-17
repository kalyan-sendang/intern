package org.practise.task.repository;

import org.practise.task.entity.CSVData;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CSVRepository extends ElasticsearchRepository<CSVData, Long> {
}
