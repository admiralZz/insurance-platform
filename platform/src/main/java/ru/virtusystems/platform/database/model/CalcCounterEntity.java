package ru.virtusystems.platform.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.virtusystems.platform.database.model.embeddable.CalcCounterId;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calc_counter")
public class CalcCounterEntity {

    @EmbeddedId
    private CalcCounterId id;

    @MapsId("productId") // связываем поле productId в ключе с сущностью
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Long counter;
}
