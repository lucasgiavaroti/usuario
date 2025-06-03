package com.lucasgiavaroti.usuario.business;

import com.lucasgiavaroti.usuario.business.converter.UsuarioConverter;
import com.lucasgiavaroti.usuario.business.dto.UsuarioDTO;
import com.lucasgiavaroti.usuario.infrastructure.entity.Usuario;
import com.lucasgiavaroti.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO dto){
        Usuario usuarioSaved = usuarioConverter.toUsuario(dto);
        return usuarioConverter.toUsuarioDTO( usuarioRepository.save(usuarioSaved));
    }

}
