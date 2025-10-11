package com.lucasgiavaroti.usuario.business.dto;

public record EnderecoRecordDTO(
        Long id,
        String rua,
        Long numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
) {
}
