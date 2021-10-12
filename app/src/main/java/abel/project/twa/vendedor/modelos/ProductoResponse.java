package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class ProductoResponse {

    private Producto data;

    public ProductoResponse(Producto pdata) {
        this.data = pdata;
    }

    public Producto getObjProducto() {
        return data;
    }
}
