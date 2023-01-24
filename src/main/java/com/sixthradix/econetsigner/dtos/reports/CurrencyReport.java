package com.sixthradix.econetsigner.dtos.reports;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CurrencyReport {
    private String name;
    private BigDecimal invoiceAmountTotal;
    private BigDecimal invoiceTaxAmountTotal;
}
