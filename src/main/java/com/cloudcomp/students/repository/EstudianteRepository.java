package com.cloudcomp.students.repository;

import com.cloudcomp.students.model.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, UUID> {
    Page<Estudiante> findByEmailContainingIgnoreCase(String email, Pageable pageable);
    boolean existsByEmail(String email);
}