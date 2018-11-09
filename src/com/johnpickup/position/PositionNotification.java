package com.johnpickup.position;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PositionNotification {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final Position position;
}
