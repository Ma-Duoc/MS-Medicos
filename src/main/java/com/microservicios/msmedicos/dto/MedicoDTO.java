package com.microservicios.msmedicos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicoDTO {
    
    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String email;
    private String telefono;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
