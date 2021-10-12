package abel.project.twa.vendedor.modelos;

public class Vendedor {

    private String CodVend;
    private String Descrip;
    private  int Activo;

    public Vendedor(String CodVend, String Descrip, int Activo) {
        this.CodVend = CodVend;
        this.Descrip = Descrip;
        this.Activo = Activo;
    }

    public String getCodVend() {
        return CodVend;
    }

    public String getDescrip() {
        return Descrip;
    }

    public int getActivo() {
        return Activo;
    }

}
