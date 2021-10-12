package abel.project.twa.vendedor.modelos;

import java.util.ArrayList;

public class ConveniosResponse {

    private ArrayList<Convenio> data;

    public ConveniosResponse(ArrayList<Convenio> pdata) {
        this.data = pdata;
    }

    public ArrayList<Convenio> getData() {
        return data;
    }

}
