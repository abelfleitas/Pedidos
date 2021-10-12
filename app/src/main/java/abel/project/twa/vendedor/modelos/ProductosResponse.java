package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class ProductosResponse {

    private ArrayList<Producto> data;

    public ProductosResponse(ArrayList<Producto> pdata) {
        this.data = pdata;
    }

    public ArrayList<Producto> getData() {
        return data;
    }

}
