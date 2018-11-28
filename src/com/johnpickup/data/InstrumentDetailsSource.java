package com.johnpickup.data;

public interface InstrumentDetailsSource {
    InstrumentDetails lookup(Instrument instrument);
}
