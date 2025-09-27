package com.cloudcomp.students.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "estudiante_perfil")
public class EstudiantePerfil {
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Estudiante estudiante;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private String bio;

    @Column(columnDefinition = "JSONB")
    private String preferencias;

    // Constructores
    public EstudiantePerfil() {}

    public EstudiantePerfil(String avatarUrl, String bio, String preferencias) {
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.preferencias = preferencias;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getPreferencias() { return preferencias; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }
}