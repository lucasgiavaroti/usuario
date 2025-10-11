package com.lucasgiavaroti.usuario.business.dto;

import java.util.List;

public record UsuarioRecordDTO(
         String nome,
         String email,
         String senha,
         List<EnderecoRecordDTO>endereco,
         List<TelefoneRecordDTO> telefone
) {
}
