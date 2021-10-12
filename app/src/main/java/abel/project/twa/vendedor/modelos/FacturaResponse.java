package abel.project.twa.vendedor.modelos;

public class FacturaResponse {

    private ObjAux data;

    public FacturaResponse(ObjAux pdata) {
        this.data = pdata;
    }

    public ObjAux getObjAux() {
        return data;
    }
}
