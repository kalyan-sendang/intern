package com.test.exceldata.repository;

import com.test.exceldata.entity.ExcelData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelDataRepo extends JpaRepository<ExcelData, Long> {
}
