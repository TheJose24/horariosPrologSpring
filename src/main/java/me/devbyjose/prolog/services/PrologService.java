
package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PrologService {
    private static final String PROLOG_FILE = "src/main/resources/prolog/base_conocimiento.pl";

    public void guardarCursoEnProlog(Curso curso) {
        String hecho = String.format("curso('%s', '%s', %d, '%s', %d, '%s').%n",
                curso.getCodigo(), curso.getNombre(), curso.getCiclo(), curso.getTipoCurso(), curso.getDias(), curso.getTipoAula());
        escribirEnArchivo(hecho);
    }

    public void guardarDocenteEnProlog(Docente docente) {
        String especialidades = "[" + String.join(", ", docente.getEspecialidades().stream().map(e -> "'" + e + "'").toList()) + "]";
        String hecho = String.format("docente('%s', '%s', %s).%n",
                docente.getId(), docente.getNombre(), especialidades);
        escribirEnArchivo(hecho);
    }

    public List<Docente> obtenerDocentesDesdeProlog() {
        List<Docente> docentes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROLOG_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("docente(")) {
                    linea = linea.substring(8, linea.length() - 2);
                    String[] partes = linea.split(", \\[", 2);
                    String[] cabecera = partes[0].split("', '");
                    Long id = Long.parseLong(cabecera[0].replace("'", ""));
                    String nombre = cabecera[1].replace("'", "");
                    String[] especialidades = partes[1].replace("]", "").replace("'", "").split(",");
                    docentes.add(new Docente(id, nombre, Arrays.stream(especialidades).map(String::trim).toList()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docentes;
    }

    private void escribirEnArchivo(String linea) {
        try (FileWriter writer = new FileWriter(PROLOG_FILE, true)) {
            writer.write(linea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}