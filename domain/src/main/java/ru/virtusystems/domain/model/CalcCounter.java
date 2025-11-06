package ru.virtusystems.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalcCounter {

    private LocalDate day;

    private Long counter;

    private Product product;
}
