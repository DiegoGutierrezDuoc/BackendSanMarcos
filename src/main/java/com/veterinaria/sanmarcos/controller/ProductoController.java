package com.veterinaria.sanmarcos.controller;

import com.veterinaria.sanmarcos.entity.Producto;
import com.veterinaria.sanmarcos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestión del catálogo de productos veterinarios")
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> listar() {
        List<EntityModel<Producto>> productos = productoService.listarTodos().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(ProductoController.class).obtener(p.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).listar()).withRel("productos")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Producto>> collectionModel = CollectionModel.of(productos,
                linkTo(methodOn(ProductoController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID (con enlaces HATEOAS)")
    public ResponseEntity<EntityModel<Producto>> obtener(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id);
        EntityModel<Producto> model = EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtener(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listar()).withRel("todos-los-productos"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(producto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}