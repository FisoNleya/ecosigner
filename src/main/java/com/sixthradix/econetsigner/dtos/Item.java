
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
public class Item implements Serializable {

    private static final long serialVersionUID = 23453L;

    @JsonProperty("AMT")
    private String amt;
    @JsonProperty("HH")
    private String hh;
    @JsonProperty("ITEMCODE")
    private String itemcode;
    @JsonProperty("ITEMNAME1")
    private String itemname1;
    @JsonProperty("ITEMNAME2")
    private String itemname2;
    @JsonProperty("PRICE")
    private String price;
    @JsonProperty("QTY")
    private String qty;
    @JsonProperty("TAX")
    private String tax;
    @JsonProperty("TAXR")
    private String taxr;

}
