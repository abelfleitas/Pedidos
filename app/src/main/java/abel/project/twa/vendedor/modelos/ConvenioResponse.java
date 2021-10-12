package abel.project.twa.vendedor.modelos;

public class ConvenioResponse {

    private Convenio data;

    public ConvenioResponse(Convenio pdata) {
        this.data = pdata;
    }

    public Convenio getObjConvenio() {
        return data;
    }

}
