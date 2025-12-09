package com.backend_telefonos.controller;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend_telefonos.entity.Ticket;
import com.backend_telefonos.services.TicketService;

@RestController
@RequestMapping("api/Ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    
    // GET: Obtener todos los tickets
    @GetMapping("/mostrar")
    public List<Ticket> mostrar(){
        return ticketService.mostrar();
    }
    
    // GET: Buscar ticket por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketService.buscarPorId(id);
        if (ticket.isPresent()) {
            return ResponseEntity.ok(ticket.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Ticket no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    // GET: Buscar tickets por usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable Long idUsuario) {
        try {
            List<Ticket> tickets = ticketService.mostrar();
            List<Ticket> ticketsFiltrados = tickets.stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getIdUsuario() == idUsuario)
                .toList();
            
            return ResponseEntity.ok(ticketsFiltrados);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al buscar tickets: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/guardar")
    public ResponseEntity<?> guardar(@RequestBody Ticket ticket) {
        try {
            ticket.setIdTicket(null);
            
            if (ticket.getUsuario() == null || ticket.getUsuario().getIdUsuario() == null || ticket.getUsuario().getIdUsuario() == 0L) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "El usuario es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (ticket.getProducto() == null || ticket.getProducto().getIdproducto() == null || ticket.getProducto().getIdproducto() == 0L) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "El producto es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (ticket.getCantidad() <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "La cantidad debe ser mayor a 0");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (ticket.getTotal() == null || ticket.getTotal().doubleValue() <= 0) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "El total debe ser mayor a 0");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (ticket.getMetodoPago() == null || ticket.getMetodoPago().getIdMetodoPago() == null || ticket.getMetodoPago().getIdMetodoPago() == 0L) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "El método de pago es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (ticket.getFechaCompra() == null) {
                ticket.setFechaCompra(LocalDateTime.now());
            }
            
            Ticket ticketGuardado = ticketService.guardar(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticketGuardado);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al guardar ticket: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // DELETE: Eliminar ticket
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            Optional<Ticket> ticketExistente = ticketService.buscarPorId(id);
            
            if (ticketExistente.isPresent()) {
                ticketService.eliminar(id);
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Ticket eliminado correctamente");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Ticket no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al eliminar ticket: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}