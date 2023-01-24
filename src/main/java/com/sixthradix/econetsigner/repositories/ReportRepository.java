package com.sixthradix.econetsigner.repositories;

import com.sixthradix.econetsigner.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r.currency, COUNT(r.currency) FROM Report AS r where r.userName = :userName GROUP BY r.currency ORDER BY r.currency DESC")
    List<Object[]> countTotalReportsByCurrency(@Param("userName") String userName);

    @Query("SELECT SUM(r.invoiceAmount) FROM Report r WHERE r.userName = :userName and r.currency = :currency")
    Optional<BigDecimal> getInvoiceTotalAmountByCurrency(@Param("userName") String userName, @Param("currency") String currency);

    @Query("SELECT SUM(r.invoiceTaxAmount) FROM Report r WHERE r.userName = :userName and r.currency = :currency")
    Optional<BigDecimal> getInvoiceTaxTotalAmountByCurrency(@Param("userName") String userName, @Param("currency") String currency);

    @Query("SELECT distinct r.currency from Report r where r.currency is not null")
    List<String> getCurrencies();

    @Query("SELECT COUNT (r) FROM Report r WHERE r.userName = :userName")
    Integer getTotalInvoiceCountByUser(@Param("userName") String userName);
}