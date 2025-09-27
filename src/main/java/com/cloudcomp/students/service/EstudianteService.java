package com.cloudcomp.students.service;

import com.cloudcomp.students.model.Estudiante;
import com.cloudcomp.students.model.EstudiantePerfil;
import com.cloudcomp.students.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.UUID;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public Page<Estudiante> findAll(Pageable pageable) {
        return estudianteRepository.findAll(pageable);
    }

    public Estudiante findById(UUID id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con id: " + id));
    }

    public Page<Estudiante> findByEmail(String email, Pageable pageable) {
        return estudianteRepository.findByEmailContainingIgnoreCase(email, pageable);
    }

    public Estudiante save(Estudiante estudiante) {
        if (estudianteRepository.existsByEmail(estudiante.getEmail())) {
            throw new RuntimeException("El email ya está registrado: " + estudiante.getEmail());
        }
        return estudianteRepository.save(estudiante);
    }

    public Estudiante updatePartial(UUID id, Map<String, Object> updates) {
        Estudiante estudiante = findById(id);
        
        updates.forEach((key, value) -> {
            switch (key) {
                case "nombres":
                    estudiante.setNombres((String) value);
                    break;
                case "apellidos":
                    estudiante.setApellidos((String) value);
                    break;
                case "email":
                    if (!estudiante.getEmail().equals(value) && estudianteRepository.existsByEmail((String) value)) {
                        throw new RuntimeException("El email ya está registrado: " + value);
                    }
                    estudiante.setEmail((String) value);
                    break;
                case "telefono":
                    estudiante.setTelefono((String) value);
                    break;
                case "pais":
                    estudiante.setPais((String) value);
                    break;
                case "perfil":
                    if (value instanceof Map) {
                        Map<String, Object> perfilMap = (Map<String, Object>) value;
                        EstudiantePerfil perfil = estudiante.getPerfil();
                        if (perfil == null) {
                            perfil = new EstudiantePerfil();
                            estudiante.setPerfil(perfil);
                        }
                        if (perfilMap.containsKey("avatarUrl")) {
                            perfil.setAvatarUrl((String) perfilMap.get("avatarUrl"));
                        }
                        if (perfilMap.containsKey("bio")) {
                            perfil.setBio((String) perfilMap.get("bio"));
                        }
                        if (perfilMap.containsKey("preferencias")) {
                            perfil.setPreferencias(perfilMap.get("preferencias").toString());
                        }
                    }
                    break;
            }
        });
        
        return estudianteRepository.save(estudiante);
    }

    public void delete(UUID id) {
        if (!estudianteRepository.existsById(id)) {
            throw new EntityNotFoundException("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}