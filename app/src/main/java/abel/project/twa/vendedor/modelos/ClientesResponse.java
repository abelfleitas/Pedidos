package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class ClientesResponse {

    private ArrayList<Cliente> data;

    public ClientesResponse(ArrayList<Cliente> pdata) {
        this.data = pdata;
    }

    public ArrayList<Cliente> getData() {
        return data;
    }

}
