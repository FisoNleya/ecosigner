
package com.econet.econetsigner.dtos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice__1 implements Serializable {

    private static final long serialVersionUID = 2342L;

    @JsonProperty("RECORD")
    private List<Record> record ;

}
