package com.back.retro_vlt.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String correo;
    private String password;
}
