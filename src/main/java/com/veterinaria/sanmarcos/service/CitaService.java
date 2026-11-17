package com.veterinaria.sanmarcos.service;

import com.veterinaria.sanmarcos.entity.Cita;
import com.veterinaria.sanmarcos.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public List<Cita> listarPorMascota(Long mascotaId) {
        return citaRepository.findByMascotaId(mascotaId);
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + id));
    }

    public Cita guardar(Cita cita) {
        return citaRepository.save(cita);
    }

    public Cita actualizar(Long id, Cita detalles) {
        Cita cita = buscarPorId(id);
        cita.setFechaHora(detalles.getFechaHora());
        cita.setMotivo(detalles.getMotivo());
        cita.setEstado(detalles.getEstado());
        if (detalles.getMascota() != null) {
            cita.setMascota(detalles.getMascota());
        }
        return citaRepository.save(cita);
    }

    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }
}