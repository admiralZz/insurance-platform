package ru.virtusystems.platform.database.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.virtusystems.platform.database.model.ContractEntity;
import ru.virtusystems.platform.dto.filter.ContractFilter;

import java.util.ArrayList;
import java.util.List;

// Spring найдет эту реализацию и автоматически создаст бин
// Обязательно называть также как FilterUserRepository и обязательно с постфиксом Impl
// (@EnableJpaRepositories включена по умолчанию и обрабатывает все реализации интерфейсов именно с постфиксом Impl)
@RequiredArgsConstructor
public class FilterContractEntityRepositoryImpl implements FilterContractEntityRepository {

    private final EntityManager entityManager;

    @Override
    public List<ContractEntity> findContractsByFilter(ContractFilter filter) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        // создаем запрос на основе таблицы contract
        var criteria = criteriaBuilder.createQuery(ContractEntity.class);
        // откуда начинаем запрос
        var root = criteria.from(ContractEntity.class);

        Predicate predicate = createFilterPredicate(filter, criteriaBuilder, root);
        criteria.where(predicate);

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Page<ContractEntity> findContractsByFilter(ContractFilter filter, Pageable pageable) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        // создаем запрос на основе таблицы contract
        var query = criteriaBuilder.createQuery(ContractEntity.class);
        // откуда начинаем запрос
        var root = query.from(ContractEntity.class);

        // Фильтрация
        Predicate filterPredicate = createFilterPredicate(filter, criteriaBuilder, root);
        query.where(filterPredicate).select(root);

        // Основной запрос с пагинацией
        var typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        var resultList = typedQuery.getResultList();

        // Отдельный запрос для total count
        var countQuery = criteriaBuilder.createQuery(Long.class);
        var countRoot = countQuery.from(ContractEntity.class);
        Predicate countPredicate = createFilterPredicate(filter, criteriaBuilder, countRoot);
        countQuery.select(criteriaBuilder.count(countRoot)).where(countPredicate);
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    private Predicate createFilterPredicate(ContractFilter filter,
                                            CriteriaBuilder criteriaBuilder,
                                            Root<ContractEntity> root) {
        var productJoin = root.join("product", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.product() != null && !filter.product().isEmpty()) {
            predicates.add(
                    criteriaBuilder.equal(productJoin.get("name"), filter.product())
            );
        }
        if (filter.status() != null) {
            predicates.add(criteriaBuilder.equal(root
                    .get("status"), filter.status()));
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
