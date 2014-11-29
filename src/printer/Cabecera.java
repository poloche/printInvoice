package printer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrador
 */
class Cabecera {

    // DATOS SUCURSAL
    public String numeroSucursal = "0";
    public String impresor;
    public String direccion;
    public String direccion2;
    public String ciudad;
    public String telefono;
    public String usuario;
    //DATOS DE LA EMPRESA
    public String titleEmpresa;
    public String nombreEmpresa;
    public String nitEmpresa;
    // configuracion de impresion
    private boolean printGuia = false;
    private String municipio;
    private String leyendaActividad;
    private String tipoFactura;
    private boolean ciudadCapital;
    private String ciudad2;

    public boolean isPrintGuia() {
        return printGuia;
    }

    public void setPrintGuia(boolean printGuia) {
        this.printGuia = printGuia;
    }

    /**
     *
     * @param String numSucursal numero de la sucursal 0 en caso de ser casa
     * matriz
     * @param String impresor codigo de autoimpresor
     * @param String direccion direccion de la oficina
     * @param String direccion2 direccin exacta en la terminal
     * @param String ciudad ciudad en la cual se esta imprimiendo
     * @param String telefono telefono de la sucursal
     * @param String usuario Nombre del usuario que emite
     * @param String titleEmpresa titulo de cabecera de la empresa
     * @param String nombreEmpresa Nombre de la empresa
     * @param String nitEmpresa NIT de la empresa
     */
    public Cabecera(String numSucursal, String impresor, String direccion,
            String direccion2, String ciudad, String telefono, String usuario,
            String titleEmpresa, String nombreEmpresa, String nitEmpresa) {

        this.numeroSucursal = numSucursal;
        this.impresor = impresor;
        this.direccion = direccion;
        this.direccion2 = direccion2;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.usuario = usuario;
        this.titleEmpresa = titleEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.nitEmpresa = nitEmpresa;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("No Suc: ".concat(numeroSucursal));
        buf.append("Impresor: ".concat(impresor));
        buf.append("Direccion: ".concat(direccion));
        buf.append("Direccion2: ".concat(direccion2));
        buf.append("Ciudad: ".concat(ciudad));
        buf.append("Telefono: ".concat(telefono));
        buf.append("Uuario: ".concat(usuario));
        buf.append("Titulo Empresa: ".concat(titleEmpresa));
        buf.append("Nombre Empresa: ".concat(nombreEmpresa));
        buf.append("Nit: ".concat(nitEmpresa));

        return buf.toString();
    }

    public String getMunicipio() {
        return municipio == null ? "Municipio no Registrado" : municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLeyendaActividad() {
        return leyendaActividad;
    }

    public void setLeyendaActividad(String leyendaActividad) {
        this.leyendaActividad = leyendaActividad;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public boolean isCiudadCapital() {
        return ciudadCapital;
    }

    public void setCiudadCapital(boolean ciudadCapital) {
        this.ciudadCapital = ciudadCapital;
    }

    public String getCiudad2() {
        return ciudad2;
    }

    public void setCiudad2(String ciudad2) {
        this.ciudad2 = ciudad2;
    }

}
