package com.sixthradix.econetsigner.dtos;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrenciesXml implements Serializable {

    private static final long serialVersionUID = 34523453L;

    @JsonProperty("Name")
    private String name;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("Rate")
    private String rate;
}
