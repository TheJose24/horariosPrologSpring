package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Docente;
import me.devbyjose.prolog.services.DocenteService;
import me.devbyjose.prolog.services.PrologService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class DocenteController {

    @Autowired
    private PrologService prologService;

    @Autowired
    private DocenteService docenteService;

    @PostMapping("/registrar-docente")
    public String registrarDocente(@ModelAttribute("docenteNuevo") Docente docente,
                                   @RequestParam("especialidades") String especialidadesRaw) {
        List<String> especialidades = Arrays.stream(especialidadesRaw.split(","))
                .map(String::trim).toList();
        docente.setEspecialidades(especialidades);
        docenteService.registrarDocente(docente);
        prologService.guardarDocenteEnProlog(docente);
        return "redirect:/";
    }




}
