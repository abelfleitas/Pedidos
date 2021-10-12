package abel.project.twa.vendedor.auxiliar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import abel.project.twa.vendedor.BodegaActivity;
import abel.project.twa.vendedor.LoginActivity;
import abel.project.twa.vendedor.PrincipalActivity;
import abel.project.twa.vendedor.R;
import abel.project.twa.vendedor.basedatos.DataPedidos;
import cn.refactor.lib.colordialog.PromptDialog;
import cn.refactor.lib.colordialog.PromptDialogExit;

public class Utils {

    private AppCompatActivity context;
    private String ecncriptPassword = "HKMWPqQSpsB8Lf5y";
    private DataPedidos map;
    private PreferenciaAux aux;
    private boolean response;


    public Utils(AppCompatActivity context) {
        this.context = context;
        map = new DataPedidos(context);
        aux = new PreferenciaAux(context);
        response =  false;
    }

    public void salir(){
        PromptDialogExit dialog = new PromptDialogExit(context);
        dialog.setDialogType(PromptDialogExit.DIALOG_TYPE_INFO);
        dialog.setAnimationEnable(true);
        dialog.setTitleText((int) R.string.confirm);
        dialog.setContentText((int) R.string.confirm_exit);
        dialog.setCancelable(false);
        dialog.setPositiveListener(context.getString(R.string.aceptar), new PromptDialogExit.OnPositiveListener() {
            public void onClick(PromptDialogExit dialog) {
                dialog.dismiss();
                if(aux.isActiveSessionClose()){
                    aux.setLogged(false);
                    map.logoutUser(map.getIdUser());
                }
                context.finish();
            }
        });
        dialog.setNegativeListener(context.getString(R.string.md_cancel_label), new PromptDialogExit.OnNegativeListener() {
            public void onClick(PromptDialogExit dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void dialogCerrarSesion(){
        PromptDialogExit dialog = new PromptDialogExit(context);
        dialog.setDialogType(PromptDialogExit.DIALOG_TYPE_WRONG);
        dialog.setAnimationEnable(true);
        dialog.setTitleText((int) R.string.alert);
        dialog.setContentText((int) R.string.confirm_exitsesion);
        dialog.setCancelable(false);
        dialog.setPositiveListener(context.getString(R.string.aceptar), new PromptDialogExit.OnPositiveListener() {
            public void onClick(PromptDialogExit dialog) {
                dialog.dismiss();
                aux.setLogged(false);
                map.logoutUser(map.getIdUser());
                Intent intent = new Intent(context,LoginActivity.class);
                context.overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                context.startActivity(intent);
                context.finish();
            }
        });
        dialog.setNegativeListener(context.getString(R.string.md_cancel_label), new PromptDialogExit.OnNegativeListener() {
            public void onClick(PromptDialogExit dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void alertInternet(){
        PromptDialog dialog = new PromptDialog(context);
        dialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG);
        dialog.setAnimationEnable(true);
        dialog.setTitleText((int) R.string.alert);
        dialog.setContentText((int) R.string.alert_text);
        dialog.setCancelable(false);
        dialog.setPositiveListener(context.getResources().getString(R.string.aceptar), new PromptDialog.OnPositiveListener() {
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showError(String msj){
        PromptDialog dialog = new PromptDialog(context);
        if(!dialog.getWindow().isActive()){
            dialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG);
            dialog.setAnimationEnable(true);
            dialog.setTitleText((int) R.string.alert);
            dialog.setContentText(msj);
            dialog.setCancelable(false);
            dialog.setPositiveListener(context.getResources().getString(R.string.aceptar), new PromptDialog.OnPositiveListener() {
                public void onClick(PromptDialog dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void showErrorFinish(String msj){
        PromptDialog dialog = new PromptDialog(context);
        dialog.setDialogType(PromptDialog.DIALOG_TYPE_WRONG);
        dialog.setAnimationEnable(true);
        dialog.setTitleText((int) R.string.alert);
        dialog.setContentText(msj);
        dialog.setCancelable(false);
        dialog.setPositiveListener(context.getResources().getString(R.string.aceptar), new PromptDialog.OnPositiveListener() {
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
                context.finish();
            }
        });
        dialog.show();
    }

    public void showInfo(String msj){
        PromptDialog dialog = new PromptDialog(context);
        if(!dialog.getWindow().isActive()){
            dialog.setDialogType(PromptDialog.DIALOG_TYPE_HELP);
            dialog.setAnimationEnable(true);
            dialog.setTitleText(context.getResources().getString(R.string.error_info));
            dialog.setContentText(msj);
            dialog.setCancelable(false);
            dialog.setPositiveListener(context.getResources().getString(R.string.aceptar), new PromptDialog.OnPositiveListener() {
                public void onClick(PromptDialog dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public boolean showInfoBolean(String msj){
        PromptDialogExit dialog = new PromptDialogExit(context);
        dialog.setDialogType(PromptDialogExit.DIALOG_TYPE_HELP);
        dialog.setAnimationEnable(true);
        dialog.setTitleText(context.getResources().getString(R.string.error_info));
        dialog.setContentText(msj);
        dialog.setCancelable(false);
        dialog.setPositiveListener(context.getString(R.string.aceptar), new PromptDialogExit.OnPositiveListener() {
            public void onClick(PromptDialogExit dialog) {
                dialog.dismiss();
                response = true;
            }
        });
        dialog.setNegativeListener(context.getString(R.string.md_cancel_label), new PromptDialogExit.OnNegativeListener() {
            public void onClick(PromptDialogExit dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return response;
    }

    public void showSatisfactorio(String msj){
        PromptDialog dialog = new PromptDialog(context);
        dialog.setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS);
        dialog.setAnimationEnable(true);
        dialog.setTitleText(context.getResources().getString(R.string.error_info));
        dialog.setContentText(msj);
        dialog.setCancelable(false);
        dialog.setPositiveListener(context.getResources().getString(R.string.aceptar), new PromptDialog.OnPositiveListener() {
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public boolean checkInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return  true;
        } else {
            return false;
        }
    }


    public String getEstatus(){
        String status = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo.getState() == NetworkInfo.State.CONNECTED){
            status = "Conectado";
        }
        else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED){
            status = "Desconectado";
        }
        else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTING){
            status = "Desconectando";
        }
        else if (networkInfo.getState() == NetworkInfo.State.SUSPENDED){
            status = "Suspendido";
        }else{
            status = "Desconocido";
        }
        return  status;
    }

    public SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    public String encriptar(String datos,String password) throws Exception{
        SecretKeySpec secretKeySpec = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes,Base64.DEFAULT);
        return datosEncriptadosString;
    }

    public String deseencriptar(String datos,String password) throws Exception{
        SecretKeySpec secretKeySpec = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte[] datosDesencriptados = Base64.decode(datos,Base64.DEFAULT);
        byte[] datosDesencriptadosBytes = cipher.doFinal(datosDesencriptados);
        String datosDesencriptadosString = new String(datosDesencriptadosBytes);
        return datosDesencriptadosString;
    }

    public String getClavaEcncript() {
        return ecncriptPassword;
    }

    public DataPedidos getMap() {
        return map;
    }

    public boolean verificarLogin(){
        if(aux.isActiveSessionClose()){
            return false;
        }else{
            return aux.isLogged();
        }
    }

    public PreferenciaAux getAux() {
        return aux;
    }

    public void onLoginSuccess() {
        if(aux.isFirst()){
            aux.setFirst(false);
            Intent intent = new Intent(context,BodegaActivity.class);
            context.overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            context.startActivity(intent);
            context.finish();
        }else{
            Intent intent = new Intent(context,PrincipalActivity.class);
            context.overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            context.startActivity(intent);
            context.finish();
        }
    }

    public static double fijarNumero(double numero, int digitos) {
        double resultado;
        resultado = numero * Math.pow(10, digitos);
        resultado = Math.round(resultado);
        resultado = resultado/Math.pow(10, digitos);
        return resultado;
    }

    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }

}

