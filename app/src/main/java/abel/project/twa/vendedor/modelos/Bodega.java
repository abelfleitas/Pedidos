package abel.project.twa.vendedor.modelos;

import java.io.Serializable;

public class Bodega implements Serializable {

    private String CodUbic;
    private String Descrip;
    private int Activo;

    public Bodega(String codUbic, String descrip, int activo) {
        CodUbic = codUbic;
        Descrip = descrip;
        Activo = activo;
    }

    public String getCodUbic() {
        return CodUbic;
    }

    public String getDescrip() {
        return Descrip;
    }

    public int getActivo() {
        return Activo;
    }

}
