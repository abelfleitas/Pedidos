package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class ObjPedido {

    String CodVend;
    String CodClie;
    String CodUbic;
    String Factor;
    String CodIMEIDisp;
    ArrayList<ProductoAux> Productos;

    public ObjPedido(String codVend, String codClie, String codUbic, String factor, String codIMEIDisp, ArrayList<ProductoAux> productos) {
        CodVend = codVend;
        CodClie = codClie;
        CodUbic = codUbic;
        Factor = factor;
        CodIMEIDisp = codIMEIDisp;
        Productos = productos;
    }
}
