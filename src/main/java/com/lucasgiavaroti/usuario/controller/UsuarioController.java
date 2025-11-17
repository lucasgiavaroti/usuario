package com.lucasgiavaroti.usuario.controller;

import com.lucasgiavaroti.usuario.business.UsuarioService;
import com.lucasgiavaroti.usuario.business.dto.EnderecoRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.LoginRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.TelefoneRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.UsuarioRecordDTO;
import com.lucasgiavaroti.usuario.infrastructure.security.JwtUtil;
import com.lucasgiavaroti.usuario.infrastructure.security.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Cadastro de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    @Operation(summary = "Salvar usuários",description = "Cria uma novo usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário salvo com sucesso")
    @ApiResponse(responseCode = "409", description = "Já existe um usuário com esse e-mail")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    public ResponseEntity<UsuarioRecordDTO> salvarUsuario(@RequestBody UsuarioRecordDTO dto){
        return ResponseEntity.ok(usuarioService.salvaUsuario(dto));
    }

    @PostMapping("/login")
    @Operation(summary = "Efetuar login",description = "Realiza o login do usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais incorretas")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    public ResponseEntity<LoginRecordDTO> login (@RequestBody UsuarioRecordDTO usuarioDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioDTO.email(), usuarioDTO.senha())
        );

        return  ResponseEntity.ok(new LoginRecordDTO("Bearer " + jwtUtil.generateToken(authentication.getName())));

    }

    @GetMapping
    @Operation(summary = "Buscar usuário",description = "Busca um usuário por e-mail no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com esse e-mail")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    public ResponseEntity<UsuarioRecordDTO> buscaUsuarioPorEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(usuarioService.buscaUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Deletar usuário",description = "Deleta um usuário por e-mail no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com esse e-mail")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    public ResponseEntity<Void> deletaUsuarioPorEmail(@PathVariable("email") String email) {
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(summary = "Atualizar dados",description = "Atualiza os dados de um usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o e-mail passado no token")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
    public ResponseEntity<UsuarioRecordDTO> atualizaDadosUsuario(@RequestBody UsuarioRecordDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, dto));
    }

    @PutMapping("/endereco")
    @Operation(summary = "Atualizar endereço",description = "Atualiza os dados de endereço de um usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum endereço encontrado com esse id")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    public ResponseEntity<EnderecoRecordDTO> atualizaEnderecoUsuario(@RequestBody EnderecoRecordDTO dto, @RequestParam Long id){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id, dto));
    }

    @PutMapping("/telefone")
    @Operation(summary = "Atualizar telefone",description = "Atualiza os dados de telefone de um usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum telefone encontrado com esse id")
    public ResponseEntity<TelefoneRecordDTO> atualizaTelefoneUsuario(@RequestBody TelefoneRecordDTO dto, @RequestParam Long id){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id, dto));
    }

    @PostMapping("/endereco")
    @Operation(summary = "Criar endereço",description = "Cria um endereço para o  usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Endereço criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o e-mail passado no token")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
    public ResponseEntity<EnderecoRecordDTO> cadastraEndereco(@RequestBody EnderecoRecordDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraEndereco(token, dto));
    }

    @PostMapping("/telefone")
    @Operation(summary = "Criar telefone",description = "Cria um telefone para o  usuário no sistema")
    @ApiResponse(responseCode = "200", description = "Telefone criado com sucesso")
    @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o e-mail passado no token")
    @ApiResponse(responseCode = "500", description = "Erro interno de servidor")
    @SecurityRequirement(name = SecurityConfig.SECURITY_SCHEME)
    public ResponseEntity<TelefoneRecordDTO> cadastraTelefone(@RequestBody TelefoneRecordDTO dto, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.cadastraTelefone(token, dto));
    }

}
