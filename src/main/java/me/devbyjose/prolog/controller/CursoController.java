package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping("/registrar-curso")
    public String registrarCurso(@ModelAttribute Curso curso) {
        cursoService.registrarCurso(curso);
        return "redirect:/";
    }
}