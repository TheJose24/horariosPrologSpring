package me.devbyjose.prolog.services;

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

    // Agrupa los horarios por ID de curso
    public Map<Integer, List<Horario>> obtenerAsignacionesPorCurso() {
        Map<Integer, List<Horario>> asignaciones = new HashMap<>();
        List<Horario> horarios = horarioService.obtenerTodosHorarios();
        for (Horario horario : horarios) {
            asignaciones.computeIfAbsent(horario.getCursoId(), k -> new ArrayList<>()).add(horario);
        }
        return asignaciones;
    }
} 