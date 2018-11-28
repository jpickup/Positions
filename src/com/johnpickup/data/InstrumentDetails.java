package com.johnpickup.data;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InstrumentDetails {
    private final LocalDate maturityDate;
    
    public boolean isPerpetual() {
        return maturityDate==null;
    }
}
