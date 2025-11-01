package com.Imperium.DTOs;

import com.Imperium.Enum.StatusUsuario;
import com.Imperium.Enum.papelUsuario;

// Este record define os campos que O USUÁRIO PODE ATUALIZAR.
// Se ele não enviar um campo, o Java o tratará como 'null'.
public record UsuarioUpdateDTO(
    String senha,
    Integer idSetor,
    papelUsuario papelUsuario,
    StatusUsuario statusUsuario
) {}