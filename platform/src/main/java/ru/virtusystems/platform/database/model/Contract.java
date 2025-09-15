package ru.virtusystems.platform.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.virtusystems.database.model.types.ContractParameter;
import ru.virtusystems.database.model.types.ContractStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String calcId;

    @Column(unique = true)
    private String number;

    private LocalDateTime calcDate;
    private LocalDateTime issueDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal premium;
    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal insuredSum;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb") // именно для Postgres
    private List<ContractParameter> params;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "insured_id")
    private Insured insured;
}
