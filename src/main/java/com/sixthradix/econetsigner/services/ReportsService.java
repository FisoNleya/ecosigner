package com.sixthradix.econetsigner.services;

import com.sixthradix.econetsigner.dtos.auth.AuthenticatedUserDto;
import com.sixthradix.econetsigner.dtos.reports.CurrencyReport;
import com.sixthradix.econetsigner.dtos.reports.ReportDto;
import com.sixthradix.econetsigner.dtos.reports.UserReportDto;
import com.sixthradix.econetsigner.entities.Report;
import com.sixthradix.econetsigner.repositories.ReportRepository;
import com.sixthradix.econetsigner.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsService {

    public static final String FAIL_STATUS = "Failed";
    public static final String SUCCESS_STATUS = "Success";

    private final ReportRepository reportRepository;
    private final DateUtil dateUtil;

    public void logReportRecord(ReportDto report) {

        reportRepository.save(Report.builder()
                .userName(report.getUser().getUserName())
                .fullName(report.getUser().getFullName())
                .createdAt(LocalDateTime.now())
                .invoiceNumber(report.getInvoiceNumber())
                .signature(report.getSignature())
                .currency(report.getCurrency())
                .invoiceAmount(report.getInvoiceAmount())
                .invoiceTaxAmount(report.getInvoiceTaxAmount())
                .build());
    }


    public Page<Report> fetchReports(String userName, String invoiceNumber, Boolean failedStatus, Pageable pageable, String startDate, String endDate) {

        Report report = Report.builder()
                .userName(userName)
                .status(checkStatus(failedStatus))
                .invoiceNumber(invoiceNumber)
                .build();

        Page<Report> reports = reportRepository.findAll(Example.of(report), pageable);
        List<Report> responseList = reports.stream().filter(rep -> filterByDateRange(startDate, endDate, rep)).collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, reports.getTotalElements());
    }

    public String checkStatus(Boolean failedStatus){
        if(failedStatus == null)
            return null;
        if(Boolean.TRUE.equals(failedStatus) == true )
            return FAIL_STATUS;
        else
            return  SUCCESS_STATUS;

    }

    public Boolean filterByDateRange(String startTime, String endTime, Report report) {

        if (startTime == null || endTime == null)
            return true;

        LocalDateTime start = dateUtil.stringToDate(startTime);
        LocalDateTime end = dateUtil.stringToDate(endTime);
        LocalDateTime date = report.getCreatedAt();
        dateUtil.validateDate(start,end);

        return (date.isAfter(start) || date.isEqual(start)) && (report.getCreatedAt().isBefore(end) || date.isEqual(start));
    }

    public UserReportDto fetchUserReport(String userName) {
        UserReportDto userReportDto = new UserReportDto();

        List<CurrencyReport> currencyReports = new ArrayList<>();
        var currencies = reportRepository.getCurrencies();
        for(String currency: currencies){
            var invoiceAmountTotal = reportRepository.getInvoiceTotalAmountByCurrency(userName, currency).orElse(BigDecimal.ZERO);
            var invoiceTaxAmountTotal = reportRepository.getInvoiceTaxTotalAmountByCurrency(userName, currency).orElse(BigDecimal.ZERO);
            var currencyReport = CurrencyReport.builder()
                    .name(currency)
                    .invoiceAmountTotal(invoiceAmountTotal)
                    .invoiceTaxAmountTotal(invoiceTaxAmountTotal)
                    .build();
            currencyReports.add(currencyReport);
        }
        var totalInvoices = reportRepository.getTotalInvoiceCountByUser(userName);

        userReportDto.setUserName(userName);
        userReportDto.setCurrencyReports(currencyReports);
        userReportDto.setNumberOfInvoices(totalInvoices);
        return userReportDto;
    }

    private BigDecimal sumAmounts(List<BigDecimal> amounts){
        return amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
