package abel.project.twa.vendedor.modelos;

public class ClienteResponse {

    private Cliente data;

    public ClienteResponse(Cliente pdata) {
        this.data = pdata;
    }

    public Cliente getObjCliente() {
        return data;
    }
}
