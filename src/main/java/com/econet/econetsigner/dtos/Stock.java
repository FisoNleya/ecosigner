package com.econet.econetsigner.dtos;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock implements Serializable {

    private static final long serialVersionUID = 34345L;

    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("BranchName")
    private String branchName;
    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;
    @JsonProperty("CustomerName")
    private String customerName;
    @JsonProperty("CustomerVatNumber")
    private String customerVatNumber;
    @JsonProperty("CustomerAddress")
    private String customerAddress;
    @JsonProperty("CustomerTelephone")
    private String customerTelephone;
    @JsonProperty("CustomerBPN")
    private String customerBPN;
    @JsonProperty("InvoiceAmount")
    private String invoiceAmount;
    @JsonProperty("InvoiceTaxAmount")
    private String invoiceTaxAmount;
    @JsonProperty("Istatus")
    private String istatus;
    @JsonProperty("Cashier")
    private String cashier;
    @JsonProperty("InvoiceComment")
    private String invoiceComment;
    @JsonProperty("ItemsXml")
    private List<ItemsXml> itemsXml ;
    @JsonProperty("CurrenciesXml")
    private List<CurrenciesXml> currenciesXml ;

}
