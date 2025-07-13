package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PrologEngineService {
    
    @Autowired
    private PrologFormatterService formatterService;
    
    private boolean prologInicializado = false;
    private boolean prologDisponible = true;
    
    public void inicializarProlog() {
        if (!prologInicializado && prologDisponible) {
            try {
                System.out.println("Intentando inicializar Prolog...");
                
                // Cargar el archivo principal de Prolog
                String prologPath = new File("src/main/resources/prolog/prolog.pl").getAbsolutePath();
                System.out.println("Ruta del archivo Prolog: " + prologPath);
                
                Query consultQuery = new Query("consult", new Term[]{new org.jpl7.Atom(prologPath)});
                
                if (consultQuery.hasSolution()) {
                    // Inicializar el sistema
                    Query initQuery = new Query("inicializar_sistema_completo");
                    initQuery.hasSolution();
                    prologInicializado = true;
                    System.out.println("Prolog inicializado correctamente");
                } else {
                    throw new RuntimeException("No se pudo cargar el archivo Prolog");
                }
            } catch (Exception e) {
                System.err.println("Error al inicializar Prolog: " + e.getMessage());
                System.out.println("Deshabilitando Prolog y usando modo de fallback");
                prologDisponible = false;
                prologInicializado = false;
            }
        }
    }
    
    public Query crearQuery(String predicado, Term[] parametros) {
        if (!prologDisponible) {
            throw new RuntimeException("Prolog no está disponible");
        }
        
        inicializarProlog();
        return new Query(predicado, parametros);
    }
    
    public boolean isPrologDisponible() {
        return prologDisponible;
    }
    
    public void agregarCursoAProlog(Curso curso) {
        if (!prologDisponible) {
            System.out.println("Prolog no disponible, saltando agregar curso");
            return;
        }
        
        inicializarProlog();
        
        try {
            // Crear el término para el curso
            Query query = new Query("assertz", new Term[]{
                new org.jpl7.Compound("curso", new Term[]{
                    new org.jpl7.Integer(Integer.parseInt(curso.getCodigo())),
                    new org.jpl7.Atom(curso.getNombre()),
                    new org.jpl7.Integer(curso.getCiclo()),
                    new org.jpl7.Atom(curso.getTipoCurso()),
                    new org.jpl7.Atom(formatterService.mapearTipoSesion(curso.getHoras())),
                    new org.jpl7.Integer(4),
                    crearListaEquipamiento(curso.getTipoAula())
                })
            });
            
            query.hasSolution();
        } catch (Exception e) {
            System.err.println("Error al agregar curso a Prolog: " + e.getMessage());
        }
    }
    
    public void agregarDocenteAProlog(Docente docente) {
        if (!prologDisponible) {
            System.out.println("Prolog no disponible, saltando agregar docente");
            return;
        }
        
        inicializarProlog();
        
        try {
            // Crear lista de especialidades
            Term[] especialidades = docente.getEspecialidades().stream()
                    .map(org.jpl7.Atom::new)
                    .toArray(Term[]::new);
            
            Query query = new Query("assertz", new Term[]{
                new org.jpl7.Compound("docente", new Term[]{
                    new org.jpl7.Integer(docente.getId().intValue()),
                    new org.jpl7.Atom(docente.getNombre()),
                    new org.jpl7.Compound(".", especialidades)
                })
            });
            
            query.hasSolution();
        } catch (Exception e) {
            System.err.println("Error al agregar docente a Prolog: " + e.getMessage());
        }
    }
    
    private Term crearListaEquipamiento(String tipoAula) {
        return switch (tipoAula) {
            case "Lab. de Computacion" -> new org.jpl7.Compound(".", new Term[]{
                new org.jpl7.Atom("computadoras"),
                new org.jpl7.Compound(".", new Term[]{
                    new org.jpl7.Atom("internet"),
                    new org.jpl7.Atom("[]")
                })
            });
            case "Aula Tradicional" -> new org.jpl7.Compound(".", new Term[]{
                new org.jpl7.Atom("proyector"),
                new org.jpl7.Compound(".", new Term[]{
                    new org.jpl7.Atom("pizarra"),
                    new org.jpl7.Atom("[]")
                })
            });
            default -> new org.jpl7.Atom("[]");
        };
    }
}