package br.com.visitsafe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public  class AuthRequestDTO {
    private String email;
    private String password;
}