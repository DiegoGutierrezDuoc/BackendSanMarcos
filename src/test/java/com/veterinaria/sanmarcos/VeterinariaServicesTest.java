package com.veterinaria.sanmarcos;

import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.entity.EstadoPedido;
import com.veterinaria.sanmarcos.entity.Pedido;
import com.veterinaria.sanmarcos.entity.Producto;
import com.veterinaria.sanmarcos.repository.ClienteRepository;
import com.veterinaria.sanmarcos.repository.PedidoRepository;
import com.veterinaria.sanmarcos.repository.ProductoRepository;
import com.veterinaria.sanmarcos.service.ClienteService;
import com.veterinaria.sanmarcos.service.PedidoService;
import com.veterinaria.sanmarcos.service.ProductoService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeterinariaServicesTest {

    private Faker faker;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ProductoService productoService;

    @InjectMocks
    private ClienteService clienteService;

    @InjectMocks
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Locale("es"));
    }

    @Test
    @DisplayName("Debe listar productos generados con DataFaker")
    void testListarProductos() {
        Producto p1 = new Producto(1L, faker.commerce().productName(), 15990, "Canino", "/img/p1.png", faker.lorem().sentence(), 10);
        Producto p2 = new Producto(2L, faker.commerce().productName(), 24990, "Felino", "/img/p2.png", faker.lorem().sentence(), 5);

        when(productoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Producto> resultado = productoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe guardar un nuevo producto correctamente")
    void testGuardarProducto() {
        Producto nuevo = new Producto(null, "Alimento " + faker.dog().breed(), 29990, "Canino", "/img/p3.png", faker.lorem().sentence(), 15);
        Producto guardado = new Producto(1L, nuevo.getNombre(), nuevo.getPrecio(), nuevo.getEspecie(), nuevo.getImagen(), nuevo.getDescripcion(), nuevo.getStock());

        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        Producto resultado = productoService.guardar(nuevo);

        assertNotNull(resultado.getId());
        assertEquals(nuevo.getNombre(), resultado.getNombre());
        assertEquals(29990, resultado.getPrecio());
    }

    @Test
    @DisplayName("Debe registrar un cliente con datos aleatorios de DataFaker")
    void testGuardarCliente() {
        Cliente cliente = new Cliente(
                null,
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.dog().name(),
                faker.internet().password(6, 12),
                "cliente"
        );
        Cliente clienteGuardado = new Cliente(1L, cliente.getNombre(), cliente.getCorreo(), cliente.getMascota(), cliente.getPassword(), "cliente");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.guardar(cliente);

        assertNotNull(resultado.getId());
        assertEquals(cliente.getCorreo(), resultado.getCorreo());
        assertEquals("cliente", resultado.getRol());
    }

    @Test
    @DisplayName("Debe crear un pedido calculando el total y descontando stock")
    void testCrearPedido() {
        Cliente cliente = new Cliente(1L, faker.name().fullName(), faker.internet().emailAddress(), faker.cat().name(), "123456", "cliente");
        Producto producto = new Producto(1L, "Vacuna Óctuple", 18000, "Canino", "/img/vacuna.png", "Vacuna anual", 10);

        Pedido solicitud = new Pedido();
        solicitud.setCliente(cliente);
        solicitud.setProducto(producto);
        solicitud.setCantidad(2);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        Pedido resultado = pedidoService.crearPedido(solicitud);

        assertNotNull(resultado.getId());
        assertEquals(36000, resultado.getTotal());
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
    }
}