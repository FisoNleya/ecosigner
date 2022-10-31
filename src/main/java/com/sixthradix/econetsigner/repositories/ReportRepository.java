package com.sixthradix.econetsigner.repositories;

import com.sixthradix.econetsigner.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}