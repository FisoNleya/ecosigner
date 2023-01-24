package com.sixthradix.econetsigner.dtos.reports;

import com.sixthradix.econetsigner.dtos.auth.AuthenticatedUserDto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ReportDto {

    private String invoiceNumber;
    private String signature;
    private String currency;
    private BigDecimal invoiceAmount;
    private BigDecimal invoiceTaxAmount;
    private AuthenticatedUserDto user;
    private String status;
}
