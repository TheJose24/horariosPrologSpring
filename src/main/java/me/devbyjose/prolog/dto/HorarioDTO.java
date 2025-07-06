package me.devbyjose.prolog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.DiaSemana;
import me.devbyjose.prolog.enums.TipoSesion;
import me.devbyjose.prolog.enums.Turno;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {
    private Long docenteId;
    private String nombreDocente;
    private Long cursoId;
    private String nombreCurso;
    private DiaSemana dia;
    private Long bloqueId;
    private BigDecimal horaInicio;
    private BigDecimal horaFin;
    private Turno turno;
    private Long aulaId;
    private String nombreAula;
    private TipoSesion tipoSesion;
}