package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Horario;
import me.devbyjose.prolog.services.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/generar-horarios")
    public String mostrarGenerarHorarios(Model model) {
        // Inicializar valores por defecto para evitar errores de null
        model.addAttribute("exito", false);
        model.addAttribute("mensaje", "");
        model.addAttribute("horarios", new ArrayList<>());
        return "generar_horarios";
    }

    @PostMapping("/generar-horarios")
    public String generarHorarios(@RequestParam(defaultValue = "BASICO") String tipoAlgoritmo, Model model) {
        try {
            List<Horario> horarios = horarioService.generarHorarios(tipoAlgoritmo);
            if (horarios != null && !horarios.isEmpty()) {
                model.addAttribute("horarios", horarios);
                model.addAttribute("exito", true);
                model.addAttribute("mensaje", "Horarios generados exitosamente (" + horarios.size() + " clases programadas)");
            } else {
                model.addAttribute("exito", false);
                model.addAttribute("mensaje", "No se pudieron generar horarios. Verifique que hay cursos y docentes registrados.");
                model.addAttribute("horarios", new ArrayList<>());
            }
        } catch (Exception e) {
            model.addAttribute("exito", false);
            model.addAttribute("mensaje", "Error al generar horarios: " + e.getMessage());
            model.addAttribute("horarios", new ArrayList<>());
        }
        return "resultado_horarios";
    }
}