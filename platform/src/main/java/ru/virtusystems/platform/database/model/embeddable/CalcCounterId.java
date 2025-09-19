package ru.virtusystems.platform.database.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CalcCounterId {
    private LocalDate day;
    private Long productId;
}
