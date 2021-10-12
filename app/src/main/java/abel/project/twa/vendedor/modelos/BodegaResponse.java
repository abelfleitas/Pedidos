package abel.project.twa.vendedor.modelos;

public class BodegaResponse {

    private Bodega data;

    public BodegaResponse(Bodega pdata) {
        this.data = pdata;
    }

    public Bodega getObjBodega() {
        return data;
    }

}
