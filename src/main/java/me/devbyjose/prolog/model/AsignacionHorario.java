package me.devbyjose.prolog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionHorario {
    private String nombreDocente;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String modalidad;
}

