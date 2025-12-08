package com.backend_telefonos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_telefonos.entity.MetodoPago;
import com.backend_telefonos.repository.MetodoPagoRepository;

@Service
public class MetodoPagoService {
	
	@Autowired
	private MetodoPagoRepository metodoPagoRepository;
	
	public List<MetodoPago> mostrar(){
		return metodoPagoRepository.findAll();
	}
	
	public MetodoPago guardar(MetodoPago metodoPago) {
		return metodoPagoRepository.save(metodoPago);
	}
	
	public MetodoPago actualizar(MetodoPago metodoPago) {
		return metodoPagoRepository.save(metodoPago);
	}
	
	public Optional<MetodoPago> buscarPorId(Long id) {
		return metodoPagoRepository.findById(id);
	}
}