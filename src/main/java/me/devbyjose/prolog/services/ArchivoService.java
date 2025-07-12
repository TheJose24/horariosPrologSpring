package me.devbyjose.prolog.services;

import me.devbyjose.prolog.model.Curso;
import me.devbyjose.prolog.model.Docente;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ArchivoService {
    
    private static final String PROLOG_FILE = "src/main/resources/prolog/base_conocimiento.pl";

    public void escribirEnArchivo(String contenido) {
        try (FileWriter writer = new FileWriter(PROLOG_FILE, true)) {
            writer.write(contenido);
        } catch (IOException e) {
            System.err.println("Error al escribir en archivo: " + e.getMessage());
        }
    }

    public List<Docente> leerDocentesDesdeArchivo() {
        List<Docente> docentes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROLOG_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().startsWith("docente(")) {
                    Docente docente = parsearDocente(linea);
                    if (docente != null) {
                        docentes.add(docente);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer docentes: " + e.getMessage());
        }
        return docentes;
    }

    public List<Curso> leerCursosDesdeArchivo() {
        List<Curso> cursos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROLOG_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().startsWith("curso(")) {
                    Curso curso = parsearCurso(linea);
                    if (curso != null) {
                        cursos.add(curso);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer cursos: " + e.getMessage());
        }
        return cursos;
    }
    
    private Docente parsearDocente(String linea) {
        try {
            Pattern pattern = Pattern.compile("docente\\((\\d+),\\s*'([^']+)',\\s*\\[([^\\]]+)\\]\\)");
            Matcher matcher = pattern.matcher(linea);
            
            if (matcher.find()) {
                Long id = Long.parseLong(matcher.group(1));
                String nombre = matcher.group(2);
                String especialidadesStr = matcher.group(3);
                
                List<String> especialidades = new ArrayList<>();
                if (especialidadesStr != null && !especialidadesStr.trim().isEmpty()) {
                    String[] especialidadesArray = especialidadesStr.split(",");
                    for (String especialidad : especialidadesArray) {
                        especialidades.add(especialidad.trim());
                    }
                }
                
                return new Docente(id, nombre, especialidades);
            }
        } catch (Exception e) {
            System.err.println("Error al parsear docente en línea '" + linea + "': " + e.getMessage());
        }
        return null;
    }
    
    private Curso parsearCurso(String linea) {
        try {
            Pattern pattern = Pattern.compile("curso\\((\\d+),\\s*'([^']+)',\\s*(\\d+),\\s*(\\w+),\\s*(\\w+),\\s*(\\d+),\\s*\\[([^\\]]+)\\]\\)");
            Matcher matcher = pattern.matcher(linea);
            
            if (matcher.find()) {
                String codigo = matcher.group(1);
                String nombre = matcher.group(2);
                int ciclo = Integer.parseInt(matcher.group(3));
                String tipoCurso = matcher.group(4);
                String tipoSesion = matcher.group(5);
                
                int dias = mapearTipoSesionADias(tipoSesion);
                String equipamiento = matcher.group(7);
                String tipoAula = mapearEquipamientoATipoAula(equipamiento);
                
                return new Curso(codigo, nombre, ciclo, tipoCurso, dias, tipoAula);
            }
        } catch (Exception e) {
            System.err.println("Error al parsear curso en línea '" + linea + "': " + e.getMessage());
        }
        return null;
    }
    
    private int mapearTipoSesionADias(String tipoSesion) {
        return switch (tipoSesion) {
            case "teorico" -> 1;
            case "practico" -> 2;
            case "teorico_practico" -> 3;
            default -> 1;
        };
    }
    
    private String mapearEquipamientoATipoAula(String equipamiento) {
        if (equipamiento.contains("computadoras")) {
            return "Lab. de Computacion";
        } else if (equipamiento.contains("laboratorio_quimica")) {
            return "Lab. de Ciencia";
        } else if (equipamiento.contains("laboratorio_fisica")) {
            return "Lab. de Ciencia";
        } else if (equipamiento.contains("proyector")) {
            return "Aula Tradicional";
        } else {
            return "Aula Tradicional";
        }
    }
}