package ru.virtusystems.platform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.virtusystems.platform.dto.PageResponse;
import ru.virtusystems.platform.dto.ReadContractDto;
import ru.virtusystems.platform.dto.filter.ContractFilter;
import ru.virtusystems.platform.service.office.OfficeDispatcherService;

@Slf4j
@Sql({
        "classpath:sql/insured.sql",
        "classpath:sql/contract.sql"
})
@RequiredArgsConstructor
public class OfficeDispatcherServiceTest extends IntegrationTest {
    private final OfficeDispatcherService officeDispatcherService;

    @Test
    public void testGettingContracts() {
        PageRequest request = PageRequest.of(1, 20, Sort.by("id"));
        PageResponse<ReadContractDto> page =
                officeDispatcherService.getContractPageResponse(new ContractFilter(null, null), request);
        log.info(page.toString());
    }

}
