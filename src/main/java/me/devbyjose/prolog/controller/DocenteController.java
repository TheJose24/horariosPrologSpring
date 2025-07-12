package me.devbyjose.prolog.controller;

import me.devbyjose.prolog.model.Docente;
import me.devbyjose.prolog.services.DocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @PostMapping("/registrar-docente")
    public String registrarDocente(@ModelAttribute("docenteNuevo") Docente docente,
                                   @RequestParam("especialidades") String[] especialidadesArray) {
        
        // Convertir array a lista
        List<String> especialidades = Arrays.asList(especialidadesArray);
        docente.setEspecialidades(especialidades);
        
        // Registrar docente (esto ya incluye guardar en Prolog)
        docenteService.registrarDocente(docente);
        
        return "redirect:/";
    }
}
