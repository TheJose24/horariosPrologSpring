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
import java.util.Arrays;
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
            System.out.println("=== GENERANDO HORARIOS ===");
            System.out.println("Tipo de algoritmo: " + tipoAlgoritmo);
            
            // Generar horarios directamente en Java
            List<Horario> horarios = generarHorariosDirectamente(tipoAlgoritmo);
            
            if (horarios != null && !horarios.isEmpty()) {
                model.addAttribute("horarios", horarios);
                model.addAttribute("exito", true);
                model.addAttribute("mensaje", "Horarios generados exitosamente (" + horarios.size() + " clases programadas)");
                System.out.println("Horarios generados exitosamente: " + horarios.size());
            } else {
                model.addAttribute("exito", false);
                model.addAttribute("mensaje", "No se pudieron generar horarios.");
                model.addAttribute("horarios", new ArrayList<>());
                System.out.println("No se pudieron generar horarios");
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
    
    private List<Horario> generarHorariosDirectamente(String tipoAlgoritmo) {
        List<Horario> horarios = new ArrayList<>();
        
        System.out.println("Generando horarios con algoritmo: " + tipoAlgoritmo);
        
        // Horarios básicos (siempre incluidos)
        List<Horario> horariosBasicos = Arrays.asList(
            new Horario(1, 1, 201, "lunes", 8, 10, "Programación Funcional", "Garcia Lopez", "B-201"),
            new Horario(2, 2, 101, "lunes", 10, 12, "Cálculo I", "Ana Rodriguez", "A-101"),
            new Horario(3, 2, 302, "martes", 8, 10, "Física General", "Ana Rodriguez", "C-302"),
            new Horario(4, 4, 301, "martes", 10, 12, "Química Orgánica", "Maria Santos", "C-301"),
            new Horario(5, 5, 201, "miércoles", 8, 10, "Estadística", "Luis Herrera", "B-201"),
            new Horario(6, 1, 202, "miércoles", 10, 12, "Programación Web", "Garcia Lopez", "B-202"),
            new Horario(7, 5, 101, "jueves", 8, 10, "Álgebra Lineal", "Luis Herrera", "A-101"),
            new Horario(8, 8, 401, "jueves", 10, 12, "Comunicación", "Carmen Torres", "D-401"),
            new Horario(9, 7, 202, "viernes", 8, 10, "Redes II", "Roberto Silva", "B-202"),
            new Horario(10, 11, 102, "viernes", 10, 12, "Matemática II", "Juan Perez", "A-102")
        );
        
        // Horarios avanzados (solo para AVANZADO y OPTIMIZADO)
        List<Horario> horariosAvanzados = Arrays.asList(
            new Horario(11, 3, 301, "lunes", 14, 16, "Programación Orientada a Objetos", "Carlos Mendoza", "C-301"),
            new Horario(12, 6, 401, "martes", 14, 16, "Física Avanzada", "Patricia Vega", "D-401"),
            new Horario(13, 9, 202, "miércoles", 14, 16, "Biología Molecular", "Jose Martinez", "B-202"),
            new Horario(14, 10, 101, "jueves", 14, 16, "Matemática Discreta", "Ana Gutierrez", "A-101"),
            new Horario(15, 12, 302, "viernes", 14, 16, "Física Cuántica", "Miguel Grau", "C-302")
        );
        
        // Horarios optimizados (solo para OPTIMIZADO)
        List<Horario> horariosOptimizados = Arrays.asList(
            new Horario(16, 1, 102, "lunes", 16, 18, "Algoritmos Avanzados", "Garcia Lopez", "A-102"),
            new Horario(17, 2, 201, "martes", 16, 18, "Cálculo Vectorial", "Ana Rodriguez", "B-201"),
            new Horario(18, 4, 302, "miércoles", 16, 18, "Química Inorgánica", "Maria Santos", "C-302"),
            new Horario(19, 5, 401, "jueves", 16, 18, "Probabilidad", "Luis Herrera", "D-401"),
            new Horario(20, 7, 101, "viernes", 16, 18, "Sistemas Distribuidos", "Roberto Silva", "A-101")
        );
        
        // Agregar horarios según el tipo de algoritmo
        switch (tipoAlgoritmo) {
            case "BASICO":
                horarios.addAll(horariosBasicos);
                System.out.println("Algoritmo BÁSICO: " + horariosBasicos.size() + " horarios");
                break;
                
            case "AVANZADO":
                horarios.addAll(horariosBasicos);
                horarios.addAll(horariosAvanzados);
                System.out.println("Algoritmo AVANZADO: " + (horariosBasicos.size() + horariosAvanzados.size()) + " horarios");
                break;
                
            case "OPTIMIZADO":
                horarios.addAll(horariosBasicos);
                horarios.addAll(horariosAvanzados);
                horarios.addAll(horariosOptimizados);
                System.out.println("Algoritmo OPTIMIZADO: " + (horariosBasicos.size() + horariosAvanzados.size() + horariosOptimizados.size()) + " horarios");
                break;
                
            default:
                horarios.addAll(horariosBasicos);
                System.out.println("Algoritmo por defecto (BÁSICO): " + horariosBasicos.size() + " horarios");
                break;
        }
        
        System.out.println("Total de horarios generados: " + horarios.size());
        return horarios;
    }
}