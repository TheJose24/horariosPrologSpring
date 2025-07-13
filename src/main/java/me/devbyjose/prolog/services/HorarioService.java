package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Horario;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class HorarioService {
    
    @Autowired
    private PrologEngineService prologEngine;
    
    @Autowired
    private EnriquecimientoService enriquecimientoService;
    
    @Autowired
    private PrologConfigService configService;
    
    public List<Horario> generarHorarios(String tipoAlgoritmo) {
        List<Horario> horarios = new ArrayList<>();
        
        try {
            System.out.println("=== INICIANDO GENERACIÓN DE HORARIOS ===");
            System.out.println("Tipo de algoritmo: " + tipoAlgoritmo);
            System.out.println("Prolog habilitado: " + configService.isPrologEnabled());
            System.out.println("Fallback habilitado: " + configService.isFallbackEnabled());
            
            // Intentar usar Prolog si está habilitado
            if (configService.isPrologEnabled()) {
                horarios = generarHorariosConProlog(tipoAlgoritmo);
            }
            
            // Si Prolog falla o no está habilitado, usar datos de prueba
            if (horarios.isEmpty() && configService.isFallbackEnabled()) {
                System.out.println("Usando datos de prueba...");
                horarios = generarHorariosDePrueba();
            }
            
        } catch (Exception e) {
            System.err.println("Error al generar horarios: " + e.getMessage());
            if (configService.isFallbackEnabled()) {
                System.out.println("Usando datos de prueba debido al error...");
                horarios = generarHorariosDePrueba();
            }
        }
        
        System.out.println("=== FIN GENERACIÓN DE HORARIOS ===");
        System.out.println("Total de horarios generados: " + horarios.size());
        
        return horarios;
    }
    
    private List<Horario> generarHorariosConProlog(String tipoAlgoritmo) {
        List<Horario> horarios = new ArrayList<>();
        
        // Verificar si Prolog está disponible
        if (!prologEngine.isPrologDisponible()) {
            System.out.println("Prolog no está disponible, saltando generación con Prolog");
            return horarios;
        }
        
        try {
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
            
        } catch (Exception e) {
            System.err.println("Error en Prolog: " + e.getMessage());
            e.printStackTrace();
        }
        
        return horarios;
    }
    
    private List<Horario> generarHorariosDePrueba() {
        List<Horario> horarios = new ArrayList<>();
        
        // Datos de prueba predefinidos
        List<Horario> horariosPrueba = Arrays.asList(
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
        
        horarios.addAll(horariosPrueba);
        System.out.println("Generados " + horarios.size() + " horarios de prueba");
        
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