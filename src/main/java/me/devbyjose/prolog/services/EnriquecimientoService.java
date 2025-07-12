package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import me.devbyjose.prolog.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnriquecimientoService {
    
    @Autowired
    private ArchivoService archivoService;
    
    public void enrichHorarioInfo(Horario horario) {
        // Buscar información del curso
        Curso curso = buscarCursoPorId(horario.getCursoId());
        if (curso != null) {
            horario.setNombreCurso(curso.getNombre());
        } else {
            horario.setNombreCurso("Curso " + horario.getCursoId());
        }
        
        // Buscar información del docente
        Docente docente = buscarDocentePorId(horario.getDocenteId());
        if (docente != null) {
            horario.setNombreDocente(docente.getNombre());
        } else {
            horario.setNombreDocente("Docente " + horario.getDocenteId());
        }
        
        // Mapear aula a ubicación
        horario.setUbicacionAula(mapearAulaAUbicacion(horario.getAulaNumero()));
    }
    
    private Curso buscarCursoPorId(int cursoId) {
        List<Curso> cursos = archivoService.leerCursosDesdeArchivo();
        for (Curso curso : cursos) {
            if (curso.getCodigo().equals(String.valueOf(cursoId))) {
                return curso;
            }
        }
        return null;
    }
    
    private Docente buscarDocentePorId(long docenteId) {
        List<Docente> docentes = archivoService.leerDocentesDesdeArchivo();
        for (Docente docente : docentes) {
            if (docente.getId().equals(docenteId)) {
                return docente;
            }
        }
        return null;
    }
    
    private String mapearAulaAUbicacion(int aulaNumero) {
        return switch (aulaNumero) {
            case 101 -> "A-101";
            case 102 -> "A-102";
            case 201 -> "B-201";
            case 202 -> "B-202";
            case 301 -> "C-301";
            case 302 -> "C-302";
            case 401 -> "D-401";
            case 402 -> "D-402";
            default -> "Aula " + aulaNumero;
        };
    }
}