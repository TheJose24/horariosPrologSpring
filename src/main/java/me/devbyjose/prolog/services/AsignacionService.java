package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.AsignacionHorario;
import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Horario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsignacionService {
    
    @Autowired
    private HorarioService horarioService;

    public Map<Curso, List<AsignacionHorario>> obtenerAsignacionesCursos() {
        Map<Curso, List<AsignacionHorario>> asignaciones = new HashMap<>();
        
        try {
            // Obtener horarios generados
            List<Horario> horarios = horarioService.generarHorarios("BASICO");
            
            // Procesar horarios y crear asignaciones
            for (Horario horario : horarios) {
                Curso curso = new Curso();
                curso.setCodigo(String.valueOf(horario.getCursoId()));
                curso.setNombre(horario.getNombreCurso());
                
                AsignacionHorario asignacion = new AsignacionHorario();
                asignacion.setNombreDocente(horario.getNombreDocente());
                asignacion.setDia(horario.getDia());
                asignacion.setHoraInicio(horario.getHoraInicio() + ":00");
                asignacion.setHoraFin(horario.getHoraFin() + ":00");
                asignacion.setModalidad("Presencial");
                
                asignaciones.computeIfAbsent(curso, k -> new ArrayList<>()).add(asignacion);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener asignaciones: " + e.getMessage());
        }
        
        return asignaciones;
    }
}