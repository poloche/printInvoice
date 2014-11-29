package printer;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author poloche
 */
public class Encomienda {

    private String remitente = "0";
    private String consignatario = "0";
    private String telefono = "0";
    private String guia = "";
    private String origen = "";
    private String destino = "";
    private String ciudadDestino = "";
    private String detalle = "";
    private String total = "0";
    private String receptor = "0";
    private String carnet = "0";
    private String valorDeclarado = "";
    private String observacion = "";
    private ArrayList<Item> items = new ArrayList<Item>();

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(String valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }

    public String getCiudadDestino() {
        return this.ciudadDestino;
    }

    public void setCiudadDestino(String ciudad) {
        this.ciudadDestino = ciudad;
    }

    public enum Tipo {

        F(" F A C T U R A "), SI(" S / I "), GIRO(" G I R O "), EN(" E N T R E G A "), PP("POR PAGAR"), NORMAL("NORMAL");
        private final String title;

        Tipo(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    };

    public String getData() {
        String resp = "remitente=" + remitente;
        resp += "&consignatario=" + consignatario;
        resp += "&guia=" + guia;
        resp += "&origen=" + origen;
        resp += "&destino=" + destino;
        resp += "&detalle=" + detalle;
        resp += "&total=" + total;
        resp += "&receptor=" + receptor;
        resp += "&carnet=" + carnet;
        return resp;
    }

    public Encomienda() {
        items = new ArrayList<Item>();
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }
    private Tipo tipo;
    private String title = " F A C T U R A ";

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    /**
     *
     * @return
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getConsignatario() {
        return consignatario;
    }

    public void setConsignatario(String consignatario) {
        this.consignatario = consignatario;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getGuia() {
        return guia;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.equalsIgnoreCase("normal")) {
            this.tipo = Tipo.F;
        } else if (title.equalsIgnoreCase("por pagar")) {
            this.tipo = Tipo.PP;
        } else if (title.equalsIgnoreCase("interno")) {
            this.tipo = Tipo.SI;
        } else if (title.equalsIgnoreCase("GIRO")) {
            this.tipo = Tipo.GIRO;
        } else {
            this.tipo = Tipo.EN;
        }
        this.title = title;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
