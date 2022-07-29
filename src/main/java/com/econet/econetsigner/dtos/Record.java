
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
public class Record implements Serializable {

    private static final long serialVersionUID = 343533L;

    @JsonProperty("BRANCH")
    private String branch;
    @JsonProperty("CurrenciesReceived")
    private List<CurrenciesReceived> currenciesReceived;
    @JsonProperty("IADDRESS")
    private String iaddress;
    @JsonProperty("IAMT")
    private String iamt;
    @JsonProperty("IBPN")
    private String ibpn;
    @JsonProperty("ICODE")
    private List<Icode> icode;
    @JsonProperty("ICONTACT")
    private String icontact;
    @JsonProperty("ICURRENCY")
    private String icurrency;
    @JsonProperty("IDATE")
    private String idate;
    @JsonProperty("IISSUER")
    private String iissuer;
    @JsonProperty("INAME")
    private String iname;
    @JsonProperty("INUM")
    private String inum;
    @JsonProperty("IOCODE")
    private String iocode;
    @JsonProperty("IONUM")
    private String ionum;
    @JsonProperty("IPADDRESS")
    private String ipaddress;
    @JsonProperty("IPAYER")
    private String ipayer;
    @JsonProperty("IPBPN")
    private String ipbpn;
    @JsonProperty("IPTEL")
    private String iptel;
    @JsonProperty("IPVAT")
    private String ipvat;
    @JsonProperty("IREMARK")
    private String iremark;
    @JsonProperty("ISHORTNAME")
    private String ishortname;
    @JsonProperty("ISTATUS")
    private String istatus;
    @JsonProperty("ITAX")
    private String itax;
    @JsonProperty("ITAXCTRL")
    private String itaxctrl;
    @JsonProperty("ITEM")
    private List<Item> item;
    @JsonProperty("ITYPE")
    private String itype;
    @JsonProperty("VAT")
    private String vat;

}
