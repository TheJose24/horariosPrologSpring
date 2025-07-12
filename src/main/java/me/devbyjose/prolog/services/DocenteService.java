package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Docente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocenteService {
    
    @Autowired
    private PrologService prologService;
    
    @Autowired
    private ArchivoService archivoService;
    
    private final List<Docente> listaDocentes = new ArrayList<>();
    private long idCounter = 1;

    public void registrarDocente(Docente docente) {
        if (docente.getId() == null) {
            docente.setId(idCounter++);
        }
        listaDocentes.add(docente);
        prologService.guardarDocenteEnProlog(docente);
    }

    public List<Docente> obtenerTodosDocentes() {
        List<Docente> todosDocentes = new ArrayList<>();
        
        // Agregar docentes registrados en Java
        todosDocentes.addAll(listaDocentes);
        
        // Agregar docentes desde Prolog
        List<Docente> docentesProlog = archivoService.leerDocentesDesdeArchivo();
        todosDocentes.addAll(docentesProlog);
        
        return todosDocentes;
    }
    
    public Docente buscarDocentePorId(long docenteId) {
        // Buscar en docentes de Java
        for (Docente docente : listaDocentes) {
            if (docente.getId().equals(docenteId)) {
                return docente;
            }
        }
        
        // Buscar en docentes de Prolog
        List<Docente> docentesProlog = archivoService.leerDocentesDesdeArchivo();
        for (Docente docente : docentesProlog) {
            if (docente.getId().equals(docenteId)) {
                return docente;
            }
        }
        
        return null;
    }
}