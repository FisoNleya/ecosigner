
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
public class ZimraSubmitInvoice implements Serializable {

    private static final long serialVersionUID = 3345223L;

    @JsonProperty("@xmlns")
    private String xmlns;
    @JsonProperty("INVOICE")
    private List<Invoice> invoice;
}
