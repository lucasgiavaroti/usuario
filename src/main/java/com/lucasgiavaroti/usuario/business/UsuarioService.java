package com.lucasgiavaroti.usuario.business;

import com.lucasgiavaroti.usuario.business.converter.UsuarioConverter;
import com.lucasgiavaroti.usuario.business.dto.EnderecoRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.LoginRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.TelefoneRecordDTO;
import com.lucasgiavaroti.usuario.business.dto.UsuarioRecordDTO;
import com.lucasgiavaroti.usuario.infrastructure.entity.Endereco;
import com.lucasgiavaroti.usuario.infrastructure.entity.Telefone;
import com.lucasgiavaroti.usuario.infrastructure.entity.Usuario;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.ConflictException;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.NotFoundException;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.UnauthorizedException;
import com.lucasgiavaroti.usuario.infrastructure.repository.EnderecoRepository;
import com.lucasgiavaroti.usuario.infrastructure.repository.TelefoneRepository;
import com.lucasgiavaroti.usuario.infrastructure.repository.UsuarioRepository;
import com.lucasgiavaroti.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioRecordDTO salvaUsuario(UsuarioRecordDTO dto){
        emailExiste(dto.email());

        UsuarioRecordDTO usuarioRecord = new UsuarioRecordDTO(dto.nome(), dto.email(), passwordEncoder.encode(dto.senha()), dto.endereco(), dto.telefone());

        Usuario usuarioSaved = usuarioConverter.toUsuario(usuarioRecord);
        return usuarioConverter.toUsuarioRecordDTO( usuarioRepository.save(usuarioSaved));
    }

    public LoginRecordDTO efetuarLogin(UsuarioRecordDTO dto){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
            );

            String token = jwtUtil.generateToken(authentication.getName());

            return usuarioConverter.toLoginRecordDTO(token);
        }catch (BadCredentialsException | UsernameNotFoundException | AuthorizationDeniedException ex){
            throw new UnauthorizedException("Credenciais incorretas. Usuário ou senha inválidos: ", ex.getCause());
        }

    }

    public UsuarioRecordDTO buscaUsuarioPorEmail(String email){
        return usuarioConverter.toUsuarioRecordDTO( usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuário não encontrado com esse e-mail")));
    }

    public void deletarUsuarioPorEmail(String email){
        usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Usuário não encontrado com esse e-mail"));
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioRecordDTO atualizaDadosUsuario(String token, UsuarioRecordDTO dto){
        // busca e-mail do usuario pelo jwt (tirar obrigatoriedade do e-mail)
        String email = jwtUtil.extractEmail(token.substring(7));

        // criptografia de senha somente se o usuário tiver passado a senha, para não arriscar criptografar a mesma senha do banco novamente
        String senha = dto.senha() != null ? passwordEncoder.encode(dto.senha()) : null;

        UsuarioRecordDTO usuarioRecord = new UsuarioRecordDTO(dto.nome(), dto.email(), senha, dto.endereco(), dto.telefone());

        // busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Email do token não localizado"));

        // mescla os dados que recebemos do DTO com os dados já existentes no banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(usuarioRecord, usuarioEntity);

        return usuarioConverter.toUsuarioRecordDTO(usuarioRepository.save(usuario));
    }

    public EnderecoRecordDTO atualizaEndereco(Long id, EnderecoRecordDTO dto){

        Endereco enderecoEntity = enderecoRepository.findById(id).orElseThrow(() -> new NotFoundException("Endereço com esse id não encontrado"));

        Endereco endereco = usuarioConverter.updateEndereco(dto, enderecoEntity);

        return usuarioConverter.toEnderecoRecordDTO(enderecoRepository.save(endereco));
    }

    public TelefoneRecordDTO atualizaTelefone(Long id, TelefoneRecordDTO dto){

        Telefone telefoneEntity = telefoneRepository.findById(id).orElseThrow(() -> new NotFoundException("Telefone com esse id não encontrado") );

        Telefone telefone = usuarioConverter.updateTelefone(dto, telefoneEntity);

        return usuarioConverter.toTelefoneRecordDTO(telefoneRepository.save(telefone));
    }

    public EnderecoRecordDTO cadastraEndereco(String token, EnderecoRecordDTO dto){

        String email = jwtUtil.extractEmail(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("E-mail não encontrado"));

        Endereco endereco = usuarioConverter.toEnderecoEntity(dto, usuario.getId());

        return usuarioConverter.toEnderecoRecordDTO(enderecoRepository.save(endereco));

    }

    public TelefoneRecordDTO cadastraTelefone(String token, TelefoneRecordDTO dto){

        String email = jwtUtil.extractEmail(token.substring(7));

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("E-mail não encontrado"));

        Telefone telefone = usuarioConverter.toTelefoneEntity(dto, usuario.getId());

        return  usuarioConverter.toTelefoneRecordDTO(telefoneRepository.save(telefone));

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
