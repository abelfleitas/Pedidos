package abel.project.twa.vendedor.modelos;

import java.io.Serializable;

public class Cliente implements Serializable{

    String CodClie;
    String Descrip;
    /*int ID3;
    int Activo;
    String Direc1;
    String Direc2;
    int Pais;
    int Estado;
    int Ciudad;
    int Municipio;
    String ZipCode;
    String Telef;
    int TipoPVP;*/

    public Cliente(String codClie, String descrip) {
        this.CodClie = codClie;
        this.Descrip = descrip;
        /*this.ID3 = ID3;
        this.Activo = activo;
        this.Direc1 = direc1;
        this.Direc2 = direc2;
        this.Pais = pais;
        this.Estado = estado;
        this.Ciudad = ciudad;
        this.Municipio = municipio;
        this.ZipCode = zipCode;
        this.Telef = telef;
        this.TipoPVP = tipoPVP;*/
    }


    public String getCodClie() {
        return CodClie;
    }

    public String getDescrip() {
        return Descrip;
    }

    /*public int getID3() {
        return ID3;
    }

    public int getActivo() {
        return Activo;
    }

    public String getDirec1() {
        return Direc1;
    }

    public String getDirec2() {
        return Direc2;
    }

    public int getPais() {
        return Pais;
    }

    public int getEstado() {
        return Estado;
    }

    public int getCiudad() {
        return Ciudad;
    }

    public int getMunicipio() {
        return Municipio;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public String getTelef() {
        return Telef;
    }

    public int getTipoPVP() {
        return TipoPVP;
    }*/

   /* , int ID3, int activo, String direc1, String direc2, int pais, int estado, int ciudad, int municipio, String zipCode, String telef, int tipoPVP*/
}
