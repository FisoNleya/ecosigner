package com.sixthradix.econetsigner.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemsXml implements Serializable {

    private static final long serialVersionUID = 431252341L;

    @JsonProperty("HH")
    private String hh;
    @JsonProperty("ITEMCODE")
    private String itemCode;
    @JsonProperty("ITEMNAME1")
    private String itemName1;
    @JsonProperty("ITEMNAME2")
    private String itemName2;
    @JsonProperty("QTY")
    private String qty;
    @JsonProperty("PRICE")
    private String price;
    @JsonProperty("AMT")
    private String amt;
    @JsonProperty("TAX")
    private String tax;
    @JsonProperty("TAXR")
    private String taxr;

}
