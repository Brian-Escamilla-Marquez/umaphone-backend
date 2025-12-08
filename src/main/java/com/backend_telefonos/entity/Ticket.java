package com.backend_telefonos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = -5457323972426857269L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_ticket")
    private Long idTicket;
    
    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name="id_producto")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name="id_metodo_pago")
    private MetodoPago metodoPago;
    
    @Column(name="cantidad")
    private int cantidad;
    
    @Column(name="total")
    private BigDecimal total;
    
    @Column(name="fecha_compra")
    private LocalDateTime fechaCompra;
    
    public Ticket() {
        this.fechaCompra = LocalDateTime.now();
    }
    
    public Long getIdTicket() {
        return idTicket;
    }
    
    public void setIdTicket(Long idTicket) {
        if (idTicket != null && idTicket > 0) {
            this.idTicket = idTicket;
        }
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }
    
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}