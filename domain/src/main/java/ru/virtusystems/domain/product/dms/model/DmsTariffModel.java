package ru.virtusystems.domain.product.dms.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import ru.virtusystems.domain.model.evaluator.TariffModel;
import ru.virtusystems.domain.model.types.ContractParameter;
import ru.virtusystems.domain.utils.AmountUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@SuperBuilder
public class DmsTariffModel extends TariffModel {

    // Константы для входных параметров
    public static final String IN_PARAM_PROGRAM = "dogovor.programma";
    public static final String IN_PARAM_PERIOD = "dogovor.SrokStrahGod";
    public static final String IN_PARAM_START_DATE = "dogovor.dogSrokS";
    public static final String IN_PARAM_END_DATE = "dogovor.dogSrokPo";
    public static final String IN_PARAM_ISSUE_DATE = "dogovor.data";

    // Константы для параметров из НТ
    public static final String IN_PARAM_SETTING_TABLE_SUM = "dogovor.strahSummaNT";
    public static final String IN_PARAM_SETTING_TABLE_PREMIUM = "dogovor.strahPremiaNT";

    // Константы для выходных параметров
    public static final String OUT_PARAM_TOTAL_PREMIUM = "dogovor.premiaItogo";
    public static final String OUT_PARAM_CONTRACT_CODE = "dogovor.kodirovkaDogovora";

    // Маппинг программ на коды страховых сумм
    private static final Map<String, String> INSURANCE_CODE_PARAMETER = Map.of(
            "Лайт", "dogovor.dmsDTPlait.strahSumma",
            "Стандарт", "dogovor.dmsDTPstandart.strahSumma",
            "Премиум", "dogovor.dmsDTPpremium.strahSumma");

    // Маппинг программ на коды рисков
    private static final Map<String, String> INSURANCE_RISK_PARAMETER = Map.of(
            "Лайт", "dogovor.dmsDTPlait.strahovat",
            "Стандарт", "dogovor.dmsDTPstandart.strahovat",
            "Премиум", "dogovor.dmsDTPpremium.strahovat");

    // Поля состояния
    private String program;
    private String period;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime issueDate;
    private BigDecimal insuranceSumFromSettings;
    private BigDecimal premiumFromSettings;

    public BigDecimal getTotalPremium() {
        return AmountUtil.getAmountByObject(getParameterValueByCode(OUT_PARAM_TOTAL_PREMIUM));
    }

    public BigDecimal getInsuranceSum() {
        return AmountUtil.getAmountByObject(getParameterValueByCode(getInsuranceSumCode()));
    }

    public String getProductNumberCode() {
        return (String) getParameterValueByCode(OUT_PARAM_CONTRACT_CODE);
    }

    /**
     * Строит входные параметры
     */
    public List<ContractParameter> buildParametersState() {

        // Параметр программы
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_PROGRAM)
                .inValue(program)
                .build());
        // Параметр периода
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_PERIOD)
                .inValue(period)
                .build());
        // Параметр начала срока действия
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_START_DATE)
                .inValue(toDate(startDate))
                .build());
        // Параметр конец срока действия
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_END_DATE)
                .inValue(toDate(endDate))
                .build());
        // Параметр дата заключения
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_ISSUE_DATE)
                .inValue(toDate(issueDate))
                .build());

        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_SUM)
                .inValue(insuranceSumFromSettings)
                .build());
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_PREMIUM)
                .inValue(premiumFromSettings)
                .build());

        // Параметр страховой суммы
        String insuranceSumCode = getInsuranceSumCode();
        if (insuranceSumCode != null) {
            parameters.add(ContractParameter.builder()
                    .code(insuranceSumCode)
                    .inValue(insuranceSumFromSettings)
                    .build());
        }
        // Параметр риска
        String riskCode = getInsuranceRiskCode();
        if (riskCode != null) {
            parameters.add(ContractParameter.builder()
                    .code(riskCode)
                    .inValue(true)
                    .build());
        }

        return parameters;
    }

    /**
     * Получает код страховой суммы для программы
     */
    private String getInsuranceSumCode() {
        return Optional.ofNullable(program)
                .map(INSURANCE_CODE_PARAMETER::get)
                .orElse(null);
    }

    /**
     * Получает код риска для программы
     */
    private String getInsuranceRiskCode() {
        return Optional.ofNullable(program)
                .map(INSURANCE_RISK_PARAMETER::get)
                .orElse(null);
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime
                .atZone(ZoneId.systemDefault())  // привязываем к часовому поясу
                .toInstant());
    }
}
