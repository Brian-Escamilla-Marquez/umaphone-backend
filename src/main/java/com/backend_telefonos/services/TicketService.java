package com.backend_telefonos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend_telefonos.entity.Ticket;
import com.backend_telefonos.repository.TicketRepository;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    public List<Ticket> mostrar(){
        return ticketRepository.findAll();
    }
    
    public Ticket guardar(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
    
    public Ticket actualizar(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
    
    public Optional<Ticket> buscarPorId(Long id) {
        return ticketRepository.findById(id);
    }
    
    public void eliminar(Long id) {
        ticketRepository.deleteById(id);
    }
}