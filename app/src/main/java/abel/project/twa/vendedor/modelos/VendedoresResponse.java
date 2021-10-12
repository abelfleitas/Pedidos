package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class VendedoresResponse {

    private ArrayList<Vendedor> data;

    public VendedoresResponse(ArrayList<Vendedor> pdata) {
        this.data = pdata;
    }

    public ArrayList<Vendedor> getData() {
        return data;
    }

}
