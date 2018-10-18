package com.johnpickup.positions;

import com.johnpickup.positions.data.Book;
import com.johnpickup.positions.data.Instrument;
import lombok.*;

@RequiredArgsConstructor
@Data
public class PositionKey {
    private final Book book;
    private final Instrument instrument;
}
