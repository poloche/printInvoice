package printer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Administrador
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.*;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JOptionPane;

//La clase debe de implementar la impresión implements Printable
public class AppletImpresionEncomienda extends JApplet {

    private static final long serialVersionUID = 1L;
    private Manifiesto manifiesto;
    private Cabecera cabecera;
    private Encomienda encomienda;
    private List<Encomienda> encomiendas;
    private Factura factura = null;
    private ArrayList<Item> items = new ArrayList<Item>();
    public int copies = 1;
    public Document typeDocument = Document.FACTURA;
    public boolean debug = false;

    public AppletImpresionEncomienda() {
        super();
        encomiendas = new ArrayList<Encomienda>();
    }

    @Override
    public void init() {
//        this.setSize(500, 500);
    }

    public boolean checkPrinter() {
        boolean printers = false;
        if (PrintServiceLookup.lookupDefaultPrintService() == null) {
            infoImpresoras();

        } else {
            printers = true;
        }
        System.out.println("Comprobando coneccion de impresora (" + printers + ")");
        return printers;
    }

    public void infoImpresoras() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Printer servicess " + services.length);
        for (PrintService printService : services) {
            System.out.println("Nombre " + printService.getName());
            PrintServiceAttributeSet printServiceAttributeSet = printService.getAttributes();
            System.out.println("PrinterLocation: " + printServiceAttributeSet.get(PrinterLocation.class));
            System.out.println("PrinterInfo: " + printServiceAttributeSet.get(PrinterInfo.class));
            System.out.println("PrinterState: " + printServiceAttributeSet.get(PrinterState.class));
            System.out.println("Destination: " + printServiceAttributeSet.get(Destination.class));
            System.out.println("PrinterMakeAndModel: " + printServiceAttributeSet.get(PrinterMakeAndModel.class));
            System.out.println("PrinterIsAcceptingJobs: " + printServiceAttributeSet.get(PrinterIsAcceptingJobs.class));
        }
        System.out.println("El PrintServiceLookup.lookupDefaultPrintService() " + PrintServiceLookup.lookupDefaultPrintService());
    }

    public void clean() {
        manifiesto = new Manifiesto("", "", "", "", "");
        cabecera = new Cabecera("", "", "", "", "", "", "", "", "", "");
        encomienda = null;
        items = new ArrayList<Item>();
        encomiendas = new ArrayList<Encomienda>();
        factura = null;
    }

    public void addItem(String cantidad, String detalle, String unitario, String precio) {
        Item i = new Item();
        i.setCantidad(cantidad);
        i.setDetalle(detalle);
        i.setPeso(unitario);
        i.setTotal(precio);
        items.add(i);
        encomienda.setItems(items);
    }

    public void setManifiesto(String chofer, String fecha, String hora, String destino, String nroBus) {
        manifiesto = new Manifiesto(chofer, fecha, hora, destino, nroBus);
    }

    public void setCabecera(String numSucursal, String impresor, String direccion,
            String direccion2, String ciudad, String telefono, String usuario,
            String titleEmp, String nombEmp, String nitEmp) {
        cabecera = new Cabecera(numSucursal, impresor, direccion, direccion2,
                ciudad, telefono, usuario, titleEmp, nombEmp, nitEmp);
    }

    public void setEncomienda(String consignatario, String destino,
            String detalle, String guia, String origen, String remitente,
            String total, String titulo, String telf, String valorDeclarado,
            String Obs, String ciudadDestino) {
        encomienda = new Encomienda();
        encomienda.setConsignatario(consignatario);
        encomienda.setDestino(destino);
        encomienda.setDetalle(detalle);
        encomienda.setGuia(guia);
        encomienda.setOrigen(origen);
        encomienda.setRemitente(remitente);
        encomienda.setTotal(total);
        encomienda.setTitle(titulo);
        encomienda.setTelefono(telf);
        encomienda.setValorDeclarado(valorDeclarado);
        encomienda.setObservacion(Obs);
        encomienda.setCiudadDestino(ciudadDestino);
    }

    public void addInfoEntrega(String receptor, String carnet) {
        encomienda.setReceptor(receptor);
        encomienda.setCarnet(carnet);
    }

    public void addEncomienda(String guia, String detalle, String importe, String tipo) {
        Encomienda enc = new Encomienda();
        enc.setDetalle(detalle);
        enc.setGuia(guia);
        enc.setTotal(importe);
        enc.setTitle(tipo);
        encomiendas.add(enc);
    }

    public void setInfoSucursal(String municipio, String leyendaActividad, String tipoFactura, boolean isCapital, String nombreCiudad2) {

        cabecera.setMunicipio(municipio);
        cabecera.setLeyendaActividad(leyendaActividad == null ? "" : leyendaActividad);
        cabecera.setTipoFactura(tipoFactura == null ? "" : tipoFactura);
        cabecera.setCiudadCapital(isCapital);
        cabecera.setCiudad2(nombreCiudad2 == null ? "" : nombreCiudad2);
    }

    public void setFactura(String fecha, String hora, String nombre, String nit,
            String numeroFactura, String autorizacion, String codigoControl,
            String fechaLimite, String total, String totalLiteral) {
        factura = new Factura();
        factura.setAutorizacion(autorizacion);
        factura.setCodigoControl(codigoControl);
        factura.setFecha(fecha);
        factura.setFechaLimite(fechaLimite);
        factura.setHora(hora);
        factura.setNit(nit);
        factura.setNombre(nombre);
        factura.setNumeroFactura(numeroFactura);
        factura.setTotal(total);
        factura.setTotalLiteral(totalLiteral);

    }

    public void imprimir() {
        try {
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
//Indicamos que lo que vamos a imprimir es un objeto imprimible
            DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            DocPrintJob pj = service.createPrintJob();
//Creamos el precio a imprimir que contendrá el objeto
            DocumentPrint print = new DocumentPrint(typeDocument);
            print.setEncomienda(encomienda);
            print.setCabecera(cabecera);
            print.setFactura(factura);
            print.setManifiesto(manifiesto);
            print.setEncomiendas(encomiendas);
            Doc doc = new SimpleDoc(print, flavor, null);
            if (!debug) {
                try {
                    String cadenaImp = "dataPrint/" + toJSON();
                    URLEncoder.encode(cadenaImp, "UTF-8");

                    URL baseUrl = getDocumentBase();
                    String path = baseUrl.getPath();
                    if (path.contains("/index*")) {
                        path = path.substring(0, path.lastIndexOf("/"));
                    }

                    URL enviar = new URL(baseUrl.getProtocol() + "://" + baseUrl.getHost() + path + "/log-impresion/" + cadenaImp);
                    URLConnection coneccion = enviar.openConnection();
                    coneccion.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(coneccion.getOutputStream());
                    wr.write(cadenaImp);
                    wr.flush();

                    //se obtiene la respuesta del servidor
                    BufferedReader rd = new BufferedReader(new InputStreamReader(coneccion.getInputStream()));
                    String linea, tmp = "";
                    while ((linea = rd.readLine()) != null) {
                        tmp += linea;
                    }
//            JOptionPane.showMessageDialog(null, tmp);
                    wr.close();
                    rd.close();
                } catch (IOException ex) {
                    Logger.getLogger(AppletImpresionEncomienda.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                aset.add(new MediaPrintableArea(1f, 1f, 80f, 279f, MediaPrintableArea.MM));
                aset.add(new Copies(copies));
                String jobname = getJobName();
                aset.add(new JobName(jobname, null));
                pj.print(doc, aset);
            } catch (PrintException e) {
                System.out.println("Error al imprimir: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String toJSON() {
        String json = "{";
        json += "'factura':";
        if (factura != null) {
            json += "{'nit':'" + factura.getNit() + "',";
            json += "'nombre':'" + factura.getNombre() + "',";
            json += "'numero':'" + factura.getNumeroFactura() + "',";
            json += "'autorizacion':'" + factura.getAutorizacion() + "',";
            json += "'codigoControl':'" + factura.getCodigoControl() + "',";
            json += "'fecha':'" + factura.getFecha() + "',";
            json += "'fechaLimite':'" + factura.getFechaLimite() + "',";
            json += "'hora':'" + factura.getHora() + "',";
            json += "'total':'" + factura.getTotal() + "',";
            json += "'literal':'" + factura.getTotalLiteral() + "'},";
        } else {
            json += "'null',";
        }
        json += "'encomienda':";
        if (encomienda != null) {
            json += "{'carnet':'" + encomienda.getCarnet() + "',";
            json += "'consignatario':'" + encomienda.getConsignatario() + "',";
            json += "'destino':'" + encomienda.getDestino() + "',";
            json += "'detalle':'" + encomienda.getDetalle() + "',";
            json += "'guia':'" + encomienda.getGuia() + "',";
            json += "'origen':'" + encomienda.getOrigen() + "',";
            json += "'receptor':'" + encomienda.getReceptor() + "',";
            json += "'remitente':'" + encomienda.getRemitente() + "',";
            json += "'title':'" + encomienda.getTitle() + "',";
            json += "'total':'" + encomienda.getTotal() + "',";
            json += "'tipo':'" + encomienda.getTipo().getTitle() + "',";
            json += "'items':[";
            int i = 0;
            for (Item item : items) {
                json += "'i" + i + "':{";
                json += "'cantidad':'" + item.getCantidad() + "',";
                json += "'detalle':'" + item.getDetalle() + "',";
                json += "'peso':'" + item.getPeso() + "',";
                json += "'total':'" + item.getTotal() + "'}";
                json += i < items.size() - 1 ? "," : "";
                i++;
            }
            json += " ]";
            json += "},";
        } else {
            json += "'null',";
        }
        json += "'manifiesto':";
        if (manifiesto != null) {
            json += "{'chofer':'" + manifiesto.chofer + "',";
            json += "'destino':'" + manifiesto.destino + "',";
            json += "'fecha':'" + manifiesto.fecha + "',";
            json += "'hora':'" + manifiesto.hora + "',";
            json += "'numeroBus':'" + manifiesto.nroBus + "',";
            json += "'encomiendas':[";
            int i = 0;
            for (Encomienda item : encomiendas) {
                json += "'e" + i + "':{";
                json += "'detalle':'" + item.getDetalle() + "',";
                json += "'guia':'" + item.getGuia() + "',";
                json += "'total':'" + item.getTotal() + "'}";
                json += i < encomiendas.size() - 1 ? "," : "";
                i++;
            }
            json += "]";
            json += "}";
        } else {
            json += "'null'";
        }
        json += "}";
        json = json.replace("/", "\\");
        json = json.replace(" ", "%20");
        return json;
    }

    public static void main(String[] s) {
        final AppletImpresionEncomienda a = new AppletImpresionEncomienda();
//        a.infoImpresoras();
        ActionListener listener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("fatura")) {
                    printFacturaEncomienda(a);
                } else if (e.getActionCommand().equals("manifiesto")) {
                    printManifiesto(a);
                }
            }
        };
        JButton facturaEncomienda = new JButton("Factura encomienda");
        facturaEncomienda.setActionCommand("factura");
        facturaEncomienda.addActionListener(listener);

        JButton manifiesto = new JButton("Manifiesto");
        manifiesto.setActionCommand("manifiesto");
        manifiesto.addActionListener(listener);
        Object buttons[] = new Object[2];
        buttons[0] = facturaEncomienda;
        buttons[1] = manifiesto;
        JOptionPane.showOptionDialog(a, "Selecciona una opcion", "Tipo de impresion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, null);

    }

    private String getJobName() {
        String jobname = "printing document";
        if (typeDocument.equals(Document.FACTURA)) {
            if (factura != null) {
                jobname = "Factura No ".concat(factura.getNumeroFactura());
            } else {
                jobname = "Recibo " + typeDocument + " ".concat(encomienda.getGuia());
            }
        } else {
            if (typeDocument.equals(Document.GUIA)) {
                jobname = "Guia de ".concat(encomienda.getTipo().getTitle()).concat(encomienda.getGuia());
            } else if (typeDocument.equals(Document.RECIBO_ENTREGA) || typeDocument.equals(Document.RECIBO_POR_PAGAR)) {
                jobname = "Guia de ".concat(encomienda.getTipo().getTitle()).concat(encomienda.getGuia());
            } else {
                jobname = "Manifiesto  ".concat(manifiesto.fecha).concat("--").concat(manifiesto.nroBus);

            }
        }
        return jobname;
    }

    private static void printFacturaEncomienda(AppletImpresionEncomienda a) {
        a.debug = true;
        System.out.println("El ejecutable es " + a.checkPrinter());
        a.typeDocument = Document.FACTURA;
        a.setEncomienda("Paolo milano", "El alto", "una mochila", "A-1010", "6 de agosto", "Simon Pedro", "100", "67857495", "false", "sin valor declarado", "", "La paz");
        a.addItem("1", "mochila", "100", "100");
        a.setCabecera("2", "02", "Av ayacucho zona central", "terminal de buses", "Cochabamba", "4358089", "paolo", "Viajando al futuro", "Mobius it srl", "1234657");
        a.imprimir();
        a.typeDocument = Document.RECIBO_ENTREGA;
        a.imprimir();
    }

    private static void printManifiesto(AppletImpresionEncomienda a) {
        a.debug = true;
        a.typeDocument = Document.MANIFIESTO;
        a.setCabecera("2", "02", "Av ayacucho zona central", "terminal de buses", "Cochabamba", "4358089", "paolo", "Viajando al futuro", "Mobius it srl", "1234657");
        a.setManifiesto("Pepe Arias", "2015-08-19", "18:30", "Santa cruz", "5");

        for (int i = 100; i < 110; i++) {
            a.addEncomienda("G-" + i + "S", "caja de erramientas", "100", "Normal");
        }

        for (int i = 110; i < 115; i++) {
            a.addEncomienda("G-" + i + "S", "caja de erramientas con multiples cosas para poder manipular tuercas", "100", "Por Pagar");
        }
        a.imprimir();
    }
}
