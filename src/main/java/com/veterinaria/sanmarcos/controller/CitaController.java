package com.veterinaria.sanmarcos.controller;

import com.veterinaria.sanmarcos.entity.Cita;
import com.veterinaria.sanmarcos.service.CitaService;
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
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas médicas veterinarias")
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    @Operation(summary = "Listar todas las citas médicas (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Cita>>> listar() {
        List<EntityModel<Cita>> citas = citaService.listarTodas().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CitaController.class).obtener(c.getId())).withSelfRel(),
                        linkTo(methodOn(CitaController.class).listar()).withRel("citas")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(citas,
                linkTo(methodOn(CitaController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una cita por ID (con enlaces HATEOAS)")
    public ResponseEntity<EntityModel<Cita>> obtener(@PathVariable Long id) {
        Cita cita = citaService.buscarPorId(id);
        EntityModel<Cita> model = EntityModel.of(cita,
                linkTo(methodOn(CitaController.class).obtener(id)).withSelfRel(),
                linkTo(methodOn(CitaController.class).listar()).withRel("todas-las-citas"));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/mascota/{mascotaId}")
    @Operation(summary = "Listar citas por ID de mascota (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Cita>>> listarPorMascota(@PathVariable Long mascotaId) {
        List<EntityModel<Cita>> citas = citaService.listarPorMascota(mascotaId).stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CitaController.class).obtener(c.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(citas,
                linkTo(methodOn(CitaController.class).listarPorMascota(mascotaId)).withSelfRel()));
    }

    @PostMapping
    @Operation(summary = "Agendar una nueva cita médica")
    public ResponseEntity<Cita> crear(@RequestBody Cita cita) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.guardar(cita));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una cita médica")
    public ResponseEntity<Cita> actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        return ResponseEntity.ok(citaService.actualizar(id, cita));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una cita médica")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}