package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class BodegasResponse {

    private ArrayList<Bodega> data;

    public BodegasResponse(ArrayList<Bodega> pdata) {
        this.data = pdata;
    }

    public ArrayList<Bodega> getData() {
        return data;
    }

}
