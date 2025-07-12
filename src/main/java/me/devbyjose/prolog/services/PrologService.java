package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrologService {
    
    @Autowired
    private PrologEngineService prologEngine;
    
    @Autowired
    private ArchivoService archivoService;
    
    @Autowired
    private PrologFormatterService formatterService;

    public void guardarCursoEnProlog(Curso curso) {
        // Guardar en archivo
        String hecho = formatterService.formatearCursoParaArchivo(curso);
        archivoService.escribirEnArchivo(hecho);
        
        // Agregar a la base de conocimientos en memoria
        prologEngine.agregarCursoAProlog(curso);
    }

    public void guardarDocenteEnProlog(Docente docente) {
        // Guardar en archivo
        String hecho = formatterService.formatearDocenteParaArchivo(docente);
        archivoService.escribirEnArchivo(hecho);
        
        // Agregar a la base de conocimientos en memoria
        prologEngine.agregarDocenteAProlog(docente);
    }
}