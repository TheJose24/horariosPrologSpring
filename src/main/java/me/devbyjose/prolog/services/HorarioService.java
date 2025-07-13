package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Horario;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HorarioService {
    
    @Autowired
    private PrologEngineService prologEngine;
    
    @Autowired
    private EnriquecimientoService enriquecimientoService;
    
    public List<Horario> generarHorarios(String tipoAlgoritmo) {
        List<Horario> horarios = new ArrayList<>();
        
        try {
            System.out.println("=== INICIANDO GENERACIÓN DE HORARIOS ===");
            System.out.println("Tipo de algoritmo: " + tipoAlgoritmo);
            
            // Llamar al predicado principal de Prolog
            Query query = prologEngine.crearQuery("main_generar_horario", new Term[]{
                new org.jpl7.Atom(tipoAlgoritmo),
                new org.jpl7.Variable("Resultado")
            });
            
            System.out.println("Query creada, verificando si tiene solución...");
            
            if (query.hasSolution()) {
                System.out.println("Query tiene solución, obteniendo resultado...");
                Map<String, Term> solution = query.oneSolution();
                Term resultado = solution.get("Resultado");
                
                System.out.println("Resultado obtenido: " + resultado);
                
                // Procesar el resultado
                if (resultado.hasFunctor("exito", 1)) {
                    System.out.println("Resultado es éxito, procesando lista de horarios...");
                    Term listaHorarios = resultado.arg(1);
                    horarios = procesarListaHorarios(listaHorarios);
                    System.out.println("Horarios procesados: " + horarios.size());
                } else {
                    System.out.println("Resultado no es éxito: " + resultado);
                }
            } else {
                System.out.println("Query no tiene solución");
            }
            
            System.out.println("=== FIN GENERACIÓN DE HORARIOS ===");
            System.out.println("Total de horarios generados: " + horarios.size());
            
        } catch (Exception e) {
            System.err.println("Error al generar horarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return horarios;
    }
    
    private List<Horario> procesarListaHorarios(Term listaHorarios) {
        List<Horario> horarios = new ArrayList<>();
        
        System.out.println("Procesando lista de horarios: " + listaHorarios);
        
        try {
            // Convertir la lista de términos Prolog a objetos Java
            Term[] elementos = listaHorarios.toTermArray();
            
            System.out.println("Número de elementos en la lista: " + elementos.length);
            
            for (int i = 0; i < elementos.length; i++) {
                Term elemento = elementos[i];
                System.out.println("Procesando elemento " + i + ": " + elemento);
                
                if (elemento.hasFunctor("horario", 6)) {
                    Horario horario = new Horario();
                    horario.setCursoId(elemento.arg(1).intValue());
                    horario.setDocenteId(elemento.arg(2).intValue());
                    horario.setAulaNumero(elemento.arg(3).intValue());
                    horario.setDia(elemento.arg(4).name());
                    horario.setHoraInicio(elemento.arg(5).intValue());
                    horario.setHoraFin(elemento.arg(6).intValue());
                    
                    System.out.println("Horario creado: " + horario);
                    
                    // Enriquecer con información usando el servicio dedicado
                    enriquecimientoService.enrichHorarioInfo(horario);
                    
                    System.out.println("Horario enriquecido: " + horario);
                    horarios.add(horario);
                } else {
                    System.out.println("Elemento no es un horario válido: " + elemento);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al procesar lista de horarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return horarios;
    }
}