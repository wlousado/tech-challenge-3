package com.fiap.core.dto;

public record ConsultaDto(
        String descricao,
        UsuarioDto paciente,
        UsuarioDto medico,
        String data
) {
}
