package printer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author poloche
 */
public class Factura {

    private boolean tipo = true;// si la copia es original o es copia n valida
    private String fecha = "";
    private String hora = "";
    private String nombre = "";
    private String nit = "";
    private String numeroFactura = "";
    private String autorizacion = "";
    private String codigoControl = "";
    private String fechaLimite = "";
    private String total = "";
    private String totalLiteral = "";

    public Factura() {
    }

    public String getData(){
        String resp = "tipo="+tipo;
        resp+="&fecha="+fecha;
        resp+="&hora="+hora;
        resp+="&nombre="+nombre;
        resp+="&nit="+nit;
        resp+="&numeroFactura="+numeroFactura;
        resp+="&autorizacion="+autorizacion;
        resp+="&codigoControl="+codigoControl;
        resp+="&fechaLimite="+fechaLimite;
        resp+="&total="+total;
        resp+="&literal="+totalLiteral;
        return resp;
    }
    
    public String getCodigoControl() {
        return codigoControl;
    }

    public void setCodigoControl(String codigoControl) {
        this.codigoControl = codigoControl;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isOriginal() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalLiteral() {
        return totalLiteral;
    }

    public void setTotalLiteral(String totalLiteral) {
        this.totalLiteral = totalLiteral;
    }
}
