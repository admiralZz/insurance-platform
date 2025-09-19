package ru.virtusystems.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractNumberCounter {

    private Long id;

    private Long counter;

    private Product product;
}
