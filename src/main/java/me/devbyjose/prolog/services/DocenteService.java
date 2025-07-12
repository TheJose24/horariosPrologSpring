
package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.AsignacionHorario;
import me.devbyjose.prolog.model.Docente;
import org.springframework.stereotype.Service;
import me.devbyjose.prolog.model.Curso;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


@Service
public class DocenteService {
    private final List<Docente> listaDocentes = new ArrayList<>();
    private long idCounter = 1;

    public void registrarDocente(Docente docente) {
        if (docente.getId() == null) {
            docente.setId(idCounter++);
        }
        listaDocentes.add(docente);
    }

    public List<Docente> obtenerTodosDocentes() {
        return listaDocentes;
    }

    public Map<Curso, List<AsignacionHorario>> obtenerAsignacionesCursos() {
        Map<Curso, List<AsignacionHorario>> asignaciones = new HashMap<>();
        List<AsignacionHorario> lista1 = new ArrayList<>();
        List<AsignacionHorario> lista2 = new ArrayList<>();
        return asignaciones;
    }
}