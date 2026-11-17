package com.veterinaria.sanmarcos.repository;

import com.veterinaria.sanmarcos.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByClienteId(Long clienteId);
}