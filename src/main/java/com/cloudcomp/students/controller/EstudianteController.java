package com.cloudcomp.students.controller;

import com.cloudcomp.students.model.Estudiante;
import com.cloudcomp.students.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Estudiantes", description = "APIs para la gestión de estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @Operation(
        summary = "Obtener lista de estudiantes",
        description = "Retorna una lista paginada de estudiantes. Puede filtrarse por email."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiantes encontrados"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    @GetMapping
    public Page<Estudiante> getEstudiantes(
            @Parameter(description = "Email para filtrar", example = "usuario@ejemplo.com")
            @RequestParam(required = false) String email,
            
            @Parameter(description = "Número de página", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        if (email != null && !email.trim().isEmpty()) {
            return estudianteService.findByEmail(email, pageable);
        }
        
        return estudianteService.findAll(pageable);
    }

    @Operation(
        summary = "Obtener estudiante por ID",
        description = "Retorna los datos completos de un estudiante específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Estudiante encontrado",
            content = @Content(schema = @Schema(implementation = Estudiante.class))
        ),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> getEstudiante(
            @Parameter(description = "ID único del estudiante", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        try {
            Estudiante estudiante = estudianteService.findById(id);
            return ResponseEntity.ok(estudiante);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Crear nuevo estudiante",
        description = "Crea un nuevo registro de estudiante en el sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante creado exitosamente",
            content = @Content(schema = @Schema(implementation = Estudiante.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Objeto Estudiante a crear",
                required = true,
                content = @Content(schema = @Schema(implementation = Estudiante.class))
            )
            @RequestBody Estudiante estudiante) {
        try {
            Estudiante nuevoEstudiante = estudianteService.save(estudiante);
            return ResponseEntity.ok(nuevoEstudiante);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Actualizar estudiante parcialmente",
        description = "Actualiza información específica de un estudiante usando PATCH"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = Estudiante.class))
        ),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Estudiante> actualizarEstudiante(
            @Parameter(description = "ID único del estudiante", required = true)
            @PathVariable UUID id,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Campos a actualizar en formato JSON",
                required = true,
                content = @Content(schema = @Schema(example = "{\"telefono\": \"+123456789\", \"pais\": \"España\"}"))
            )
            @RequestBody Map<String, Object> updates) {
        try {
            Estudiante estudianteActualizado = estudianteService.updatePartial(id, updates);
            return ResponseEntity.ok(estudianteActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Eliminar estudiante",
        description = "Elimina un estudiante del sistema usando su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estudiante eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiante(
            @Parameter(description = "ID único del estudiante", required = true)
            @PathVariable UUID id) {
        try {
            estudianteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}