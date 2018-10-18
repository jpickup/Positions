package com.johnpickup.positions;

import com.johnpickup.positions.data.Book;
import com.johnpickup.positions.data.Instrument;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PositionKey {
    private final Book book;
    private final Instrument instrument;
}
