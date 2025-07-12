package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import org.springframework.stereotype.Service;

@Service
public class PrologFormatterService {
    
    public String formatearCursoParaArchivo(Curso curso) {
        return String.format("curso(%s, '%s', %d, %s, %s, 4, %s).%n",
                curso.getCodigo(), 
                curso.getNombre(), 
                curso.getCiclo(), 
                curso.getTipoCurso().toLowerCase(), 
                mapearTipoSesion(curso.getHoras()),
                crearListaEquipamiento(curso.getTipoAula()));
    }
    
    public String formatearDocenteParaArchivo(Docente docente) {
        String especialidades = "[" + String.join(", ", docente.getEspecialidades().stream()
                .map(String::toLowerCase).toList()) + "]";
        return String.format("docente(%d, '%s', %s).%n",
                docente.getId(), docente.getNombre(), especialidades);
    }
    
    public String mapearTipoSesion(int dias) {
        return switch (dias) {
            case 1 -> "teorico";
            case 2 -> "practico";
            case 3 -> "teorico_practico";
            default -> "teorico";
        };
    }
    
    public String crearListaEquipamiento(String tipoAula) {
        return switch (tipoAula) {
            case "Lab. de Computacion" -> "[computadoras, internet]";
            case "Aula Tradicional" -> "[proyector, pizarra]";
            case "Lab. de Redes" -> "[computadoras, equipos_redes]";
            default -> "[proyector]";
        };
    }
}