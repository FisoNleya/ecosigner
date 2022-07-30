
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
public class Icode implements Serializable {

    private static final long serialVersionUID = 3434523L;

    @JsonProperty("0")
    private String _0;
    @JsonProperty("1")
    private String _1;
    @JsonProperty("length")
    private Integer length;

}
