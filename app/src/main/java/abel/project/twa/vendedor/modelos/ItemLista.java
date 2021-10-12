package abel.project.twa.vendedor.modelos;

public class ItemLista {

    private int id;
    private String producto;
    private String codigo;
    private double cantidad;
    private String unidad;
    private double precio;
    private double tasa_precio;
    private double total;
    private double tasa_total;
    private String codbodega;
    private String nombodega;
    private String codcliente;
    private String nomcliente;
    private String codConv;
    private String convenio;
    private double iva;
    private double precioInicial;

    public ItemLista(int id, String producto, String codigo, double cantidad, String unidad, double precio, double tasa_precio, double total, double tasa_total,String codbodega,String nombodega,String codcliente,String nomcliente,String codConv, String convenio,double iva,double precioInicial) {
        this.id = id;
        this.producto = producto;
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.precio = precio;
        this.tasa_precio = tasa_precio;
        this.total = total;
        this.tasa_total = tasa_total;
        this.codbodega = codbodega;
        this.nombodega = nombodega;
        this.codcliente = codcliente;
        this.nomcliente = nomcliente;
        this.codConv = codConv;
        this.convenio = convenio;
        this.iva = iva;
        this.precioInicial = precioInicial;
    }

    public int getId() {
        return id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getTasa_precio() {
        return tasa_precio;
    }

    public void setTasa_precio(double tasa_precio) {
        this.tasa_precio = tasa_precio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTasa_total() {
        return tasa_total;
    }

    public void setTasa_total(double tasa_total) {
        this.tasa_total = tasa_total;
    }

    public String getCodbodega() {
        return codbodega;
    }

    public String getNombodega() {
        return nombodega;
    }

    public String getCodClie() {
        return codcliente;
    }

    public String getCliente() {
        return nomcliente;
    }

    public String getCodConv() {
        return codConv;
    }

    public String getConvenio() {
        return convenio;
    }

    public double getIva() {
        return iva;
    }

    public double getPrecioInicial() {
        return precioInicial;
    }

    public void setPrecioInicial(double precioInicial) {
        this.precioInicial = precioInicial;
    }

    public String toString(){

        System.err.println(
                "id  "+ getId()+"\n"+
                "producto  " + getProducto()+"\n"+
                "codigo  " + getCodigo()+"\n"+
                "cantidad  " + getCantidad()+"\n"+
                "unidad  " + getUnidad()+"\n"+
                "precio  " + getPrecio()+"\n"+
                "tasa_precio  " + getTasa_precio()+"\n"+
                "total  " + getTotal()+"\n"+
                "tasa_total  " + getTasa_total()+"\n"+
                "codbodega  " + getCodbodega()+"\n"+
                "nombodega  " + getNombodega()+"\n"+
                "codcliente  " + getCodClie()+"\n"+
                "nomcliente  " + getCliente()+"\n"+
                "codConv  " + getCodConv()+"\n"+
                "convenio " + getConvenio()+"\n"+
                "iva  " + getIva()+"\n");
        return "";
    }

}
