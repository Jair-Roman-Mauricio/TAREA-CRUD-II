package com.crud.tareacrud.repository;

import com.crud.tareacrud.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, UUID> {
    boolean existsByEmail(String email);
}
