
package me.devbyjose.prolog.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Docente {
    private Long id;
    private String nombre;
    private List<String> especialidades;
}
