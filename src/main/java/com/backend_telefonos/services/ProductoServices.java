package com.backend_telefonos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_telefonos.entity.Producto;
import com.backend_telefonos.repository.ProductoRepository;

@Service
public class ProductoServices {

    @Autowired
    private ProductoRepository productoRepository;
    
    public List<Producto> mostrar() {
        List<Producto> productos = productoRepository.findAll();
        
        // Convertir imágenes a base64 para cada producto
        productos.forEach(producto -> {
            if (producto.getImagen() != null && producto.getImagenTipo() != null) {
                producto.getImagenBase64(); // Esto activa la conversión
            }
        });
        
        return productos;
    }
    
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public Producto actualizar(Producto producto) {
        return productoRepository.save(producto);
    }
    
    public Optional<Producto> buscarPorId(Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        
        // Convertir imagen a base64 si existe
        producto.ifPresent(p -> {
            if (p.getImagen() != null && p.getImagenTipo() != null) {
                p.getImagenBase64();
            }
        });
        
        return producto;
    }
    
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }
}