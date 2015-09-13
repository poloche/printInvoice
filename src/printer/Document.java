/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printer;

/**
 *
 * @author familia
 */
public enum Document {

    FACTURA,
    FACTURA_ENCOMIENDA,
    GUIA,
    RECIBO_ENTREGA,
    RECIBO_POR_PAGAR,
    MANIFIESTO;

    public static Document get(String documentType) {
        if (documentType.equalsIgnoreCase("lista")) {
            return MANIFIESTO;
        }
        if (documentType.equalsIgnoreCase("guia")) {
            return GUIA;
        }
        if (documentType.equalsIgnoreCase("factura")) {
            return FACTURA;
        }
        if (documentType.equalsIgnoreCase("reciboEntregaPP")) {
            return RECIBO_POR_PAGAR;
        }
        if (documentType.equalsIgnoreCase("reciboEntrega")) {
            return RECIBO_ENTREGA;
        }
        if (documentType.equalsIgnoreCase("facturaEncomienda")) {
            return FACTURA_ENCOMIENDA;
        }
        return MANIFIESTO;
    }
}
