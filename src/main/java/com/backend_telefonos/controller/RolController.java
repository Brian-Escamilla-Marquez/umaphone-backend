package com.backend_telefonos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend_telefonos.entity.Rol;
import com.backend_telefonos.services.RolService;

@RestController
@RequestMapping("api/Rol")
@CrossOrigin("*")
public class RolController {

	@Autowired
	private RolService rolService;
	
	@GetMapping("/mostrar")
	public List<Rol> mostrar(){
		return rolService.mostrar();
	}
	
	@PostMapping("/guardar")
	public Rol guardar(@RequestBody Rol rol) {
		return rolService.guardar(rol);
	}
	
	@PutMapping("/actualizar")
	public ResponseEntity<?> actualizar(@RequestBody Rol rol) {
		try {
			Optional<Rol> rolExistente = rolService.buscarPorId(rol.getId_rol());
			
			if (rolExistente.isPresent()) {
				Rol rolActualizado = rolService.actualizar(rol);
				return ResponseEntity.ok(rolActualizado);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al actualizar el rol: " + e.getMessage());
		}
	}
}