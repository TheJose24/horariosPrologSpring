package me.devbyjose.prolog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Horario {
    private int cursoId;
    private int docenteId;
    private int aulaNumero;
    private String dia;
    private int horaInicio;
    private int horaFin;
    private String nombreCurso;
    private String nombreDocente;
    private String ubicacionAula;
}