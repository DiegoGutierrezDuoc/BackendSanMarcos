package com.veterinaria.sanmarcos.controller;

import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.service.ClienteService;
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
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes y usuarios")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos los clientes (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Cliente>>> listar() {
        List<EntityModel<Cliente>> clientes = clienteService.listarTodos().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClienteController.class).obtener(c.getId())).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).listar()).withRel("clientes")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Cliente>> collectionModel = CollectionModel.of(clientes,
                linkTo(methodOn(ClienteController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID (con enlaces HATEOAS)")
    public ResponseEntity<EntityModel<Cliente>> obtener(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        EntityModel<Cliente> model = EntityModel.of(cliente,
                linkTo(methodOn(ClienteController.class).obtener(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).listar()).withRel("todos-los-clientes"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente")
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.guardar(cliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un cliente")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}