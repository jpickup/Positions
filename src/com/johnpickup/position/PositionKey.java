package com.johnpickup.position;

import com.johnpickup.data.Book;
import com.johnpickup.data.Instrument;
import lombok.*;

@RequiredArgsConstructor
@Data
public class PositionKey {
    private final Book book;
    private final Instrument instrument;
}
