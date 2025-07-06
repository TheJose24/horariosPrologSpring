package me.devbyjose.prolog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.devbyjose.prolog.enums.Turno;

import java.math.BigDecimal;
import java.time.Duration;

@Entity
@Table(name = "bloques_horarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloqueHorario {
    @Id
    private Long id;
    
    @Column(nullable = false)
    private BigDecimal horaInicio; // 8.0 = 8:00, 9.5 = 9:30
    
    @Column(nullable = false)
    private BigDecimal horaFin;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;
    
    public Duration getDuracion() {
        BigDecimal duracion = horaFin.subtract(horaInicio);
        long minutes = duracion.multiply(BigDecimal.valueOf(60)).longValue();
        return Duration.ofMinutes(minutes);
    }
}