package abel.project.twa.vendedor.auxiliar;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenciaAux {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "Pedidos";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String ALLWAS_SESSIOM = "session";
    private static final String URL_BASE = "urlbase";
    private static final String IS_FIRST = "primera_vez";
    private static final String EDIT_TEXT_CON_VALOR = "editext_con_valor";
    private static final String CLIENTE_TEXT_CON_VALOR = "cliente_con_valor";
    private static final String FACTOR = "factor";
    private static final String ISEDITAR = "is_editar";
    private static final String CLIENTE = "cliente";
    private Context context;
    int PRIVATE_MODE = 0;

    private boolean isLogged  = false;
    private boolean primeraVez = true;
    private boolean editTextConValor = false;
    private boolean clienteTextConValor = false;
    private float factor = 0;
    private boolean isEditar = false;
    private String cliente = "";

    //private String url = "http://192.168.184.1:5900/myigniter/";
    private String url = "http://192.168.142.1:5900/";

    public PreferenciaAux( Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isLogged() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, isLogged);
    }

    public void setLogged(boolean isLogged) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isLogged);
        editor.commit();
    }

    public boolean isActiveSessionClose() {
        return pref.getBoolean(ALLWAS_SESSIOM, isLogged);
    }

    public void setSessionClose(boolean isLogged) {
        editor.putBoolean(ALLWAS_SESSIOM,isLogged);
        editor.commit();
    }

    public String getUrlBase() {
        return pref.getString(URL_BASE, url);
    }

    public void setUrlBase(String url) {
        editor.putString(URL_BASE,url);
        editor.commit();
    }

    public boolean isFirst(){
        return pref.getBoolean(IS_FIRST, primeraVez);
    }

    public void setFirst(boolean primeraVez) {
        editor.putBoolean(IS_FIRST,primeraVez);
        editor.commit();
    }

    public boolean isEditTextProductoConValor(){
        return pref.getBoolean(EDIT_TEXT_CON_VALOR, editTextConValor);
    }

    public void setEditTextProductoConValor(boolean editTextConValor){
        editor.putBoolean(EDIT_TEXT_CON_VALOR,editTextConValor);
        editor.commit();
    }

    public boolean isClienteProductoConValor(){
        return pref.getBoolean(CLIENTE_TEXT_CON_VALOR, clienteTextConValor);
    }

    public void setClienteTextConValor(boolean clienteTextConValor){
        editor.putBoolean(CLIENTE_TEXT_CON_VALOR,clienteTextConValor);
        editor.commit();
    }

    public float getFactor(){
        return pref.getFloat(FACTOR, factor);
    }

    public void setFactor(float factor){
        editor.putFloat(FACTOR,factor);
        editor.commit();
    }

    public boolean isEdit(){
        return pref.getBoolean(ISEDITAR, isEditar);
    }

    public void setEditar(boolean isEditar){
        editor.putBoolean(ISEDITAR,isEditar);
        editor.commit();
    }

    public String getCliente(){
        return pref.getString(CLIENTE, cliente);
    }

    public void setCliente(String cliente){
        editor.putString(CLIENTE,cliente);
        editor.commit();
    }


}
