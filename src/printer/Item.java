/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package printer;

/**
 *
 * @author poloche
 */
class Item {
    private String cantidad="0";
    private String detalle="0";
    private String peso="0";
    private String total="0";

    public Item() {
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
