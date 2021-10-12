package abel.project.twa.vendedor.modelos;

public class VendedorResponse {

    private Vendedor data;

    public VendedorResponse(Vendedor pdata) {
        this.data = pdata;
    }

    public Vendedor getObjVendedor() {
        return data;
    }
}
