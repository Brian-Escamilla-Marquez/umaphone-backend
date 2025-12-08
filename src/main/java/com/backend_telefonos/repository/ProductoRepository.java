package com.backend_telefonos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend_telefonos.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}