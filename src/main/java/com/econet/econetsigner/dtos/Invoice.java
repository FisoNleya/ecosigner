
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
public class Invoice implements Serializable {

    private static final long serialVersionUID = 32542345L;

    @JsonProperty("@xmlns")
    private String xmlns;
    @JsonProperty("BPN")
    private String bpn;
    @JsonProperty("CODE")
    private String code;
    @JsonProperty("CPY")
    private String cpy;
    @JsonProperty("DECENDDATE")
    private String decenddate;
    @JsonProperty("DECSTARTDATE")
    private String decstartdate;
    @JsonProperty("DETENDDATE")
    private String detenddate;
    @JsonProperty("DETSTARTDATE")
    private String detstartdate;
    @JsonProperty("IND")
    private String ind;
    @JsonProperty("INVOICES")
    private List<Invoice__1> invoices = null;
    @JsonProperty("MACNUM")
    private String macnum;

}
