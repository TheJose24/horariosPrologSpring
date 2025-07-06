package me.devbyjose.prolog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.Certificacion;
import me.devbyjose.prolog.enums.Profesion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "docentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "docente_profesiones")
    private Set<Profesion> profesiones = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "docente_certificaciones")
    private Set<Certificacion> certificaciones = new HashSet<>();
    
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL)
    private List<Asignacion> asignaciones = new ArrayList<>();
    
    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL)
    private List<Preferencia> preferencias = new ArrayList<>();
}