package com.lucasgiavaroti.usuario.business.dto;

import com.lucasgiavaroti.usuario.infrastructure.entity.Endereco;
import com.lucasgiavaroti.usuario.infrastructure.entity.Telefone;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {
    
    private String nome;
    private String email;
    private String senha;
    private List<EnderecoDTO> endereco;
    private List<TelefoneDTO> telefone;

}
