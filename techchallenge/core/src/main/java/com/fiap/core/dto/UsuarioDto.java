package com.fiap.core.dto;

public record UsuarioDto(
        String nome,
        String sobrenome,
        String email,
        String tipoUsuario
) {
}
