package com.veterinaria.sanmarcos.controller;

import com.veterinaria.sanmarcos.dto.AuthResponse;
import com.veterinaria.sanmarcos.dto.LoginRequest;
import com.veterinaria.sanmarcos.entity.Cliente;
import com.veterinaria.sanmarcos.repository.ClienteRepository;
import com.veterinaria.sanmarcos.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login y registro de usuarios con generación de token JWT")
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return clienteRepository.findByCorreo(request.getCorreo())
                .filter(cliente -> cliente.getPassword().equals(request.getPassword()))
                .map(cliente -> {
                    String token = jwtUtil.generarToken(cliente.getCorreo(), cliente.getRol());
                    return ResponseEntity.ok(new AuthResponse(
                            token,
                            cliente.getCorreo(),
                            cliente.getNombre(),
                            cliente.getRol()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo cliente y obtener token JWT")
    public ResponseEntity<?> register(@RequestBody Cliente nuevoCliente) {
        if (clienteRepository.existsByCorreo(nuevoCliente.getCorreo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está registrado");
        }
        if (nuevoCliente.getRol() == null || nuevoCliente.getRol().isBlank()) {
            nuevoCliente.setRol("cliente");
        }
        Cliente guardado = clienteRepository.save(nuevoCliente);
        String token = jwtUtil.generarToken(guardado.getCorreo(), guardado.getRol());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(token, guardado.getCorreo(), guardado.getNombre(), guardado.getRol()));
    }
}