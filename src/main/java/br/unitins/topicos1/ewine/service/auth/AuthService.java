package br.unitins.topicos1.ewine.service.auth;
// src/main/java/br/unitins/topicos1/ewine/service/auth/AuthService.java

import br.unitins.topicos1.ewine.dto. auth.LoginDTO;
import br.unitins.topicos1. ewine.dto.auth.LoginDTOResponse;

public interface AuthService {
    LoginDTOResponse login(LoginDTO loginDTO);
}
