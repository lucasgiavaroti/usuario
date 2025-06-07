package com.lucasgiavaroti.usuario.business;

import com.lucasgiavaroti.usuario.business.converter.UsuarioConverter;
import com.lucasgiavaroti.usuario.business.dto.UsuarioDTO;
import com.lucasgiavaroti.usuario.infrastructure.entity.Usuario;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.ConflictException;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.NotFoundException;
import com.lucasgiavaroti.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    private final PasswordEncoder passwordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO dto){
        emailExiste(dto.getEmail());

        dto.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario usuarioSaved = usuarioConverter.toUsuario(dto);
        return usuarioConverter.toUsuarioDTO( usuarioRepository.save(usuarioSaved));
    }

    public UsuarioDTO buscaUsuarioPorEmail(String email){
        return usuarioConverter.toUsuarioDTO( usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuário não encontrado com esse e-mail")));
    }

    public void deletarUsuarioPorEmail(String email){
        usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuário não encontrado com esse e-mail"));
        usuarioRepository.deleteByEmail(email);
    }

    public void emailExiste(String email) {
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("E-mail já cadastrado " + email);
            }
        }catch(ConflictException e){
            throw new ConflictException(e.getMessage(), e.getCause());
        }
    }

    // Responsável apenas para chamar nosso repository (Pode ser utilizado em outros contextos)
    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

}
