package ru.virtusystems.domain.product.zachitadohoda20.model;

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
public class ZachitaDohoda20TariffModel extends TariffModel {

    // Константы для входных параметров
    public static final String IN_PARAM_PROGRAM = "dogovor.programm";
    public static final String IN_PARAM_PERIOD = "dogovor.insurancePeriod";
    public static final String IN_PARAM_START_DATE = "dogovor.dogSrokS";
    public static final String IN_PARAM_END_DATE = "dogovor.dogSrokPo";
    public static final String IN_PARAM_ISSUE_DATE = "dogovor.data";
    public static final String IN_PARAM_PERCENT_TYPE = "dogovor.tipprocentov";

    // Константы для рисков
    public static final String IN_PARAM_RISK = "dogovor.risknepredvidrash.strahovat";

    // Константы для параметров из НТ
    public static final String IN_PARAM_SETTING_TABLE_SUM = "dogovor.strahSummaNT";
    public static final String IN_PARAM_SETTING_TABLE_PREMIUM = "dogovor.strahPremiaNT";
    public static final String IN_PARAM_SETTING_TABLE_ACCESS_PERIOD = "dogovor.accessSrokStrah";
    public static final String IN_PARAM_SETTING_TABLE_ACCESS_PROGRAM = "dogovor.accessProgram";
    public static final String IN_PARAM_SETTING_TABLE_PAYMENT_METHOD = "dogovor.accessSposobOplati";

    // Константы для выходных параметров
    public static final String OUT_PARAM_TOTAL_PREMIUM = "dogovor.premiaItogo";
    public static final String OUT_PARAM_INSURANCE_SUM = "dogovor.strahSummaDog";
    public static final String OUT_PARAM_CONTRACT_CODE = "dogovor.kodirovkaDogovora";

    // Поля состояния
    private String program;
    private String period;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime issueDate;
    private BigDecimal insuranceSumFromSettings;
    private BigDecimal premiumFromSettings;
    private String accessibleProgramFromSettings;
    private String accessiblePeriodFromSettings;
    private String accessiblePaymentMethodFromSettings;

    public BigDecimal getTotalPremium() {
        return AmountUtil.getAmountByObject(getParameterValueByCode(OUT_PARAM_TOTAL_PREMIUM));
    }

    public BigDecimal getInsuranceSum() {
        return AmountUtil.getAmountByObject(getParameterValueByCode(OUT_PARAM_INSURANCE_SUM));
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
        // Параметр какой-то процентный тип
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_PERCENT_TYPE)
                .inValue("Без капитализации")
                .build());

        // Риск
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_RISK)
                .inValue(true)
                .build());

        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_SUM)
                .inValue(insuranceSumFromSettings)
                .build());
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_PREMIUM)
                .inValue(premiumFromSettings)
                .build());
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_ACCESS_PERIOD)
                .inValue(accessiblePeriodFromSettings)
                .build());
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_ACCESS_PROGRAM)
                .inValue(accessibleProgramFromSettings)
                .build());
        parameters.add(ContractParameter.builder()
                .code(IN_PARAM_SETTING_TABLE_PAYMENT_METHOD)
                .inValue(accessiblePaymentMethodFromSettings)
                .build());



        return parameters;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime
                .atZone(ZoneId.systemDefault())  // привязываем к часовому поясу
                .toInstant());
    }
}
