package com.lucasgiavaroti.usuario.controller;

import com.lucasgiavaroti.usuario.business.UsuarioService;
import com.lucasgiavaroti.usuario.business.dto.LoginDTO;
import com.lucasgiavaroti.usuario.business.dto.UsuarioDTO;
import com.lucasgiavaroti.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody UsuarioDTO dto){
        return ResponseEntity.ok(usuarioService.salvaUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login (@RequestBody UsuarioDTO usuarioDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail(), usuarioDTO.getSenha())
        );

        return  ResponseEntity.ok(new LoginDTO("Bearer " + jwtUtil.generateToken(authentication.getName())));

    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscaUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable("email") String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

}
