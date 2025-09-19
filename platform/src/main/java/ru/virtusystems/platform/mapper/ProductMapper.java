package ru.virtusystems.platform.mapper;

import org.mapstruct.Mapper;
import ru.virtusystems.domain.model.Contract;
import ru.virtusystems.domain.model.Product;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.database.model.ProductEntity;
import ru.virtusystems.platform.dto.ReadContractDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity productEntity);
}
