
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
public class Response implements Serializable {

    private static final long serialVersionUID = 344352L;

    @JsonProperty("Code")
    private String code;
    @JsonProperty("Data")
    private List<Datum> data = null;
    @JsonProperty("Message")
    private String message;

}
