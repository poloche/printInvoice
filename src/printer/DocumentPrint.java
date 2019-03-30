package printer;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class DocumentPrint extends JPanel implements Printable {

    private static final long serialVersionUID = 1L;
    private Manifiesto man = null;
    private Cabecera cab = null;
    private Encomienda e;
    private List<Encomienda> encomiendas;
    private Factura fac;
    private Document typeDocument = Document.FACTURA;
    private int heigth = 150;
    private static final int MAX_WIDTH = 200;

    private static final String lineSeparator = "-------------------------------------------------------------";
    
    public DocumentPrint(Document typeDocument) {
        this.typeDocument = typeDocument;
        encomiendas = new ArrayList<Encomienda>();
    }

    @Override
    public void paint(Graphics g) {

        switch (typeDocument) {
            case FACTURA:
                factura(g);
                break;
            case GUIA:
                guia(g);
                break;
            case RECIBO_ENTREGA:
            case RECIBO_POR_PAGAR:
                printReciboEntrega(g);
                break;
            default:
                lista(g);
                break;
        }
    }

    public int print(Graphics g, PageFormat pageFormat, int pagina)
            throws PrinterException {
        Graphics2D g2 = (Graphics2D) g;
        if (pagina == 0) {
            try {
                switch (typeDocument) {
                    case FACTURA:
                        factura(g);
                        break;
                    case GUIA:
                        guia(g);
                        break;
                    case RECIBO_ENTREGA:
                    case RECIBO_POR_PAGAR:
                        printReciboEntrega(g);
                        break;
                    default:
                        lista(g);
                        break;
                }

            } catch (Exception ne) {
                ne.printStackTrace();
            }
        }
        int fontHeight = g2.getFontMetrics().getHeight();

        double alturaPag = pageFormat.getImageableHeight() - fontHeight;
        double anchoPag = pageFormat.getImageableWidth();
//        double AnchoPanel = (double) this.getWidth();
        double alturaPanel = (double) heigth;
//        double escala = 1;
//        //El panel no cabria en la hoja, asi que necesitamos reescalarlo:
//        if (AnchoPanel >= anchoPag) {
//            escala = anchoPag / AnchoPanel;
//        }
//
//        g2.scale(escala, escala);
        int NumPages = (int) (alturaPanel / alturaPag);

        g2.translate(0f, -pagina * alturaPag);
        if (pagina != NumPages) {
            g2.setClip(0, (int) (pagina * alturaPag), (int) anchoPag, (int) alturaPag);
        } else {
            int restoPanel = (int) (alturaPanel - pagina * alturaPag);
            g2.setClip(0, (int) (pagina * alturaPag), (int) anchoPag, restoPanel);
        }
        if (pagina > NumPages) {
            return NO_SUCH_PAGE;
        } else {
            this.paint(g2);
            return PAGE_EXISTS;
        }
    }

    public void guia(Graphics g) {
        Font flinea = new Font("Courier New", Font.PLAIN, 9);
        Font fdatosfactura = new Font("Verdana", Font.PLAIN, 8);
        Font fdetalle = new Font("Verdana", Font.PLAIN, 10);

        int x = 12;
        int xValue = 82;
        int y = 2;

        printCabecera(g, y, x, fdatosfactura);
        y = 60;
        y = y + 15;
        g.setFont(new Font("Verdana", Font.BOLD, 14));

        String aux = " GUIA DE ENCOMIENDAS ";
        g.drawString(aux, getX(aux, g.getFont(), g), y);// 49
        y = y + 15;
        aux = e.getTipo().getTitle().trim().equalsIgnoreCase("F A C T U R A") ? "NORMAL" : e.getTipo().getTitle();

        g.drawString(aux, getX(aux, g.getFont(), g), y);// 49
        y = y + 9;
        if (e.getTipo().equals(Encomienda.Tipo.SI)) {
            g.drawString("CONTROL INTERNO", getX("CONTROL INTERNO", g.getFont(), g), y);// 49
            y = y + 9;
        }

        g.setFont(fdatosfactura);

        y = y + 5;
        g.setFont(flinea);
        g.drawString(lineSeparator, x, y);

        y = y + 10;

        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        String fecha = "";
        if (fac != null) {
            fecha = fac.getFecha();
        } else {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            fecha = formato.format(new Date());
        }

        g.setFont(new Font("Dialog", Font.BOLD, 12));
        y = drawLine(g, "FECHA :", fecha, x, x + 55, y, 12);
        y = drawLine(g, "Origen :", e.getOrigen().toUpperCase(), x, 65, y, 13);
        y = drawLine(g, "Destino :", e.getDestino().toUpperCase(), x, 70, y, 13);

        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        y = drawLine(g, "Remitente :", e.getRemitente(), x, xValue, y, 12);
        y = drawLine(g, "Destinatario :", e.getConsignatario(), x, xValue, y, 12);
        y = drawLine(g, "Telefono  :", e.getTelefono(), x, xValue, y, 12);
        g.setFont(new Font("Dialog", Font.BOLD, 12));
        y = drawLine(g, "GUIA :", e.getGuia(), x, xValue, y, 14);
        if (fac != null && fac.getNumeroFactura() != null && !fac.getNumeroFactura().isEmpty()) {
            y = drawLine(g, "Segun Factura", " : " + fac.getNumeroFactura(), x, xValue + 25, y, 14);
        }

        int inicio = y - 8;
        g.setColor(Color.black);
        //  DIBUJAMOS EL CUADRADO DEL TEXTO DETALLE
        g.drawRect(x, y - 9, x + 187, 11);
        g.setColor(Color.black);
        g.setFont(new Font("Dialog", Font.BOLD, 10));
        String text = "DETALLE";
        g.drawString(text, getX(text, g.getFont(), g), y);
        y = y + 10;
        g.drawString("cant", getXTam("Cant", g.getFont(), g, x + 1, 20), y);
        g.drawString("Detalle", getXTam("detalle", g.getFont(), g, 20, 110), y);
        g.drawString("Peso", getXTam("Peso", g.getFont(), g, 110, 125), y);
        g.drawString("Mont", getXTam("Mont", g.getFont(), g, 125, 145), y);

        g.drawLine(x, y + 1, x + 197, y + 1);
        int xDetalle = 30;
        int xPrecio = 165;
        int xTotal = 125;
        y = y + 9;
        g.setFont(new Font("Courier New", Font.BOLD, 7));
        boolean par = false;
        //      PINTAMOS LOS ITEMS
        for (Item item : e.getItems()) {

            String cant = item.getCantidad();//xx[0].substring(0, 2);
            String detalle = item.getDetalle() == null ? "" : item.getDetalle();
            String precio = item.getTotal() == null ? "" : item.getTotal();
            String peso = item.getPeso() == null ? "" : item.getPeso();
            Integer nn = new Integer(cant);

            g.drawString(nn + ".", x + 4, y);

            y = drawBetweenXtoX1(detalle, g.getFont(), g, x + 22, 140, y);

            g.drawString(peso, xPrecio, y);
            BigDecimal total = new BigDecimal(cant).multiply(new BigDecimal(precio));
            g.drawString(precio, xPrecio + 35, y);
            y = y + 10;

            par = !par;
        }
        int fin = y - 8;
        // DIBUJAMOS EL valor declarado
        if (!e.getTipo().equals(Encomienda.Tipo.SI)) {
            y = drawBetweenXtoX1(e.getObservacion(), flinea, g, x + 5, 200, y);
        }

        // DIBUJAMOS EL RECUADRO DE LOS ITEMS
        g.setColor(Color.black);

        g.drawLine(x, inicio, x, y);
        g.drawLine(x + 20, inicio, x + 20, fin);
        g.drawLine(x + 142, inicio + 11, x + 142, fin);
        g.drawLine(x + 172, inicio + 11, x + 172, fin);
        g.drawLine(x + 199, inicio, x + 199, y);
        g.drawLine(x, fin, x + 199, fin);
        g.drawLine(x, y, x + 199, y);
        //  HASTA AQUI SE DIBUJA EL RECUADRO

        g.setFont(new Font("Dialog", Font.BOLD, 10));
        y = y + 8;
        g.drawString("Total ", xPrecio - 10, y);
        g.drawString(e.getTotal(), xPrecio + 35, y);

        g.setFont(flinea);
        y = y + 10;
        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        g.drawString("Usuario :", x, y);
        g.setFont(fdetalle.deriveFont(Font.BOLD));
        g.drawString(cab.usuario, x + 50, y);

        y = y + 50;
        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        g.drawString(lineSeparator, x, y);
        g.setFont(fdetalle.deriveFont(Font.BOLD));
        y = y + 8;
        if (!e.getTipo().equals(Encomienda.Tipo.SI)) {
            y = drawLine(g, "", "FIRMA", x, getX("FIRMA", g.getFont(), g), y, 10);
            y = drawLine(g, "", e.getRemitente(), x, getX(e.getRemitente(), g.getFont(), g), y, 12);
        }
        g.setFont(flinea);
        g.drawString(lineSeparator, x, y);

        y = y + 7;
        drawQuantum(fdatosfactura, g, y);
        y = y + 2;
        heigth = y;
    }

    private int drawItems(int x, int y, Graphics g) {
        int inicio = y - 8;
        g.drawRect(x, y - 9, x + 187, 11);
        g.setColor(Color.black);
        g.setFont(new Font("Dialog", Font.BOLD, 10));
        String text = "DETALLE";
        g.drawString(text, getX(text, g.getFont(), g), y);
        y = y + 10;
        g.drawString("cant", getXTam("Cant", g.getFont(), g, x + 1, 20), y);
        g.drawString("Detalle", getXTam("detalle", g.getFont(), g, 20, 110), y);
        g.drawString("Peso", getXTam("Peso", g.getFont(), g, 110, 125), y);
        g.drawString("Mont", getXTam("Mont", g.getFont(), g, 125, 145), y);

        g.drawLine(x, y + 1, x + 197, y + 1);
        int xDetalle = 30;
        int xPrecio = 165;
        int xTotal = 125;
        y = y + 9;
        g.setFont(new Font("Courier New", Font.BOLD, 7));
        boolean par = false;
        //      PINTAMOS LOS ITEMS
        for (Item item : e.getItems()) {

            String cant = item.getCantidad();//xx[0].substring(0, 2);
            String detalle = item.getDetalle() == null ? "" : item.getDetalle();
            String precio = item.getTotal() == null ? "" : item.getTotal();
            String peso = item.getPeso() == null ? "" : item.getPeso();
            Integer nn = new Integer(cant);

            g.drawString(nn + ".", x + 4, y);

            y = drawBetweenXtoX1(detalle, g.getFont(), g, x + 22, 140, y);

            g.drawString(peso, xPrecio, y);
            BigDecimal total = new BigDecimal(cant).multiply(new BigDecimal(precio));
            g.drawString(precio, xPrecio + 35, y);
            y = y + 10;

            par = !par;
        }
        int fin = y - 8;
        // DIBUJAMOS EL RECUADRO DE LOS ITEMS
        g.setColor(Color.black);

        g.drawLine(x, inicio, x, y);
        g.drawLine(x + 20, inicio, x + 20, fin);
        g.drawLine(x + 142, inicio + 11, x + 142, fin);
        g.drawLine(x + 172, inicio + 11, x + 172, fin);
        g.drawLine(x + 199, inicio, x + 199, y);
        g.drawLine(x, fin, x + 199, fin);
        g.drawLine(x, y, x + 199, y);
        //  HASTA AQUI SE DIBUJA EL RECUADRO
        return y;
    }

    private void drawQuantum(Font fdatosfactura, Graphics g, int y) {
        g.setFont(fdatosfactura.deriveFont(Font.BOLD).deriveFont(7f));
        String aux = "Mobius IT Solutions";
        g.drawString(aux, getX(aux, g.getFont(), g), y);

    }

    /**
     * This method print string label whit value in size xl to xv and y to
     * avance
     *
     * @param g graphics to print
     * @param label label to be printed
     * @param value value to label to be printed follow to label
     * @param xl initial X position to print Label
     * @param xv initial X position to print Value
     * @param y position in Y axis to print
     * @param avance size for avance in Y axis
     * @return int the final position for y axis.
     */
    private int drawLine(Graphics g, String label, String value, int xl, int xv, int y, int avance) {
        return drawLine(g, label, value, xl, xv, y, avance, 210);
    }

    private int drawLine(Graphics g, String label, String value, int xl, int xv, int y, int avance, int xsize) {
        g.drawString(label, xl, y);

        int tam = g.getFontMetrics().stringWidth(value);
        if ((xv + tam) > xsize) {
            int lastSpace = xv;
            String temp = "";

            StringTokenizer tokenizer = new StringTokenizer(value);
            List<String> vector = new ArrayList<String>();
            while (tokenizer.hasMoreTokens()) {
                vector.add(tokenizer.nextToken());
            }

            for (String string : vector) {
                temp += string;
                int tam1 = g.getFontMetrics().stringWidth(temp);
                if ((xv + tam1) > 199) {
                    g.drawString(temp, xv, y);
                    y = y + 7;
                    tam1 = xv;
                    temp = "";
                }
                g.drawString(temp, xv, y);
                temp += " ";
            }

        } else {
            g.drawString(value, xv, y);
        }
        y = y + avance;
        return y;
    }

    public void factura(Graphics g) {
        Font flinea = new Font("Courier New", Font.PLAIN, 9);
        Font fnombre = new Font("Courier New", Font.PLAIN, 10);
        Font fdatosfactura = new Font("Verdana", Font.PLAIN, 8);
        Font fdetalle = new Font("Verdana", Font.PLAIN, 10);
        Font fdetalle2 = new Font("Dialog", Font.BOLD, 12);

        int x = 12;
        int y = 2;

        printCabecera(g, y, x, fdatosfactura);
        String aux = "";
        y = 60;
        y = y + 15;
        g.setFont(new Font("Verdana", Font.BOLD, 14));
        if (null == fac) {
            aux = " R E C I B O " + e.getTipo().getTitle();
            g.drawString(aux, getX(aux, g.getFont(), g), y);// 49
            y = y + 19;
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = formato.format(new Date());
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            y = drawLine(g, "Fecha :", fecha, x, x + 70, y, 0);
        } else {
            aux = "F A C T U R A";
            g.drawString(aux, getX(aux, g.getFont(), g), y);// 49
            y = y + g.getFontMetrics().getHeight();
            g.setFont(fdatosfactura);
            if (fac.isOriginal()) {
                aux = "O R I G I N A L";
                g.drawString(aux, getX(aux, g.getFont(), g), y);
            }// 49
            else {
                aux = "C O P I A   N O   V A L I D A";
                g.drawString(aux, getX(aux, g.getFont(), g), y);
                y = y + 9;
                aux = "P A R A   C R E D I T O   F I S C A L";
                g.drawString(aux, getX(aux, g.getFont(), g), y);
            }
            //TODO: debemos dibujar con multilinea y centrado
            g.setFont(fdetalle.deriveFont(Font.BOLD).deriveFont(6f));
            if (cab.getLeyendaActividad() != null && !cab.getLeyendaActividad().equals("")) {
                y = y + 9;
                y = drawBetweenXtoX1Tokenized(cab.getLeyendaActividad(), g, x + 10, 200, y, 9);
            }

            if (cab.getTipoFactura() != null && !cab.getTipoFactura().equals("")) {
                aux = "SERVICIO DE ENCOMIENDAS";
                if (cab.getTipoFactura().equalsIgnoreCase("Pasajes")) {
                    aux = "VENTA DE PASAJES";
                }
                g.drawString(aux, getX(aux, g.getFont(), g), y);
            }

            y = y + 5;
            g.setFont(flinea);
            g.drawString(lineSeparator, x, y);

            y = y + 10;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            g.drawString("NIT: ", x + 20, y);
            g.drawString("#Factura: ", x + 100, y);
            g.setFont(fdatosfactura);
            g.drawString(cab.nitEmpresa, x + 45, y);
            g.setFont(new Font("Courier New", Font.BOLD, 10));
            g.drawString(fac.getNumeroFactura(), x + 147, y);

            y = y + 10;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            y = drawLine(g, "#Autorización: ", fac.getAutorizacion(), x, x + 100, y, 8);
            g.setFont(flinea);
            g.drawString(lineSeparator, x, y);

            y = y + 10;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            y = drawLine(g, cab.getMunicipio(), fac.getFecha(), x, x + 70, y, 10);
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));

            g.setFont(fnombre);
            y = drawLine(g, "Señor(es): ", "", x, x + 70, y, 0);
            y = drawBetweenXtoX1(fac.getNombre(), g.getFont(), g, x + 70, 210, y, 13);
            y = drawLine(g, "NIT/CI: ", fac.getNit(), x, x + 50, y, 8);

            y = y + 5;
            g.setFont(flinea);
            g.drawString(lineSeparator, x, y);
        }
        g.setFont(fdatosfactura.deriveFont(Font.BOLD));

        y = y + 10;

        y = drawLine(g, "Remitente :", e.getRemitente(), x, x + 70, y, 12);
        y = drawLine(g, "Destinatario :", e.getConsignatario(), x, x + 70, y, 12);
        y = drawLine(g, "Telefono :", e.getTelefono(), x, x + 70, y, 12);
        y = y - 7;
        g.drawString(lineSeparator, x, y);
        g.setFont(fdetalle2.deriveFont(Font.BOLD, 13));
        y = y + 12;
        g.drawString("GUIA : " + e.getGuia(), getXTam("GUIA : " + e.getGuia(), g.getFont(), g, x, 200), y);
        y = y + 12;
//        y = drawLine(g, "GUIA :", e.getGuia(), x, x + 60, y, 12);
        g.setFont(fdetalle2.deriveFont(Font.BOLD, 11));
        y = drawLine(g, "Origen :", e.getOrigen().toUpperCase(), x, x + 60, y, 12);
        y = drawLine(g, "Destino :", e.getDestino().toUpperCase(), x, x + 60, y, 12);
        String ciudadDestino = e.getCiudadDestino() == null ? "" : e.getCiudadDestino().toUpperCase();
        y = drawLine(g, "", ciudadDestino, x, x + 60, y, 12);
        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        if (e.getTipo() == Encomienda.Tipo.EN) {
            y = y + 12;
            g.drawString("Entregado A ", x, y);
            g.drawString(e.getReceptor(), x + 70, y);
            y = y + 12;
            g.drawString("Documento ", x, y);
            g.drawString(e.getCarnet(), x + 70, y);

        }
        y = y + 12;
        // Aqui
        int inicio = y - 9;
        g.setColor(Color.black);
        //  DIBUJAMOS EL CUADRADO DEL TEXTO DETALLE
        g.drawRect(x, y - 9, x + 187, 11);
        g.setColor(Color.black);
        g.setFont(new Font("Dialog", Font.BOLD, 10));
        String text = "DETALLE";
        g.drawString(text, getX(text, g.getFont(), g), y);
        g.setFont(new Font("Dialog", Font.BOLD, 8));
        y = y + 10;
        g.drawString("Cant", getXTam("Cant", g.getFont(), g, x + 1, 20), y);
        g.drawString("Detalle", getXTam("detalle", g.getFont(), g, 20, 100), y);
        g.drawString("Peso", getXTam("Peso", g.getFont(), g, 100, 125), y);
        g.drawString("Monto", getXTam("Monto", g.getFont(), g, 125, 145), y);
        // g.drawString("C.I.", x + 160, y);
//        y = y + 10;
//        g.drawRect(x, y + 1, x + 187, y);
        g.drawLine(x, y + 1, x + 197, y + 1);
        int xDetalle = 30;
        int xPrecio = 165;
        int xTotal = 125;
        y = y + 9;
        g.setFont(new Font("Courier New", Font.BOLD, 7));
        boolean par = false;
        //      PINTAMOS LOS ITEMS
        for (Item item : e.getItems()) {

            String cant = item.getCantidad();//xx[0].substring(0, 2);
            String detalle = item.getDetalle() == null ? "" : item.getDetalle();
            String precio = item.getTotal() == null ? "" : item.getTotal();
            String peso = item.getPeso() == null ? "" : item.getPeso();
            Integer nn = new Integer(cant);

            g.drawString(nn + ".", x + 4, y);

            y = drawBetweenXtoX1(detalle, flinea, g, x + 23, 160, y);

            g.drawString(peso, xPrecio, y);
            BigDecimal total = new BigDecimal(cant).multiply(new BigDecimal(precio));
            g.drawString(precio, xPrecio + 20, y);
            g.drawLine(x, y + 1, x + 197, y + 1);
            y = y + 10;

            par = !par;
        }
        int fin = y - 9;
        // DIBUJAMOS EL valor declarado
// DIBUJAMOS EL valor declarado
        if (!e.getTipo().equals(Encomienda.Tipo.SI)) {
            y = drawBetweenXtoX1(e.getObservacion(), flinea, g, x + 5, 200, y);
        }

        int detalleFin = y;

        // DIBUJAMOS EL RECUADRO DE LOS ITEMS
        g.setColor(Color.black);

        g.drawLine(x, inicio, x, detalleFin);
        g.drawLine(x + 22, inicio, x + 22, fin);
        g.drawLine(x + 135, inicio + 11, x + 135, fin);
        g.drawLine(x + 172, inicio + 11, x + 172, fin);
        g.drawLine(x + 199, inicio, x + 199, detalleFin);
        g.drawLine(x, detalleFin, x + 199, detalleFin);
        //  HASTA AQUI SE DIBUJA EL RECUADRO

        g.setFont(flinea);

//        g.drawRect(x, y - 8, 199, 40);
        if (null != fac) {
            y = y + 15;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            g.drawString("Total General:", x + 4, y);
            g.setFont(new Font("Courier", Font.BOLD, 18));
            g.drawString(" Bs" + getDecimalFormat(Double.parseDouble(fac.getTotal())), x + 69, y);
            y = y + 10;
            g.setFont(fdetalle.deriveFont(Font.BOLD).deriveFont(5f));
            g.drawString("Son: " + fac.getTotalLiteral().toUpperCase(), x + 4, y);

            g.setFont(flinea);

            y = y + 10;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            g.drawString("Código de control:", x, y);
            g.setFont(fdetalle.deriveFont(Font.BOLD));
            g.drawString(fac.getCodigoControl(), x + 100, y);
            y = y + 10;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD));
            g.drawString("Fecha limite emisión:", x, y);
            g.setFont(fdetalle.deriveFont(Font.BOLD));
            g.drawString(fac.getFechaLimite(), x + 100, y);
        } else {
            y = y + 8;
            g.drawString("Total ", xPrecio - 10, y);
            g.drawString(e.getTotal(), xPrecio + 35, y);
        }
        y = y + 10;
        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        g.drawString("Usuario :", x, y);
        g.setFont(fdetalle.deriveFont(Font.BOLD));
        g.drawString(cab.usuario, x + 100, y);
        if (null != fac) {
            y = y + 8;
            g.setFont(flinea);
            g.drawString(lineSeparator, x, y);
            /**
             ****************************************************
             * ************* DIBUJAMOS EL CODIGO QR *************
             */

            try {
                BufferedImage bmp = null;
                try {
                    ByteArrayOutputStream out = QRCode.from(getQRText()).
                            withErrorCorrection(ErrorCorrectionLevel.H).
                            withCharset("UTF-8").
                            withSize(70, 70).
                            to(ImageType.PNG).
                            stream();

                    bmp = ImageIO.read(new ByteArrayInputStream(out.toByteArray()));
                    g.drawImage(bmp, getX(bmp), y, this);
                } catch (IOException ex) {
                    Logger.getLogger(DocumentPrint.class.getName()).log(Level.SEVERE, null, ex);
                }
                y = y + 75;
            } catch (Exception ex) {
                Logger.getLogger(DocumentPrint.class.getName()).log(Level.SEVERE, null, ex);
            }
            g.setFont(flinea);
            g.drawString(lineSeparator, x, y);

            String cad1 = "--\"ESTA FACTURA CONTRIBUYE AL DESARROLLO-";
            cad1 += " -----DEL PAIS. EL USO ILICITO DE ESTA SERA--- ";
            cad1 += " --------SANCIONADO DE ACUERDO A LEY\"    ";
            String cad2 = cab.getLeyendaSucursal();

            y = y + 7;
            g.setFont(fdatosfactura.deriveFont(Font.BOLD).deriveFont(new Float(7)));
            y = drawBetweenXtoX1Tokenized(cad1, g, x + 5, 220, y, 10);
//        
            g.setFont(fdatosfactura.deriveFont(Font.BOLD).deriveFont(new Float(5)));
            y = drawBetweenXtoX1Tokenized(cad2, g, x, 220, y, 7);
        }

        y = y + 8;
        g.setFont(flinea);
        g.drawString(lineSeparator, x, y);

        y = y + 7;
        drawQuantum(fdatosfactura, g, y);
        y = y + 2;
        heigth = y;
    }

    private int getX(String string, Font font, Graphics g) {
        int xx = (int) g.getFontMetrics(font).getStringBounds(string, null).getWidth();
        return (220 - xx) / 2;
    }

    private int getXTam(String string, Font font, Graphics g, int x, int x2) {
        int xx = (int) g.getFontMetrics(font).getStringBounds(string, null).getWidth();
        int valor = ((x2 - xx) / 2) + x;
        return ((x2 - xx) / 2) + x;
    }

    private boolean canDrawInXtoX1(String string, Font font, Graphics g, int x, int x2) {
        int xx = (int) g.getFontMetrics(font).getStringBounds(string, null).getWidth();
        int size = x2 - x;
        return xx < size;
    }

    private int drawBetweenXtoX1(String string, Font font, Graphics g, int x, int x2, int y) {
        return drawBetweenXtoX1(string, font, g, x, x2, y, 9);
    }

    /**
     *
     * @param stringToPrint text to print
     * @param font font used to print this text
     * @param g g graphics that is used to draw content
     * @param xStart x position to start to draw text
     * @param xFin x position until will be draw the text
     * @param yLine y position were we'll daw the text
     * @param avanceY height that will advance in y axis
     * @return Integer last y position of draw
     */
    private int drawBetweenXtoX1(String stringToPrint, Font font, Graphics g, int xStart, int xFin, int yLine, int avanceY) {
        char[] chars = stringToPrint.toCharArray();
        String helper = "";
        for (char ch : chars) {
            helper += ch;
            if (!canDrawInXtoX1(helper, font, g, xStart, xFin)) {
                g.drawString(helper, xStart, yLine);
                yLine = yLine + avanceY;
                helper = "";
            }
        }
        if (!helper.equalsIgnoreCase("")) {
            g.drawString(helper, xStart, yLine);
            yLine += avanceY;
        }
        return yLine;
    }

    private int getX(BufferedImage image) {
        int xx = image.getWidth(null);
        return (220 - xx) / 2;
    }

    private int drawBetweenXtoX1Tokenized(String stringToPrint, Graphics g, int xStart, int xFin, int yLine, int avanceY) {
        String arrayHelper[];
        String helper = "";
        if (stringToPrint.contains(" ")) {
            arrayHelper = stringToPrint.split(" ");
            for (String string : arrayHelper) {
                if (!canDrawInXtoX1(helper + string, g.getFont(), g, xStart, xFin)) {
                    helper = helper.replaceAll("-", " ");
                    g.drawString(helper, getX(helper, g.getFont(), g), yLine);
                    yLine = yLine + avanceY;
                    helper = "";
                }
                helper += " " + string;
            }
            if (!helper.equalsIgnoreCase("")) {
                helper = helper.replaceAll("-", " ");
                g.drawString(helper, xStart, yLine);
                yLine += avanceY;
            }
        }
        return yLine;
    }

    public void lista(Graphics g) {
        Font timeromas14 = new Font("Times New Roman", Font.BOLD, 15);
        Font currier12 = new Font("Courier New", Font.BOLD, 13);
        Font currier8 = new Font("Courier New", Font.BOLD, 9);
        Font courier14 = new Font("Courier New", Font.BOLD, 15);
        Font dialog8 = new Font("Dialog", Font.PLAIN, 9);
        Font dialog9 = new Font("Dialog", Font.PLAIN, 10);
        Font dialog11 = new Font("Dialog", Font.PLAIN, 12);
        Font verdana6 = new Font("Verdana", Font.BOLD, 6);

        int y = 7;
        int x = 12;

        y = y + 7;
        g.setFont(currier12);
        String titleM = "Manifiesto de " + cab.direccion;
        g.drawString(titleM, getXTam(titleM, g.getFont(), g, x, 200), y);
        y = y + 9;
        g.drawString(cab.titleEmpresa, getXTam(cab.titleEmpresa, g.getFont(), g, x, 200), y);
        y = y + 10;
        g.drawString(cab.nombreEmpresa, getXTam(cab.nombreEmpresa, g.getFont(), g, x, 200), y);
        y = y + 10;
        g.setFont(dialog8);
        y = drawLine(g, "Chofer:", man.chofer, x, x + 30, y, 10);
        g.setFont(dialog11);
        String placa = "Bus:" + man.nroBus;
        g.drawString(placa, x + 142, y);
        y = y + 10;
        String dest = man.destino;
        g.drawString("Destino: " + dest, x, y);
        g.setFont(dialog8);
        y = y + 10;
        y = drawLine(g, "Enviado por: ", cab.usuario, x, x + 50, y, 10);

        String fecha2 = man.fecha;
        String hora2 = man.hora;
        g.drawString("Fecha: " + fecha2, x, y);
        g.drawString("Hora : " + hora2, x + 142, y);
        y = y + 12;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", new Locale("es", "BO"));
        String fechaImp = sdf.format(new Date());

        g.drawString("Fecha Imp: " + fechaImp, x + 40, y);
        y = y + 12;
        int inicio = y - 9;
        int fin = y;
        int totalManifiesto = 0;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.lightGray);
        g2d.fill(new Rectangle2D.Double(x, y - 9, x + 187, 11));
        g2d.setPaint(Color.BLACK);
        g.setFont(dialog8);
        g.drawString("Guia", x + 11, y);
        g.drawString("Detalle", x + 61, y);
        g.drawString("Imp.", x + 180, y);
        y = y + 9;
        if (encomiendas.size() > 0) {
            Collections.sort(encomiendas, new Comparator<Encomienda>() {
                public int compare(Encomienda t, Encomienda t1) {
                    return t.getTipo().getTitle().compareTo(t1.getTipo().getTitle());
                }
            });
            g.setFont(currier8);
            int j = 1;
            String title = "";
            for (Encomienda enc : encomiendas) {
                if (!enc.getTitle().equalsIgnoreCase(title)) {
                    g.drawString(enc.getTitle(), getX(enc.getTitle(), g.getFont(), g), y);
                    g.drawLine(x, y + 1, x + 200, y + 1);
                    y = y + 10;
                    title = enc.getTitle();
                }

//                float[] dashPattern = {5, 5, 5, 5};
//                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
//                        BasicStroke.JOIN_MITER, 1,
//                        dashPattern, 0));
                g.drawLine(x + 15, y - 5, x + 15, y + 3);

                y = y + 4;
                g.drawLine(x, y, x + 200, y);
                int yAux = y;
                y = drawBetweenXtoX1(enc.getGuia(), g.getFont(), g, x + 50, x + 180, y);
                //g.drawString(enc.getGuia(), x + 11, y);// 147
                g.setFont(dialog9);
                int sizeTotalE = g.getFontMetrics().stringWidth(enc.getTotal());
                g.drawString(enc.getTotal(), (x + 199) - sizeTotalE, y);
                g.setFont(dialog8);
//                g2d.draw(new Line2D.Float(x, y + 1, 199 + x, y + 1));
//                g2d.setStroke(new BasicStroke(1));
//                y = y + 9;
                String detalleEnc = enc.getDetalle().toUpperCase().trim();
                if (enc.getTitle().equalsIgnoreCase("NORMAL") || enc.getTitle().equalsIgnoreCase("GIRO")) {
                    totalManifiesto += Integer.parseInt(enc.getTotal());
                }
                y = drawBetweenXtoX1(detalleEnc, g.getFont(), g, x + 10, 170, y, 10);
                g.drawLine(x + 180, yAux, x + 180, y - 7);

                g2d.draw(new Line2D.Float(x, y - 6, 199 + x, y - 6));
                j++;
            }

        }
        fin = y - 6;

        g.drawLine(x, inicio, x, fin + 13);
//        g.drawLine(x + 10, inicio, x + 10, fin);
//        g.drawLine(x + 50, inicio, x + 50, fin);
//        g.drawLine(x + 169, inicio, x + 169, fin + 12);
        g.drawLine(x + 199, inicio, x + 199, fin + 12);
        g.drawLine(x, fin, x + 199, fin);
//        // Richard: Informacion de Mobius IT Solutions
        int size = fin + 2;
        g.setFont(courier14);
        String total = "TOTAL           ";
        g.drawString(total, x, size + 10);
        FontMetrics metrics = g.getFontMetrics();
        int sizeTotal = metrics.stringWidth(totalManifiesto + "");
        g.drawString(totalManifiesto + "", (x + 199) - sizeTotal, size + 10);

        g.drawLine(x, fin + 13, x + 199, fin + 13);
        size = size + 70;
        g.setFont(currier8);
        String aux = man == null ? "" : man.chofer;
        g.drawString(aux, getX(aux, g.getFont(), g), size + 7);
        size = size + 10;
        aux = "RECIBI CONFORME :: ";
        g.drawString(aux, getX(aux, g.getFont(), g), size + 7);
        size = size + 10;
        g.setFont(verdana6);
//        g.fillRect(11, size, 201, 1);
        g.drawString("MOBIUS IT SOLUTIONS", 63, size + 10);
        heigth = size;
    }

    private void printCabecera(Graphics g, int y, int x, Font fdatosfactura) {
        g.setFont(new Font("Verdana", Font.BOLD, 12));
        //ImageIcon icon = I18n.getImage("logoFactura");
        //g.drawImage(icon.getImage(), getX(icon) - 8, y, 165, 23, null);

        //y = y + icon.getIconHeight() + 10;        
        y = y + 10;
        g.setFont(new Font("Courier New", Font.BOLD, 12));
        g.drawString(cab.titleEmpresa, getX(cab.titleEmpresa, g.getFont(), g), y);
        y = y + 10;
        g.drawString(cab.nombreEmpresa, getX(cab.nombreEmpresa, g.getFont(), g), y);
        y = y + 10;
        g.setFont(fdatosfactura);

        String aux = "";
        if (cab.numeroSucursal.equals("0")) {
            aux += "Casa Matriz  ";
        } else {
            aux += "Sucursal: " + cab.numeroSucursal + "  ";
        }

        String textoCiudad = cab.getMunicipio() + " - " + cab.getCiudad2();
        if (cab.isCiudadCapital()) {
            textoCiudad = cab.ciudad + " - Bolivia";
        }

        if (cab.telefono != null && !cab.telefono.trim().equals("")) {
            aux += "Teléfono: " + cab.telefono + "  ";
        }
        aux += cab.impresor;
        g.drawString(aux, getX(aux, fdatosfactura, g), y);
        y = y + 10;
        g.drawString(cab.direccion, getX(cab.direccion,
                fdatosfactura, g), y);

        if (cab.direccion2 != null && !cab.direccion2.trim().equals("")) {
            y = y + 10;
            g.drawString(cab.direccion2, getX(cab.direccion2,
                    fdatosfactura, g), y);
        }

        y = y + 10;
        g.drawString(textoCiudad, getX(textoCiudad, fdatosfactura, g), y);

    }

    private void printReciboEntrega(Graphics g) {
        Font flinea = new Font("Courier New", Font.PLAIN, 9);
        Font fdatosfactura = new Font("Verdana", Font.PLAIN, 8);
        Font fdetalle = new Font("Verdana", Font.PLAIN, 10);

        String line = "----------------------------------------";

        int x = 12;
        int xValue = 82;
        int y = 2;

        printCabecera(g, y, x, fdatosfactura);
        y = 60;
        y = y + 15;
        g.setFont(new Font("Verdana", Font.BOLD, 14));

        String aux = " RECIBO DE ENTREGA ";
        if (typeDocument.equals(Document.RECIBO_POR_PAGAR)) {
            aux = " RECIBO DE ENTREGA       POR  PAGAR";
        }
//        y = drawLine(g, "", aux, 5, 20, y, 20);
        y = drawBetweenXtoX1(aux, g.getFont(), g, 20, 210, y, 15);

//        g.drawString(aux, getX(aux, g.getFont(), g), y);// 49
        //        y = y + 15;
        g.setFont(flinea);
        g.drawString(line, x, y);

        y = y + 10;

        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        String fecha = "";
        if (fac != null) {
            fecha = fac.getFecha();
        } else {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd  k:m");
            fecha = formato.format(new Date());
        }

        y = drawLine(g, "FECHA :", fecha, x, x + 50, y, 10);
        y = drawLine(g, "Origen :", e.getOrigen(), x, xValue, y, 12);
        y = drawLine(g, "Destino :", e.getDestino(), x, xValue, y, 12);
        y = drawLine(g, "Entregado en  :", e.getDestino(), x, xValue, y, 12);

        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        y = drawLine(g, "GUIA :", e.getGuia(), x, xValue, y, 12);
        y = drawLine(g, "Remitente :", e.getRemitente(), x, xValue, y, 12);
        y = drawLine(g, "Destinatario :", e.getConsignatario(), x, xValue, y, 12);
        y = drawLine(g, "Recoje :", e.getReceptor(), x, xValue, y, 12);
        y = drawLine(g, "Documento :", e.getCarnet(), x, xValue, y, 12);
        y = drawLine(g, "Telefono  :", e.getTelefono(), x, xValue, y, 12);

        if (fac != null && fac.getNumeroFactura() != null && !fac.getNumeroFactura().isEmpty()) {
            y = drawLine(g, "Segun Factura", " : " + fac.getNumeroFactura(), x, xValue + 25, y, 14);
        }
        boolean par = false;
        int xPrecio = 165;
        //      PINTAMOS LOS ITEMS
        y = drawItems(x, y, g);
        //      PINTAMOS LOS ITEMS

        y = y + 40;

        g.setFont(fdatosfactura.deriveFont(Font.BOLD));
        g.drawString("Usuario :", x, y);
        g.setFont(fdetalle.deriveFont(Font.BOLD));
        g.drawString(cab.usuario, x + 100, y);

        y = y + 40;
        y = drawLine(g, "Firma  :", "..........................", x, xValue, y, 12);
        y = drawLine(g, "        ", e.getReceptor(), x, xValue, y, 12, 200);
        y = y + 8;
        g.setFont(flinea);
        g.drawString(line, x, y);

        y = y + 7;
        drawQuantum(fdatosfactura, g, y);
        y = y + 2;

    }

    public void setEncomienda(Encomienda ec) {
        this.e = ec;
    }

    public void setCabecera(Cabecera cab) {
        this.cab = cab;
    }

    public void setFactura(Factura f) {
        this.fac = f;
    }

    public void setEncomiendas(List<Encomienda> encs) {
        encomiendas = encs;
    }

    public void setManifiesto(Manifiesto ma) {
        man = ma;
    }

    private String getQRText() {
        StringBuilder textoQR = new StringBuilder();
        textoQR.append(cab.nitEmpresa);
        textoQR.append("|");
        textoQR.append(cab.nombreEmpresa);
        textoQR.append("|");
        textoQR.append(fac.getNumeroFactura());
        textoQR.append("|");
        textoQR.append(fac.getAutorizacion());
        textoQR.append("|");
        textoQR.append(fac.getFecha());
        textoQR.append("|");
        textoQR.append(fac.getTotal());
        textoQR.append("|");
        textoQR.append(fac.getCodigoControl());
        textoQR.append("|");
        textoQR.append(fac.getFechaLimite());
        textoQR.append("|");
        textoQR.append("0");
        textoQR.append("|");
        textoQR.append("0");
        textoQR.append("|");
        textoQR.append(fac.getNit());
        textoQR.append("|");
        textoQR.append(fac.getNombre());
        return textoQR.toString();
    }

    private String getDecimalFormat(double number) {
        Locale locale = new Locale("en", "US");
        String pattern = "#,###.00";
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        formatter.applyPattern(pattern);
        return formatter.format(number);
    }
}
