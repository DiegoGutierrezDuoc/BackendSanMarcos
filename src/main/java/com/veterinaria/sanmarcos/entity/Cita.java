package com.veterinaria.sanmarcos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;
}