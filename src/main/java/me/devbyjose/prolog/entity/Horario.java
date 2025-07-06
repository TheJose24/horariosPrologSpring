package me.devbyjose.prolog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.DiaSemana;
import me.devbyjose.prolog.enums.TipoSesion;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaSemana dia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloque_id", nullable = false)
    private BloqueHorario bloque;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private Aula aula;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSesion tipoSesion;
    
    @CreationTimestamp
    private LocalDateTime fechaCreacion;
}