package com.lucasgiavaroti.usuario.business;

import com.lucasgiavaroti.usuario.business.dto.ViaCepDTO;
import com.lucasgiavaroti.usuario.infrastructure.clients.ViaCepClient;
import com.lucasgiavaroti.usuario.infrastructure.exceptions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient viaCepClient;

    public ViaCepDTO buscaEnderecoPorCep(String cep){

        // Tratamento do CEP (caso espaço retira eles, caso alfanuméricos retorna um erro)
        return viaCepClient.buscaEnderecoPorCep(tratarCep(cep));

    }

    private String tratarCep(String cep){
        String cepTratado = cep.replace(" ","")
                .replace("-", "");

        // Regex: Ter apenas números e mais de um dígito e se o tamanho do CEP for menor que oito dígitos
        if(!cepTratado.matches("\\d+") || !Objects.equals(cepTratado.length(), 8)) {
            throw new IllegalArgumentException("O CEP contém caracteres inválidos.");
        }

        return cepTratado;

    }

}
