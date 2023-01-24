package com.sixthradix.econetsigner.dtos.reports;

import lombok.Data;

import java.util.List;

@Data
public class UserReportDto {

    private String userName;
    private List<CurrencyReport> currencyReports;
    private Integer numberOfInvoices;
}
