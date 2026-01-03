package com.lucasgiavaroti.usuario.business.converter;

import com.lucasgiavaroti.usuario.business.dto.EnderecoRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.LoginRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.TelefoneRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.UsuarioRecordDTO;
import com.lucasgiavaroti.usuario.infrastructure.entity.Endereco;
import com.lucasgiavaroti.usuario.infrastructure.entity.Telefone;
import com.lucasgiavaroti.usuario.infrastructure.entity.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario toUsuario(UsuarioRecordDTO dto){
        return Usuario.builder()
                .email(dto.email())
                .nome(dto.nome())
                .senha(dto.senha())
                .enderecos(toListaEndereco(dto.endereco()))
                .telefones(toListaTelefone(dto.telefone()))
                .build();
    }

    public LoginRecordDTO toLoginRecordDTO(String token){
        return new LoginRecordDTO(token);
    }

    public List<Endereco> toListaEndereco(List<EnderecoRecordDTO> dto){
        /*
        *  stream coloca em uma esteira
        *  map é o que vai fazer para cada um, nesse caso, transformar cada um para entidade
        *  por último, convertemos em uma lista
         */
        return dto.stream().map(this::toEndereco).toList();
    }

    public Endereco toEndereco(EnderecoRecordDTO dto){
        return Endereco.builder()
                .rua(dto.rua())
                .numero(dto.numero())
                .cep(dto.cep())
                .bairro(dto.bairro())
                .cidade(dto.cidade())
                .complemento(dto.complemento())
                .estado(dto.estado())
                .build();
    }

    public List<Telefone> toListaTelefone(List<TelefoneRecordDTO> dto){
        return dto.stream().map(this::toTelefone).toList();
    }

    public Telefone toTelefone(TelefoneRecordDTO dto){
        return Telefone.builder()
                .ddd(dto.ddd())
                .numero(dto.numero())
                .build();
    }

    public UsuarioRecordDTO toUsuarioRecordDTO(Usuario usuario){
        return new UsuarioRecordDTO(usuario.getNome(), usuario.getEmail(), usuario.getSenha(), toListaEnderecoRecordDTO(usuario.getEnderecos()), toListaTelefoneRecordDTO(usuario.getTelefones()));
    }

    public List<EnderecoRecordDTO> toListaEnderecoRecordDTO(List<Endereco> endereco){
        return endereco.stream().map(this::toEnderecoRecordDTO).toList();
    }

    public EnderecoRecordDTO toEnderecoRecordDTO(Endereco endereco){
        return new EnderecoRecordDTO (
                endereco.getId(),
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    public List<TelefoneRecordDTO> toListaTelefoneRecordDTO(List<Telefone> telefone){
        return telefone.stream().map(this::toTelefoneRecordDTO).toList();
    }

    public TelefoneRecordDTO toTelefoneRecordDTO(Telefone telefone){
        return new TelefoneRecordDTO (
                telefone.getId(),
                telefone.getNumero(),
                telefone.getDdd()
        );
    }

    public Usuario updateUsuario(UsuarioRecordDTO dto, Usuario usuario ){
        return Usuario.builder()
                .nome(dto.nome() != null ? dto.nome() : usuario.getNome())
                .id(usuario.getId())
                .senha(usuario.getSenha() != null ? dto.senha() : usuario.getSenha())
                .email(usuario.getEmail() != null ? usuario.getEmail() : dto.email())
                .telefones(usuario.getTelefones()) // não alteramos telefones aqui
                .enderecos(usuario.getEnderecos()) // não alteramos enderecos aqui
                .build();
    }

    public Endereco updateEndereco(EnderecoRecordDTO dto, Endereco endereco){
        return Endereco.builder()
                .id(endereco.getId())
                .cep(dto.cep() != null ? dto.cep() : endereco.getCep())
                .rua(dto.rua() != null ? dto.rua() : endereco.getRua())
                .bairro(dto.bairro() != null ? dto.bairro() : endereco.getBairro())
                .numero(dto.numero() != null ? dto.numero() : endereco.getNumero())
                .cidade(dto.cidade() != null ? dto.cidade() : endereco.getCidade())
                .estado(dto.estado() != null ? dto.estado() : endereco.getEstado())
                .complemento(dto.complemento() != null ? dto.complemento() : endereco.getComplemento())
                .usuario_id(endereco.getUsuario_id())
                .build();
    }
    public Telefone updateTelefone(TelefoneRecordDTO dto, Telefone telefone){
        return Telefone.builder()
                .id(telefone.getId())
                .numero(dto.numero() != null ? dto.numero() : telefone.getNumero())
                .ddd(dto.ddd() != null ? dto.ddd() : telefone.getDdd())
                .usuario_id(telefone.getUsuario_id())
                .build();
    }

    public Endereco toEnderecoEntity(EnderecoRecordDTO dto, Long idUsuario){
        return Endereco.builder()
                .rua(dto.rua())
                .complemento(dto.complemento())
                .cep(dto.cep())
                .estado(dto.estado())
                .cidade(dto.cidade())
                .numero(dto.numero())
                .bairro(dto.bairro())
                .usuario_id(idUsuario)
                .build();
    }

    public Telefone toTelefoneEntity(TelefoneRecordDTO dto, Long idUsuario){
        return Telefone.builder()
                .ddd(dto.ddd())
                .numero(dto.numero())
                .usuario_id(idUsuario)
                .build();
    }

}
