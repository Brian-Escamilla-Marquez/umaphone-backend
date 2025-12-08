package com.backend_telefonos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_telefonos.entity.Rol;
import com.backend_telefonos.repository.RolRepository;

@Service
public class RolService {
	
	@Autowired
	private RolRepository rolRepository;
	
	public List<Rol> mostrar(){
		return rolRepository.findAll();
	}
	
	public Rol guardar(Rol rol) {
		return rolRepository.save(rol);
	}
	
	public Rol actualizar(Rol rol) {
		return rolRepository.save(rol);
	}
	
	public Optional<Rol> buscarPorId(Long id) {
		return rolRepository.findById(id);
	}
}