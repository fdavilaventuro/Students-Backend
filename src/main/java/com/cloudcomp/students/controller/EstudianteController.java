package com.cloudcomp.students.controller;

import com.cloudcomp.students.model.Estudiante;
import com.cloudcomp.students.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public Page<Estudiante> getEstudiantes(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (email != null && !email.trim().isEmpty()) {
            return estudianteService.findByEmail(email, pageable);
        }
        
        return estudianteService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> getEstudiante(@PathVariable UUID id) {
        try {
            Estudiante estudiante = estudianteService.findById(id);
            return ResponseEntity.ok(estudiante);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        try {
            Estudiante nuevoEstudiante = estudianteService.save(estudiante);
            return ResponseEntity.ok(nuevoEstudiante);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Estudiante> actualizarEstudiante(
            @PathVariable UUID id, 
            @RequestBody Map<String, Object> updates) {
        try {
            Estudiante estudianteActualizado = estudianteService.updatePartial(id, updates);
            return ResponseEntity.ok(estudianteActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable UUID id) {
        try {
            estudianteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}