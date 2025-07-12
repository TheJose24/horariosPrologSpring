package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import me.devbyjose.prolog.services.PrologService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CursoController {

    @Autowired
    private PrologService prologService;

    private final List<Curso> listaCursos = new ArrayList<>();

    @GetMapping("/")
    public String mostrarPaginaPrincipal(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("cursos", listaCursos);
        model.addAttribute("docenteNuevo", new Docente());
        model.addAttribute("docentes", prologService.obtenerDocentesDesdeProlog());
        return "sistema_asignacion_horarios";
    }

    @PostMapping("/registrar-curso")
    public String registrarCurso(@ModelAttribute Curso curso) {
        listaCursos.add(curso);
        prologService.guardarCursoEnProlog(curso);
        return "redirect:/";
    }

}