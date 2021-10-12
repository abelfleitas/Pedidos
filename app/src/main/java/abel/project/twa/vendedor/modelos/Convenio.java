package abel.project.twa.vendedor.modelos;

import java.io.Serializable;
import java.util.Date;

public class Convenio implements Serializable{

    private String CodConv;
    private String Descrip;
    /*private String Autori;
    private String Respon;
    private String FechaE;
    private String FechaV;
    private String Frecuencia;
    private String FechaUC;
    private int EsFijo;
    private int TipoCnv;
    private int EsBase;
    private int Activo;*/

    public Convenio(String codConv, String descrip) {
        CodConv = codConv;
        Descrip = descrip;
        /*Autori = autori;
        Respon = respon;
        FechaE = fechaE;
        FechaV = fechaV;
        Frecuencia = frecuencia;
        FechaUC = fechaUC;
        EsFijo = esFijo;
        TipoCnv = tipoCnv;
        EsBase = esBase;
        Activo = activo;*/
    }

    public String getCodConv() {
        return CodConv;
    }

    public String getDescrip() {
        return Descrip;
    }

    /*public String getAutori() {
        return Autori;
    }

    public String getRespon() {
        return Respon;
    }

    public String getFechaE() {
        return FechaE;
    }

    public String getFechaV() {
        return FechaV;
    }

    public String getFrecuencia() {
        return Frecuencia;
    }

    public String getFechaUC() {
        return FechaUC;
    }

    public int getEsFijo() {
        return EsFijo;
    }

    public int getTipoCnv() {
        return TipoCnv;
    }

    public int getEsBase() {
        return EsBase;
    }

    public int getActivo() {
        return Activo;
    }*/

    /*, String autori, String respon, String fechaE, String fechaV, String frecuencia, String fechaUC, int esFijo, int tipoCnv, int esBase, int activo*/

}
