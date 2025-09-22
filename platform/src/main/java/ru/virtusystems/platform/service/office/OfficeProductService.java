package ru.virtusystems.platform.service.office;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtusystems.platform.database.repository.ProductEntityRepository;
import ru.virtusystems.platform.dto.ReadProductDto;
import ru.virtusystems.platform.mapper.ProductMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OfficeProductService {
    private final ProductEntityRepository productEntityRepository;
    private final ProductMapper productMapper;

    public List<ReadProductDto> getAllProducts() {
        return productEntityRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
}
