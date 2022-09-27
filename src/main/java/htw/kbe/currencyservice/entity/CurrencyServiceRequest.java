package htw.kbe.currencyservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class CurrencyServiceRequest {


        private String from;
        private String to;
        private Double quantity;





    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Double getQuantity() {
        return quantity;
    }




}

