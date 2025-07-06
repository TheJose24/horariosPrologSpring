package me.devbyjose.prolog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.Equipamiento;
import me.devbyjose.prolog.enums.TipoAula;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aulas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
    @Id
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Integer capacidad;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "aula_equipamientos")
    private Set<Equipamiento> equipamientos = new HashSet<>();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAula tipoAula;
}