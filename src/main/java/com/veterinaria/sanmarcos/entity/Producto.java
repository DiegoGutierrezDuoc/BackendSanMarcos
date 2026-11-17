package com.veterinaria.sanmarcos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false, length = 50)
    private String especie;

    @Column(columnDefinition = "TEXT")
    private String imagen;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer stock = 10;
}