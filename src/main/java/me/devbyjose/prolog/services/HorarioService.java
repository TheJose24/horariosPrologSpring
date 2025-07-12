package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import me.devbyjose.prolog.model.Horario;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HorarioService {
    
    @Autowired
    private PrologEngineService prologEngine;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    public List<Horario> generarHorarios(String tipoAlgoritmo) {
        List<Horario> horarios = new ArrayList<>();
        
        try {
            // Llamar al predicado principal de Prolog
            Query query = prologEngine.crearQuery("main_generar_horario", new Term[]{
                new org.jpl7.Atom(tipoAlgoritmo),
                new org.jpl7.Variable("Resultado")
            });
            
            if (query.hasSolution()) {
                Map<String, Term> solution = query.oneSolution();
                Term resultado = solution.get("Resultado");
                
                // Procesar el resultado
                if (resultado.hasFunctor("exito", 1)) {
                    Term listaHorarios = resultado.arg(1);
                    horarios = procesarListaHorarios(listaHorarios);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al generar horarios: " + e.getMessage());
        }
        
        return horarios;
    }
    
    private List<Horario> procesarListaHorarios(Term listaHorarios) {
        List<Horario> horarios = new ArrayList<>();
        
        // Convertir la lista de términos Prolog a objetos Java
        Term[] elementos = listaHorarios.toTermArray();
        
        for (Term elemento : elementos) {
            if (elemento.hasFunctor("horario", 6)) {
                Horario horario = new Horario();
                horario.setCursoId(elemento.arg(1).intValue());
                horario.setDocenteId(elemento.arg(2).intValue());
                horario.setAulaNumero(elemento.arg(3).intValue());
                horario.setDia(elemento.arg(4).name());
                horario.setHoraInicio(elemento.arg(5).intValue());
                horario.setHoraFin(elemento.arg(6).intValue());
                
                // Enriquecer con información de otros servicios
                enrichHorarioInfo(horario);
                
                horarios.add(horario);
            }
        }
        
        return horarios;
    }
    
    private void enrichHorarioInfo(Horario horario) {
        try {
            // Resolver dependencias dinámicamente para evitar ciclos
            CursoService cursoService = applicationContext.getBean(CursoService.class);
            DocenteService docenteService = applicationContext.getBean(DocenteService.class);
            
            // Buscar información del curso
            Curso curso = cursoService.buscarCursoPorId(horario.getCursoId());
            if (curso != null) {
                horario.setNombreCurso(curso.getNombre());
            } else {
                horario.setNombreCurso("Curso " + horario.getCursoId());
            }
            
            // Buscar información del docente
            Docente docente = docenteService.buscarDocentePorId(horario.getDocenteId());
            if (docente != null) {
                horario.setNombreDocente(docente.getNombre());
            } else {
                horario.setNombreDocente("Docente " + horario.getDocenteId());
            }
            
            // Mapear aula a ubicación
            horario.setUbicacionAula(mapearAulaAUbicacion(horario.getAulaNumero()));
        } catch (Exception e) {
            // Si hay error, usar valores por defecto
            horario.setNombreCurso("Curso " + horario.getCursoId());
            horario.setNombreDocente("Docente " + horario.getDocenteId());
            horario.setUbicacionAula(mapearAulaAUbicacion(horario.getAulaNumero()));
        }
    }
    
    private String mapearAulaAUbicacion(int aulaNumero) {
        return switch (aulaNumero) {
            case 101 -> "A-101";
            case 102 -> "A-102";
            case 201 -> "B-201";
            case 202 -> "B-202";
            case 301 -> "C-301";
            case 302 -> "C-302";
            case 401 -> "D-401";
            case 402 -> "D-402";
            default -> "Aula " + aulaNumero;
        };
    }
}