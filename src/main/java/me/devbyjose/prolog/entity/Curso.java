package me.devbyjose.prolog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Integer horasTotales;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCurso tipoCurso;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NaturalezaCurso naturalezaCurso;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "curso_profesiones_requeridas")
    private Set<Profesion> profesionesRequeridas = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "curso_certificaciones_deseables")
    private Set<Certificacion> certificacionesDeseables = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "curso_equipamiento_requerido")
    private Set<Equipamiento> equipamientoRequerido = new HashSet<>();
}