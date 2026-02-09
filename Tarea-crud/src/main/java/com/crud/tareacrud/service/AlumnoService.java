package com.crud.tareacrud.service;

import com.crud.tareacrud.entity.Alumno;
import com.crud.tareacrud.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Autowired
    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public Alumno crearAlumno(Alumno alumno) {

        if (alumno.getEdad() < 16) {
            throw new IllegalArgumentException("La edad mínima es 16 años");
        }

        if (alumnoRepository.existsByEmail(alumno.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        alumno.setActivo(true);
        alumno.setFechaRegistro(LocalDateTime.now());

        return alumnoRepository.save(alumno);
    }

    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> obtenerAlumnoPorId(UUID id) {
        return alumnoRepository.findById(id);
    }

    public Alumno actualizarAlumno(UUID id, Alumno alumnoActualizado) {
        Alumno alumnoExistente = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        if (!alumnoExistente.isActivo()) {

            if (alumnoActualizado.isActivo()) {
                if (alumnoExistente.getEdad() < 18) {
                    throw new IllegalArgumentException("No se puede reactivar un alumno menor de 18 años");
                }
                alumnoExistente.setActivo(true);
                return alumnoRepository.save(alumnoExistente);
            } else {

                throw new IllegalArgumentException("Un alumno inactivo solo puede reactivarse");
            }
        }

        alumnoExistente.setNombre(alumnoActualizado.getNombre());

        if (!alumnoExistente.getEmail().equals(alumnoActualizado.getEmail())) {
            if (alumnoRepository.existsByEmail(alumnoActualizado.getEmail())) {
                throw new IllegalArgumentException("El email ya está registrado");
            }
            alumnoExistente.setEmail(alumnoActualizado.getEmail());
        }

        alumnoExistente.setEdad(alumnoActualizado.getEdad());

        return alumnoRepository.save(alumnoExistente);
    }

    public void desactivarAlumno(UUID id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        alumno.setActivo(false);
        alumnoRepository.save(alumno);
    }
}
