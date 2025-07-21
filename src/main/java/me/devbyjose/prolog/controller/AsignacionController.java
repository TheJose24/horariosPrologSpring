package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.AsignacionHorario;
import me.devbyjose.prolog.services.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import me.devbyjose.prolog.model.Horario;

@Controller
public class AsignacionController {

    @Autowired
    private AsignacionService asignacionService;

    @GetMapping("/ver-asignaciones")
    public String verAsignaciones(Model model) {
        Map<Integer, List<Horario>> asignaciones = asignacionService.obtenerAsignacionesPorCurso();
        model.addAttribute("asignaciones", asignaciones);
        return "ver_asignaciones";
    }
}
