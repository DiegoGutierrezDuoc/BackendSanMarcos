package com.veterinaria.sanmarcos.controller;

import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.entity.Mascota;
import com.veterinaria.sanmarcos.repository.ClienteRepository;
import com.veterinaria.sanmarcos.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/mascotas")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Mascotas", description = "Gestión de mascotas asociadas a clientes")
public class MascotaController {

    private final MascotaService mascotaService;
    private final ClienteRepository clienteRepository;

    @GetMapping
    @Operation(summary = "Listar todas las mascotas (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Mascota>>> listar() {
        List<EntityModel<Mascota>> mascotas = mascotaService.listarTodas().stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MascotaController.class).obtener(m.getId())).withSelfRel(),
                        linkTo(methodOn(MascotaController.class).listar()).withRel("mascotas")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una mascota por ID (con enlaces HATEOAS)")
    public ResponseEntity<EntityModel<Mascota>> obtener(@PathVariable Long id) {
        Mascota mascota = mascotaService.buscarPorId(id);
        EntityModel<Mascota> model = EntityModel.of(mascota,
                linkTo(methodOn(MascotaController.class).obtener(id)).withSelfRel(),
                linkTo(methodOn(MascotaController.class).listar()).withRel("todas-las-mascotas"));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar mascotas por ID de cliente (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Mascota>>> listarPorCliente(@PathVariable Long clienteId) {
        List<EntityModel<Mascota>> mascotas = mascotaService.listarPorCliente(clienteId).stream()
                .map(m -> EntityModel.of(m,
                        linkTo(methodOn(MascotaController.class).obtener(m.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(mascotas,
                linkTo(methodOn(MascotaController.class).listarPorCliente(clienteId)).withSelfRel()));
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva mascota")
    public ResponseEntity<Mascota> crear(@RequestBody Mascota mascota, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Cliente cliente = clienteRepository.findByCorreo(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        mascota.setCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(mascotaService.guardar(mascota));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una mascota")
    public ResponseEntity<Mascota> actualizar(@PathVariable Long id, @RequestBody Mascota mascota, Authentication authentication) {
        if (authentication != null && authentication.getName() != null && mascota.getCliente() == null) {
            clienteRepository.findByCorreo(authentication.getName()).ifPresent(mascota::setCliente);
        }
        return ResponseEntity.ok(mascotaService.actualizar(id, mascota));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una mascota")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}