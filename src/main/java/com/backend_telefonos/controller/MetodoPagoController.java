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

import com.backend_telefonos.entity.MetodoPago;
import com.backend_telefonos.services.MetodoPagoService;

@RestController
@RequestMapping("api/MetodoPago")
@CrossOrigin("*")
public class MetodoPagoController {

	@Autowired
	private MetodoPagoService metodoPagoService;
	
	@GetMapping("/mostrar")
	public List<MetodoPago> mostrar(){
		return metodoPagoService.mostrar();
	}
	
	@PostMapping("/guardar")
	public MetodoPago guardar(@RequestBody MetodoPago metodoPago) {
		return metodoPagoService.guardar(metodoPago);
	}
	
	@PutMapping("/actualizar")
	public ResponseEntity<?> actualizar(@RequestBody MetodoPago metodoPago) {
		try {
			Optional<MetodoPago> metodoPagoExistente = metodoPagoService.buscarPorId(metodoPago.getIdMetodoPago());
			
			if (metodoPagoExistente.isPresent()) {
				MetodoPago metodoPagoActualizado = metodoPagoService.actualizar(metodoPago);
				return ResponseEntity.ok(metodoPagoActualizado);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error al actualizar el método de pago: " + e.getMessage());
		}
	}
}