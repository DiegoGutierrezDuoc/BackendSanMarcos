package com.veterinaria.sanmarcos.service;

import com.veterinaria.sanmarcos.entity.Producto;
import com.veterinaria.sanmarcos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setPrecio(datos.getPrecio());
        existente.setEspecie(datos.getEspecie());
        existente.setImagen(datos.getImagen());
        existente.setDescripcion(datos.getDescripcion());
        existente.setStock(datos.getStock());
        return productoRepository.save(existente);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}