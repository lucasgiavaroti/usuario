package com.lucasgiavaroti.usuario.infrastructure.exceptions.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String message,
        String error,
        String path
) {
}
