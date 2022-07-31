
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
public class Datum implements Serializable {

    private static final long serialVersionUID = 343452324L;

    @JsonProperty("Signature")
    private String signature;
    @JsonProperty("ZimraSubmitInvoices")
    private List<ZimraSubmitInvoice> zimraSubmitInvoices;
}
