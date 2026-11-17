package com.veterinaria.sanmarcos.service;

import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.entity.EstadoPedido;
import com.veterinaria.sanmarcos.entity.Pedido;
import com.veterinaria.sanmarcos.entity.Producto;
import com.veterinaria.sanmarcos.repository.ClienteRepository;
import com.veterinaria.sanmarcos.repository.PedidoRepository;
import com.veterinaria.sanmarcos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + id));
    }

    public Pedido crearPedido(Pedido pedido) {
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        Producto producto = productoRepository.findById(pedido.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        pedido.setCliente(cliente);
        pedido.setProducto(producto);
        int cantidad = (pedido.getCantidad() != null && pedido.getCantidad() > 0) ? pedido.getCantidad() : 1;
        pedido.setCantidad(cantidad);
        pedido.setTotal(producto.getPrecio() * cantidad);
        if (pedido.getEstado() == null) {
            pedido.setEstado(EstadoPedido.PENDIENTE);
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public void eliminar(Long id) {
        pedidoRepository.deleteById(id);
    }
}