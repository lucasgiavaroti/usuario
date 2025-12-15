package com.lucasgiavaroti.usuario.infrastructure.clients;

import com.lucasgiavaroti.usuario.business.dto.ViaCepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "via-cep", url = "${viacep.url}")
public interface ViaCepClient {

    @GetMapping("/ws/{cep}/json")
    ViaCepDTO buscaEnderecoPorCep(@PathVariable("cep") String cep);

}
