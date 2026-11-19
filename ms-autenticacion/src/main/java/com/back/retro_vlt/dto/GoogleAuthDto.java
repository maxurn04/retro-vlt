package com.back.retro_vlt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthDto {

    private String token;
    private String credential;
    private String correo;
    private String nombre;
    private String picture;
}
