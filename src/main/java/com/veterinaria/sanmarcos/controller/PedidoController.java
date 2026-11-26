package com.veterinaria.sanmarcos.controller;

import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.entity.EstadoPedido;
import com.veterinaria.sanmarcos.entity.Pedido;
import com.veterinaria.sanmarcos.entity.Producto;
import com.veterinaria.sanmarcos.repository.ClienteRepository;
import com.veterinaria.sanmarcos.repository.ProductoRepository;
import com.veterinaria.sanmarcos.service.PedidoService;
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
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Gestión de órdenes y pedidos de clientes")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos (con enlaces HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<Pedido>>> listar() {
        List<EntityModel<Pedido>> pedidos = pedidoService.listarTodos().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PedidoController.class).obtener(p.getId())).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).obtener(p.getCliente().getId())).withRel("cliente"),
                        linkTo(methodOn(ProductoController.class).obtener(p.getProducto().getId())).withRel("producto"),
                        linkTo(methodOn(PedidoController.class).listar()).withRel("pedidos")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Pedido>> collectionModel = CollectionModel.of(pedidos,
                linkTo(methodOn(PedidoController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por ID (con enlaces HATEOAS)")
    public ResponseEntity<EntityModel<Pedido>> obtener(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        EntityModel<Pedido> model = EntityModel.of(pedido,
                linkTo(methodOn(PedidoController.class).obtener(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).obtener(pedido.getCliente().getId())).withRel("cliente"),
                linkTo(methodOn(ProductoController.class).obtener(pedido.getProducto().getId())).withRel("producto"),
                linkTo(methodOn(PedidoController.class).listar()).withRel("todos-los-pedidos"));
        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    public ResponseEntity<Pedido> crear(@RequestBody Pedido pedido, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }

        Cliente cliente = clienteRepository.findByCorreo(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Long productoId = (pedido.getProducto() != null) ? pedido.getProducto().getId() : null;
        if (productoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe indicar un producto válido");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        pedido.setCliente(cliente);
        pedido.setProducto(producto);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(pedido));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar el estado de un pedido")
    public ResponseEntity<Pedido> cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}