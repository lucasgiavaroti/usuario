package com.lucasgiavaroti.usuario.business;

import com.lucasgiavaroti.usuario.business.converter.UsuarioConverter;
import com.lucasgiavaroti.usuario.business.dto.UsuarioDTO;
import com.lucasgiavaroti.usuario.infrastructure.entity.Usuario;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.ConflictException;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.NotFoundException;
import com.lucasgiavaroti.usuario.infrastructure.repository.UsuarioRepository;
import com.lucasgiavaroti.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

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

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO dto){
        // busca e-mail do usuario pelo jwt (tirar obrigatoriedade do email)
        String email = jwtUtil.extractEmail(token.substring(7));

        // criptografia de senha somente se o usuário tiver passado a senha, para não correr o risco de criptografar a mesma senha do banco novamente
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);

        // busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email do token não localizado"));

        // mescla os dados que recebemos do DTO com os dados já existentes no banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

        return usuarioConverter.toUsuarioDTO(usuarioRepository.save(usuario));
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
