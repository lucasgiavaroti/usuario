package com.lucasgiavaroti.usuario.controller;

import com.lucasgiavaroti.usuario.business.UsuarioService;
import com.lucasgiavaroti.usuario.business.dto.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(usuarioService.salvaUsuario(dto));
    }

}
