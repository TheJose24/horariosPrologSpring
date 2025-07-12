package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {
    
    @Autowired
    private PrologService prologService;
    
    @Autowired
    private ArchivoService archivoService;
    
    private final List<Curso> listaCursos = new ArrayList<>();

    public void registrarCurso(Curso curso) {
        listaCursos.add(curso);
        prologService.guardarCursoEnProlog(curso);
    }

    public List<Curso> obtenerTodosCursos() {
        List<Curso> todosCursos = new ArrayList<>();
        
        // Agregar cursos registrados en Java
        todosCursos.addAll(listaCursos);
        
        // Agregar cursos desde Prolog
        List<Curso> cursosProlog = archivoService.leerCursosDesdeArchivo();
        todosCursos.addAll(cursosProlog);
        
        return todosCursos;
    }
    
    public Curso buscarCursoPorId(int cursoId) {
        // Buscar en cursos de Java
        for (Curso curso : listaCursos) {
            if (curso.getCodigo().equals(String.valueOf(cursoId))) {
                return curso;
            }
        }
        
        // Buscar en cursos de Prolog
        List<Curso> cursosProlog = archivoService.leerCursosDesdeArchivo();
        for (Curso curso : cursosProlog) {
            if (curso.getCodigo().equals(String.valueOf(cursoId))) {
                return curso;
            }
        }
        
        return null;
    }
}