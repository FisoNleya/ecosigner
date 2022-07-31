
package com.sixthradix.econetsigner.dtos;

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
public class SignedInvoiceResponse implements Serializable {

    private static final long serialVersionUID = 344352L;

    @JsonProperty("invoice_number")
    private String invoiceNumber;
    @JsonProperty("bpn")
    private String BPN;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("invoice_amt")
    private String invoiceAMT;
    @JsonProperty("signature")
    private String signature;

}
