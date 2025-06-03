package com.lucasgiavaroti.usuario.business.converter;

import com.lucasgiavaroti.usuario.business.dto.EnderecoDTO;
import com.lucasgiavaroti.usuario.business.dto.TelefoneDTO;
import com.lucasgiavaroti.usuario.business.dto.UsuarioDTO;
import com.lucasgiavaroti.usuario.infrastructure.entity.Endereco;
import com.lucasgiavaroti.usuario.infrastructure.entity.Telefone;
import com.lucasgiavaroti.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario toUsuario(UsuarioDTO dto){
        return Usuario.builder()
                .email(dto.getEmail())
                .nome(dto.getNome())
                .senha(dto.getSenha())
                .enderecos(toListaEndereco(dto.getEndereco()))
                .telefones(toListaTelefone(dto.getTelefone()))
                .build();
    }

    public List<Endereco> toListaEndereco(List<EnderecoDTO> dto){
        /*
        *  stream coloca em uma esteira
        *  map é o que vai fazer para cada um, nesse caso, transformar cada um para entidade
        *  por último, convertemos em uma lista
         */
        return dto.stream().map(this::toEndereco).toList();
    }

    public Endereco toEndereco(EnderecoDTO dto){
        return Endereco.builder()
                .rua(dto.getRua())
                .numero(dto.getNumero())
                .cep(dto.getCep())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .complemento(dto.getComplemento())
                .estado(dto.getEstado())
                .build();
    }

    public List<Telefone> toListaTelefone(List<TelefoneDTO> dto){
        return dto.stream().map(this::toTelefone).toList();
    }

    public Telefone toTelefone(TelefoneDTO dto){
        return Telefone.builder()
                .ddd(dto.getDdd())
                .numero(dto.getNumero())
                .build();
    }

    public UsuarioDTO toUsuarioDTO(Usuario usuario){
        return UsuarioDTO.builder()
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .senha(usuario.getSenha())
                .endereco(toListaEnderecoDTO(usuario.getEnderecos()))
                .telefone(toListaTelefoneDTO(usuario.getTelefones()))
                .build();
    }

    public List<EnderecoDTO> toListaEnderecoDTO(List<Endereco> endereco){
        return endereco.stream().map(this::toEnderecoDTO).toList();
    }

    public EnderecoDTO toEnderecoDTO(Endereco endereco){
        return EnderecoDTO.builder()
                .bairro(endereco.getBairro())
                .cep(endereco.getCep())
                .cidade(endereco.getCidade())
                .complemento(endereco.getComplemento())
                .estado(endereco.getEstado())
                .numero(endereco.getNumero())
                .rua(endereco.getRua())
                .build();
    }

    public List<TelefoneDTO> toListaTelefoneDTO(List<Telefone> telefone){
        return telefone.stream().map(this::toTelefoneDTO).toList();
    }

    public TelefoneDTO toTelefoneDTO(Telefone telefone){
        return TelefoneDTO.builder()
                .ddd(telefone.getDdd())
                .numero(telefone.getNumero())
                .build();
    }

}
