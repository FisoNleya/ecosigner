
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
public class CurrenciesReceived implements Serializable {

    private static final long serialVersionUID = 335134453L;

    @JsonProperty("Currency")
    private List<Currency> currency;

}
