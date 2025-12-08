package com.backend_telefonos.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name="Producto")
public class Producto implements Serializable {

    private static final long serialVersionUID = 3358035149138780973L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_producto")
    private Long idproducto;

    @Column(name="nombre_producto")
    private String nombreproducto;

    @Column(name="descripcion")
    private String descripcion;

    @Column(name="marca")
    private String marca;

    @Column(name="modelo")
    private String modelo;

    @Column(name="precio_unitario")
    private BigDecimal preciounitario;

    @Column(name="stock")
    private int stock;

    // ✅ IMAGEN COMO BLOB EN LA BASE DE DATOS
    @Lob
    @Column(name="imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;

    @Column(name="imagen_tipo")
    private String imagenTipo;

    @Transient  // No se guarda en la base de datos
    private String imagenBase64;

    // ✅ GETTERS Y SETTERS
    public Long getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Long idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPreciounitario() {
        return preciounitario;
    }

    public void setPreciounitario(BigDecimal preciounitario) {
        this.preciounitario = preciounitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenTipo() {
        return imagenTipo;
    }

    public void setImagenTipo(String imagenTipo) {
        this.imagenTipo = imagenTipo;
    }

    // ✅ MÉTODO PARA OBTENER IMAGEN COMO BASE64
    public String getImagenBase64() {
        if (imagenBase64 == null && imagen != null && imagenTipo != null) {
            imagenBase64 = "data:" + imagenTipo + ";base64," + 
                          Base64.getEncoder().encodeToString(imagen);
        }
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }
}