package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Horario;
import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioService {
    @Autowired
    private ArchivoService archivoService;

    @Autowired
    private CursoService cursoService;
    @Autowired
    private DocenteService docenteService;

    public List<Horario> generarYObtenerHorarios() {
        // 1. Leer cursos y docentes
        List<Curso> cursos = cursoService.obtenerTodosCursos();
        List<Docente> docentes = docenteService.obtenerTodosDocentes();
        // 2. Generar hechos de horario (ejemplo simple: asignar primer docente a cada curso)
        List<String> hechosHorarios = new ArrayList<>();
        int aula = 101; // Puedes mejorar la lógica para asignar aulas
        int horaInicio = 8;
        int horaFin = 10;
        String dia = "lunes";
        for (int i = 0; i < cursos.size(); i++) {
            Curso curso = cursos.get(i);
            if (!docentes.isEmpty()) {
                Docente docente = docentes.get(i % docentes.size());
                String hecho = String.format("horario(%s, %d, %d, %s, %d, %d).%n",
                        curso.getCodigo(), docente.getId(), aula, dia, horaInicio, horaFin);
                hechosHorarios.add(hecho);
            }
        }
        // 3. Limpiar horarios anteriores y escribir los nuevos
        archivoService.limpiarHorariosEnArchivo();
        archivoService.escribirHorariosEnArchivo(hechosHorarios);
        // 4. Leer y devolver los horarios generados
        return archivoService.leerHorariosDesdeArchivo();
    }

    public List<Horario> obtenerTodosHorarios() {
        return archivoService.leerHorariosDesdeArchivo();
    }
}