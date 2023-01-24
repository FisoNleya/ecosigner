package com.sixthradix.econetsigner.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sixthradix.econetsigner.utils.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report implements Serializable {

    private static final Long serialVersionUID = 8723414879L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", columnDefinition = "BIGINT")
    private Long reportId;

    private String userName;

    private String fullName;

    private String invoiceNumber;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    private String status;

    private String signature;
    private String currency;
    private BigDecimal invoiceAmount;
    private BigDecimal invoiceTaxAmount;
}
