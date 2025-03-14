package demo.dto;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.springframework.expression.ParseException;

public class CurrencyDTO {

    private String code;
    private String symbol;
    private String description;
    private String chartName;
    private String disclaimer;
    private Double rate;
    private Double rateFloat;
    private String updated;
    private String updatedIso;
    private String updatedUk;

    public static CurrencyDTO fromCurrencyData(Map<String, Object> currencyData, String chartName, String disclaimer,
                                               String updated, String updatedIso, String updatedUk) throws ParseException, java.text.ParseException {
        CurrencyDTO dto = new CurrencyDTO();
        dto.code = (String) currencyData.get("code");
        dto.symbol = (String) currencyData.get("symbol");
        dto.description = (String) currencyData.get("description");
        dto.chartName = chartName;
        dto.disclaimer = disclaimer;

        String rateStr = (String) currencyData.get("rate");
        dto.rate = rateStr != null ? Double.parseDouble(rateStr.replace(",", "")) : null;
        dto.rateFloat = (Double) currencyData.get("rate_float");

        dto.updated = parseUpdatedDateToString(updated);
        dto.updatedIso = parseIsoDateToString(updatedIso);
        dto.updatedUk = parseUkDateToString(updatedUk);

        return dto;
    }

    private static String parseIsoDateToString(String isoDateStr) {
        if (isoDateStr == null) return null;
        OffsetDateTime odt = OffsetDateTime.parse(isoDateStr); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return odt.format(formatter);
    }

    private static String parseUkDateToString(String ukDateStr) throws ParseException, java.text.ParseException {
        if (ukDateStr == null) return null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy 'at' HH:mm z", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = inputFormat.parse(ukDateStr);
        return outputFormat.format(date);
    }

    private static String parseUpdatedDateToString(String updatedStr) throws ParseException, java.text.ParseException {
        if (updatedStr == null) return null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = inputFormat.parse(updatedStr);
        return outputFormat.format(date);
    }

    // Getters and setters

    public String getCode() {
        return code;
    }

    public String getChartName() {
        return chartName;
    }

    public Double getRate() {
        return rate;
    }

    public String getUpdatedIso() {
        return updatedIso;
    }

    public String getUpdatedUk() {
        return updatedUk;
    }

    public String getUpdated() {
        return updated;
    }
}