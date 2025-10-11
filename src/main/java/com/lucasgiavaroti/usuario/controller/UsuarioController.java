package com.lucasgiavaroti.usuario.controller;

import com.lucasgiavaroti.usuario.business.UsuarioService;
import com.lucasgiavaroti.usuario.business.dto.EnderecoRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.LoginRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.TelefoneRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.UsuarioRecordDTO;
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
    public ResponseEntity<UsuarioRecordDTO> salvarUsuario(@RequestBody UsuarioRecordDTO dto){
        return ResponseEntity.ok(usuarioService.salvaUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRecordDTO> login (@RequestBody UsuarioRecordDTO usuarioDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.email(), usuarioDTO.senha())
        );

        return  ResponseEntity.ok(new LoginRecordDTO("Bearer " + jwtUtil.generateToken(authentication.getName())));

    }

    @GetMapping
    public ResponseEntity<UsuarioRecordDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscaUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable("email") String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioRecordDTO> atualizaDadosUsuario(@RequestBody UsuarioRecordDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoRecordDTO> atualizaEnderecoUsuario(@RequestBody EnderecoRecordDTO dto, @RequestParam Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneRecordDTO> atualizaTelefoneUsuario(@RequestBody TelefoneRecordDTO dto, @RequestParam Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoRecordDTO> cadastraEndereco(@RequestBody EnderecoRecordDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, dto));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneRecordDTO> cadastraTelefone(@RequestBody TelefoneRecordDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }

}
