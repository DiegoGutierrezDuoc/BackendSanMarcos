package com.veterinaria.sanmarcos;

import com.veterinaria.sanmarcos.entity.Cita;
import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.entity.Mascota;
import com.veterinaria.sanmarcos.repository.CitaRepository;
import com.veterinaria.sanmarcos.repository.MascotaRepository;
import com.veterinaria.sanmarcos.service.CitaService;
import com.veterinaria.sanmarcos.service.MascotaService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MascotaCitaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    @InjectMocks
    private CitaService citaService;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Locale("es"));
    }

    @Test
    void testGuardarYActualizarMascotaConDataFaker() {
        Cliente cliente = new Cliente(1L, faker.name().fullName(), faker.internet().emailAddress(), null, "123456", "cliente");
        Mascota mascota = new Mascota(1L, faker.dog().name(), "Perro", faker.dog().breed(), faker.number().numberBetween(1, 15), cliente);

        when(mascotaRepository.save(any(Mascota.class))).thenReturn(mascota);
        when(mascotaRepository.findById(1L)).thenReturn(Optional.of(mascota));

        Mascota guardada = mascotaService.guardar(mascota);
        assertNotNull(guardada);
        assertEquals(mascota.getNombre(), guardada.getNombre());

        mascota.setEdad(5);
        Mascota actualizada = mascotaService.actualizar(1L, mascota);
        assertEquals(5, actualizada.getEdad());
        verify(mascotaRepository, times(2)).save(any(Mascota.class));
    }

    @Test
    void testAgendarYActualizarCitaConDataFaker() {
        Mascota mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre(faker.cat().name());

        Cita cita = new Cita(1L, LocalDateTime.now().plusDays(2), "Control preventivo: " + faker.lorem().sentence(3), "PENDIENTE", mascota);
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        Cita agendada = citaService.guardar(cita);
        assertEquals("PENDIENTE", agendada.getEstado());

        cita.setEstado("ATENDIDO");
        Cita atendida = citaService.actualizar(1L, cita);
        assertEquals("ATENDIDO", atendida.getEstado());
    }
}