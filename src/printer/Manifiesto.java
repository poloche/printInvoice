package printer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrador
 */
public class Manifiesto {
    public String chofer;
    public String fecha;
    public String hora;
    public String destino;
    public String nroBus;

    /**
     *
     * @param String chofer     nombre del chofer
     * @param String fecha      fecha de envio
     * @param String hora       hora de envio
     * @param String destino    ciudad de destino
     * @param String nroBus     numero del bus
     */
    public Manifiesto(String chofer, String fecha, String hora, String destino, String nroBus) {
        this.chofer = chofer;
        this.fecha = fecha;
        this.hora = hora;
        this.destino = destino;
        this.nroBus = nroBus;
    }
}
