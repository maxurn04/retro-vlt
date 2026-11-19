package com.back.retro_vlt.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String correo;
    private String password;

    public String getContra() {
        return password;
    }

    public void setContra(String contra) {
        this.password = contra;
    }
}
