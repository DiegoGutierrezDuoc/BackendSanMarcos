package com.veterinaria.sanmarcos.service;

import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    public Cliente guardar(Cliente cliente) {
        if (clienteRepository.existsByCorreo(cliente.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente datos) {
        Cliente existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setMascota(datos.getMascota());
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            existente.setPassword(datos.getPassword());
        }
        return clienteRepository.save(existente);
    }

    public void eliminar(Long id) {
        clienteRepository.deleteById(id);
    }
}