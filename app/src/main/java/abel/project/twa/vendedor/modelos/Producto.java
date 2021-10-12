package abel.project.twa.vendedor.modelos;

import java.io.Serializable;

public class Producto implements Serializable{

    private String CodProd;
    private String Descrip;
    private String Precio1;
    private String Unidad;
    private String IVA;

    public Producto(String codProd, String descrip, String precio1, String unidad,String pimpuesto) {
        CodProd = codProd;
        Descrip = descrip;
        Precio1 = precio1;
        Unidad = unidad;
        IVA = pimpuesto;
    }

    public String getCodProd() {
        return CodProd;
    }

    public String getDescrip() {
        return Descrip;
    }

    public String getPrecio1() {
        return Precio1;
    }

    public String getUnidad() {
        return Unidad;
    }

    public String getImpuesto() {
        return IVA;
    }

    public void setPrecio1(String precio1) {
        Precio1 = precio1;
    }

}
