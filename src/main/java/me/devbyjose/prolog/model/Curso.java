package me.devbyjose.prolog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Curso {
    private String codigo;
    private String nombre;
    private int ciclo;
    private String tipoCurso;
    private int horas;
    private String tipoAula;
}
