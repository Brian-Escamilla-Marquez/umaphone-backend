package com.backend_telefonos.controller;

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

import com.backend_telefonos.entity.Usuario;
import com.backend_telefonos.services.UsuarioService;

@RestController
@RequestMapping("api/Usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // GET: Obtener todos los usuarios
    @GetMapping("/mostrar")
    public List<Usuario> mostrar(){
        return usuarioService.mostrar();
    }
    
    // GET: Buscar usuario por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // POST: Crear nuevo usuario
    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            // Validar campos requeridos
            if (usuario.getCorreoUsuario() == null || usuario.getCorreoUsuario().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "El correo es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (usuario.getContrasena() == null || usuario.getContrasena().isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "La contraseña es requerida");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Establecer rol por defecto (2 = cliente) si no se especifica
            if (usuario.getRol() == null || usuario.getRol().getId_rol() == 0) {
                com.backend_telefonos.entity.Rol rolCliente = new com.backend_telefonos.entity.Rol();
                rolCliente.setId_rol(2);
                usuario.setRol(rolCliente);
            }
            
            Usuario usuarioGuardado = usuarioService.guardar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al guardar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // PUT: Actualizar usuario
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorId(id);
            
            if (usuarioExistente.isPresent()) {
                Usuario usuarioActual = usuarioExistente.get();
                
                if (usuario.getNombreUsuario() != null) {
                    usuarioActual.setNombreUsuario(usuario.getNombreUsuario());
                }
                if (usuario.getApellidoUsuario() != null) {
                    usuarioActual.setApellidoUsuario(usuario.getApellidoUsuario());
                }
                if (usuario.getCorreoUsuario() != null) {
                    usuarioActual.setCorreoUsuario(usuario.getCorreoUsuario());
                }
                if (usuario.getContrasena() != null) {
                    usuarioActual.setContrasena(usuario.getContrasena());
                }
                if (usuario.getTelefono() != null) {
                    usuarioActual.setTelefono(usuario.getTelefono());
                }
                if (usuario.getDireccion() != null) {
                    usuarioActual.setDireccion(usuario.getDireccion());
                }
                if (usuario.getRol() != null) {
                    usuarioActual.setRol(usuario.getRol());
                }
                
                Usuario usuarioActualizado = usuarioService.actualizar(usuarioActual);
                return ResponseEntity.ok(usuarioActualizado);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al actualizar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // DELETE: Eliminar usuario
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            Optional<Usuario> usuarioExistente = usuarioService.buscarPorId(id);
            
            if (usuarioExistente.isPresent()) {
                usuarioService.eliminar(id);
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Usuario eliminado correctamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al eliminar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // GET: Login simple
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String correo, @RequestParam String contrasena) {
        try {
            List<Usuario> usuarios = usuarioService.mostrar();
            
            for (Usuario usuario : usuarios) {
                if (usuario.getCorreoUsuario().equals(correo) && 
                    usuario.getContrasena().equals(contrasena)) {
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("usuario", usuario);
                    response.put("rol", usuario.getRol());
                    return ResponseEntity.ok(response);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Error en el servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // GET: Buscar usuarios por rol
    @GetMapping("/por-rol/{idRol}")
    public ResponseEntity<?> buscarPorRol(@PathVariable Long idRol) {
        try {
            List<Usuario> usuarios = usuarioService.mostrar();
            List<Usuario> usuariosFiltrados = usuarios.stream()
                .filter(u -> u.getRol() != null && u.getRol().getId_rol() == idRol)
                .toList();
            
            return ResponseEntity.ok(usuariosFiltrados);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al buscar usuarios: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}