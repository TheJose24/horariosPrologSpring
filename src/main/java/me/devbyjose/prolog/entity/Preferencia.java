package me.devbyjose.prolog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.DiaSemana;
import me.devbyjose.prolog.enums.Turno;

@Entity
@Table(name = "preferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Preferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;
    
    @Column(nullable = false)
    private Integer nivelPreferencia; // 0-3: 0=imposible, 1=evitar, 2=aceptable, 3=preferido
}