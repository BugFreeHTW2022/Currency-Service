package htw.kbe.currencyservice.entity;

import java.math.BigDecimal;

public class CurrencyServiceResponse {

    private String from;
    private String to;
    private Double totalCalculatedAmount;

    public CurrencyServiceResponse(String from, String to, Double totalCalculatedAmount) {
        this.from = from;
        this.to = to;
        this.totalCalculatedAmount = totalCalculatedAmount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getTotalCalculatedAmount() {
        return totalCalculatedAmount;
    }
}
