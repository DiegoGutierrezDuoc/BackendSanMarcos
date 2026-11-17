package com.veterinaria.sanmarcos.service;

import com.veterinaria.sanmarcos.entity.Mascota;
import com.veterinaria.sanmarcos.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    public List<Mascota> listarTodas() {
        return mascotaRepository.findAll();
    }

    public List<Mascota> listarPorCliente(Long clienteId) {
        return mascotaRepository.findByClienteId(clienteId);
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));
    }

    public Mascota guardar(Mascota mascota) {
        return mascotaRepository.save(mascota);
    }

    public Mascota actualizar(Long id, Mascota detalles) {
        Mascota mascota = buscarPorId(id);
        mascota.setNombre(detalles.getNombre());
        mascota.setEspecie(detalles.getEspecie());
        mascota.setRaza(detalles.getRaza());
        mascota.setEdad(detalles.getEdad());
        if (detalles.getCliente() != null) {
            mascota.setCliente(detalles.getCliente());
        }
        return mascotaRepository.save(mascota);
    }

    public void eliminar(Long id) {
        mascotaRepository.deleteById(id);
    }
}