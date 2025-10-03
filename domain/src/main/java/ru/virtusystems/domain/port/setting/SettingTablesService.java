package ru.virtusystems.domain.port.setting;

import ru.virtusystems.domain.model.evaluator.TariffModel;

public interface SettingTablesService {
    TariffModel getDefaultSettingsTables();
}
