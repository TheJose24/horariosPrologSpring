package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import me.devbyjose.prolog.services.DocenteService;
import me.devbyjose.prolog.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired
    private DocenteService docenteService;
    
    @Autowired
    private CursoService cursoService;

    @GetMapping("/")
    public String mostrarPaginaPrincipal(Model model) {
        model.addAttribute("curso", new Curso());
        model.addAttribute("cursos", cursoService.obtenerTodosCursos());
        model.addAttribute("docenteNuevo", new Docente());
        model.addAttribute("docentes", docenteService.obtenerTodosDocentes());
        return "sistema_asignacion_horarios";
    }
}
