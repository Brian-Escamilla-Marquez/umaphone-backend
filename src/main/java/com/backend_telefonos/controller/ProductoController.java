package com.backend_telefonos.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend_telefonos.entity.Producto;
import com.backend_telefonos.services.ProductoServices;

@RestController
@RequestMapping("api/Producto")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoServices productoServices;

    // ✅ ENDPOINT DE PRUEBA
    @PostMapping("/test-upload")
    public ResponseEntity<?> testUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("✅ TEST UPLOAD - Archivo recibido:");
        System.out.println("   📁 Nombre: " + file.getOriginalFilename());
        System.out.println("   📊 Tamaño: " + file.getSize() + " bytes");
        System.out.println("   📄 Tipo: " + file.getContentType());
        System.out.println("   🟢 ¿Está vacío?: " + file.isEmpty());
        System.out.println("══════════════════════════════════════════════════════");
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Archivo recibido correctamente",
            "filename", file.getOriginalFilename(),
            "size", file.getSize(),
            "contentType", file.getContentType(),
            "isEmpty", file.isEmpty()
        ));
    }

    // ✅ GET: Obtener todos los productos CON IMÁGENES BASE64
    @GetMapping("/mostrar") 
    public List<Producto> mostrar() {
        System.out.println("🔍 Mostrando productos...");
        return productoServices.mostrar();
    }
    
    // ✅ GET: Buscar producto por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        System.out.println("🔍 Buscando producto ID: " + id);
        Optional<Producto> producto = productoServices.buscarPorId(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Producto no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // ✅ GET: Buscar productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String nombre) {
        try {
            System.out.println("🔍 Buscando productos por nombre: " + nombre);
            List<Producto> productos = productoServices.mostrar();
            List<Producto> productosFiltrados = productos.stream()
                .filter(p -> p.getNombreproducto().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
            
            return ResponseEntity.ok(productosFiltrados);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error en la búsqueda: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // ✅ POST: Crear producto CON IMAGEN (FormData) - GUARDA BLOB
    @PostMapping("/guardar-con-imagen")
    public ResponseEntity<?> guardarConImagen(
            @RequestParam("nombreproducto") String nombreproducto,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam("preciounitario") BigDecimal preciounitario,
            @RequestParam("stock") int stock,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("🆕 CREANDO NUEVO PRODUCTO CON IMAGEN:");
        System.out.println("   📝 Nombre: " + nombreproducto);
        System.out.println("   📊 Precio: " + preciounitario);
        System.out.println("   📦 Stock: " + stock);
        System.out.println("   📷 ¿Tiene imagen?: " + (imagen != null));
        
        if (imagen != null) {
            System.out.println("   📁 Nombre imagen: " + imagen.getOriginalFilename());
            System.out.println("   📊 Tamaño imagen: " + imagen.getSize() + " bytes");
            System.out.println("   📄 Tipo imagen: " + imagen.getContentType());
            System.out.println("   🟢 ¿Está vacía?: " + imagen.isEmpty());
        }
        System.out.println("══════════════════════════════════════════════════════");
        
        try {
            // Validaciones
            if (nombreproducto == null || nombreproducto.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre del producto es requerido"));
            }
            
            if (preciounitario == null || preciounitario.doubleValue() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El precio debe ser mayor a 0"));
            }
            
            if (stock < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El stock no puede ser negativo"));
            }
            
            // Crear producto
            Producto producto = new Producto();
            producto.setNombreproducto(nombreproducto);
            producto.setDescripcion(descripcion);
            producto.setMarca(marca);
            producto.setModelo(modelo);
            producto.setPreciounitario(preciounitario);
            producto.setStock(stock);
            
            // ✅ GUARDAR IMAGEN COMO BLOB EN LA BASE DE DATOS
            if (imagen != null && !imagen.isEmpty()) {
                System.out.println("💾 Guardando imagen en BLOB...");
                byte[] imageBytes = imagen.getBytes();
                producto.setImagen(imageBytes);
                producto.setImagenTipo(imagen.getContentType());
                System.out.println("✅ Imagen guardada: " + imageBytes.length + " bytes");
            } else {
                System.out.println("⚠️ No se recibió imagen o está vacía");
            }
            
            Producto productoGuardado = productoServices.guardar(producto);
            System.out.println("✅ Producto creado ID: " + productoGuardado.getIdproducto());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
            
        } catch (IOException e) {
            System.err.println("❌ Error al procesar imagen: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al procesar imagen: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error al guardar producto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al guardar producto: " + e.getMessage()));
        }
    }
    
    // ✅ POST: Crear producto SIN IMAGEN (JSON - para compatibilidad)
    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Producto producto) {
        try {
            System.out.println("🆕 Creando producto sin imagen: " + producto.getNombreproducto());
            
            if (producto.getNombreproducto() == null || producto.getNombreproducto().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El nombre del producto es requerido"));
            }
            
            if (producto.getPreciounitario() == null || producto.getPreciounitario().doubleValue() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "El precio debe ser mayor a 0"));
            }
            
            Producto productoGuardado = productoServices.guardar(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
        } catch (Exception e) {
            System.err.println("❌ Error al guardar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al guardar producto: " + e.getMessage()));
        }
    }
    
    // ✅ PUT: Actualizar producto CON IMAGEN - ACTUALIZA BLOB
    @PutMapping("/actualizar-con-imagen/{id}")
    public ResponseEntity<?> actualizarConImagen(
            @PathVariable Long id,
            @RequestParam(value = "nombreproducto", required = false) String nombreproducto,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "preciounitario", required = false) BigDecimal preciounitario,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("🔄 ACTUALIZANDO PRODUCTO CON IMAGEN:");
        System.out.println("   🆔 ID: " + id);
        System.out.println("   📝 Nombre: " + nombreproducto);
        System.out.println("   📊 Precio: " + preciounitario);
        System.out.println("   📦 Stock: " + stock);
        System.out.println("   📷 ¿Tiene imagen?: " + (imagen != null));
        
        if (imagen != null) {
            System.out.println("   📁 Nombre imagen: " + imagen.getOriginalFilename());
            System.out.println("   📊 Tamaño imagen: " + imagen.getSize() + " bytes");
            System.out.println("   📄 Tipo imagen: " + imagen.getContentType());
            System.out.println("   🟢 ¿Está vacía?: " + imagen.isEmpty());
        }
        System.out.println("══════════════════════════════════════════════════════");
        
        try {
            Optional<Producto> productoExistente = productoServices.buscarPorId(id);
            
            if (!productoExistente.isPresent()) {
                System.err.println("❌ Producto no encontrado ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Producto no encontrado"));
            }
            
            Producto productoActual = productoExistente.get();
            System.out.println("🔍 Producto encontrado: " + productoActual.getNombreproducto());
            
            // Actualizar campos
            if (nombreproducto != null) productoActual.setNombreproducto(nombreproducto);
            if (descripcion != null) productoActual.setDescripcion(descripcion);
            if (marca != null) productoActual.setMarca(marca);
            if (modelo != null) productoActual.setModelo(modelo);
            if (preciounitario != null) productoActual.setPreciounitario(preciounitario);
            if (stock != null) productoActual.setStock(stock);
            
            // ✅ ACTUALIZAR IMAGEN COMO BLOB
            if (imagen != null && !imagen.isEmpty()) {
                System.out.println("💾 Guardando imagen en BLOB...");
                byte[] imageBytes = imagen.getBytes();
                productoActual.setImagen(imageBytes);
                productoActual.setImagenTipo(imagen.getContentType());
                productoActual.setImagenBase64(null); // Resetear cache de base64
                System.out.println("✅ Imagen guardada: " + imageBytes.length + " bytes");
            } else {
                System.out.println("⚠️ Imagen es null o está vacía - Manteniendo imagen existente");
            }
            
            Producto productoActualizado = productoServices.actualizar(productoActual);
            
            System.out.println("✅ Producto actualizado. ¿Tiene imagen?: " + 
                (productoActualizado.getImagen() != null));
            
            return ResponseEntity.ok(productoActualizado);
            
        } catch (IOException e) {
            System.err.println("❌ Error al procesar imagen: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al procesar imagen: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar producto: " + e.getMessage()));
        }
    }
    
    // ✅ PUT: Actualizar producto SIN IMAGEN (JSON)
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            System.out.println("🔄 Actualizando producto sin imagen ID: " + id);
            
            Optional<Producto> productoExistente = productoServices.buscarPorId(id);
            
            if (!productoExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Producto no encontrado"));
            }
            
            Producto productoActual = productoExistente.get();
            
            if (producto.getNombreproducto() != null) {
                productoActual.setNombreproducto(producto.getNombreproducto());
            }
            if (producto.getDescripcion() != null) {
                productoActual.setDescripcion(producto.getDescripcion());
            }
            if (producto.getMarca() != null) {
                productoActual.setMarca(producto.getMarca());
            }
            if (producto.getModelo() != null) {
                productoActual.setModelo(producto.getModelo());
            }
            if (producto.getPreciounitario() != null) {
                productoActual.setPreciounitario(producto.getPreciounitario());
            }
            if (producto.getStock() >= 0) {
                productoActual.setStock(producto.getStock());
            }
            
            Producto productoActualizado = productoServices.actualizar(productoActual);
            return ResponseEntity.ok(productoActualizado);
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar producto: " + e.getMessage()));
        }
    }
    
    // ✅ DELETE: Eliminar producto
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Eliminando producto ID: " + id);
            
            Optional<Producto> productoExistente = productoServices.buscarPorId(id);
            
            if (!productoExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Producto no encontrado"));
            }
            
            productoServices.eliminar(id);
            System.out.println("✅ Producto eliminado");
            return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
            
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar producto: " + e.getMessage()));
        }
    }
    
    // ✅ GET: Obtener solo la imagen de un producto
    @GetMapping("/imagen/{id}")
    public ResponseEntity<?> obtenerImagen(@PathVariable Long id) {
        try {
            System.out.println("🖼️ Obteniendo imagen del producto ID: " + id);
            
            Optional<Producto> producto = productoServices.buscarPorId(id);
            
            if (producto.isPresent() && producto.get().getImagen() != null) {
                Producto p = producto.get();
                
                Map<String, Object> response = new HashMap<>();
                response.put("id", p.getIdproducto());
                response.put("tipo", p.getImagenTipo());
                response.put("tamaño", p.getImagen().length);
                response.put("base64", Base64.getEncoder().encodeToString(p.getImagen()));
                response.put("urlBase64", p.getImagenBase64());
                
                System.out.println("✅ Imagen encontrada: " + p.getImagen().length + " bytes");
                return ResponseEntity.ok(response);
            }
            
            System.out.println("⚠️ Imagen no encontrada para el producto");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Imagen no encontrada para el producto"));
        } catch (Exception e) {
            System.err.println("❌ Error al obtener imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener imagen: " + e.getMessage()));
        }
    }
    
    // ✅ GET: Obtener productos sin imágenes (para listados rápidos)
    @GetMapping("/listado-rapido")
    public ResponseEntity<?> listadoRapido() {
        try {
            System.out.println("📋 Obteniendo listado rápido de productos");
            List<Producto> productos = productoServices.mostrar();
            
            List<Map<String, Object>> productosLigeros = productos.stream()
                .map(p -> {
                    Map<String, Object> productoMap = new HashMap<>();
                    productoMap.put("idproducto", p.getIdproducto());
                    productoMap.put("nombreproducto", p.getNombreproducto());
                    productoMap.put("marca", p.getMarca());
                    productoMap.put("modelo", p.getModelo());
                    productoMap.put("preciounitario", p.getPreciounitario());
                    productoMap.put("stock", p.getStock());
                    productoMap.put("tieneImagen", p.getImagen() != null);
                    productoMap.put("imagenTipo", p.getImagenTipo());
                    
                    return productoMap;
                })
                .toList();
            
            return ResponseEntity.ok(productosLigeros);
        } catch (Exception e) {
            System.err.println("❌ Error al obtener listado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener listado: " + e.getMessage()));
        }
    }
}