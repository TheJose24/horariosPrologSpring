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
import org.jpl7.Query;

@Controller
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/generar-horarios")
    public String mostrarGenerarHorarios(Model model) {
        model.addAttribute("exito", false);
        model.addAttribute("mensaje", "");
        model.addAttribute("horarios", new ArrayList<>());
        return "generar_horarios";
    }

    @PostMapping("/generar-horarios")
    public String generarHorarios(Model model) {
        try {
            System.out.println("=== GENERANDO HORARIOS DINÁMICAMENTE EN JAVA ===");

            List<Horario> horarios = horarioService.generarYObtenerHorarios();

            if (horarios != null && !horarios.isEmpty()) {
                model.addAttribute("horarios", horarios);
                model.addAttribute("exito", true);
                model.addAttribute("mensaje", "Horarios generados dinámicamente (" + horarios.size() + " clases programadas)");
                System.out.println("Horarios generados: " + horarios.size());
            } else {
                model.addAttribute("exito", false);
                model.addAttribute("mensaje", "No se generaron horarios. Verifique que existan cursos y docentes en la base de conocimientos.");
                model.addAttribute("horarios", new ArrayList<>());
                System.out.println("No se generaron horarios");
            }
        } catch (Exception e) {
            System.err.println("Error al generar horarios: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("exito", false);
            model.addAttribute("mensaje", "Error al generar horarios: " + e.getMessage());
            model.addAttribute("horarios", new ArrayList<>());
        }
        return "generar_horarios";
    }
}