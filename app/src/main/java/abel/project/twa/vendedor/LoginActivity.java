package abel.project.twa.vendedor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.UserLoginResponse;
import abel.project.twa.vendedor.modelos.VendedorResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText _codigo;
    private EditText _password;
    private TextView _signupLink;
    private ProgressDialog progressDialog;
    private Utils utils;
    private Retrofit retrofit;
    private RetrofitApiJson retrofitApiJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        utils = new Utils(this);
        changeStatusBarColor();
        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiJson = retrofit.create(RetrofitApiJson.class);

        progressDialog = new ProgressDialog(LoginActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.validate_data));

        _codigo = (EditText) findViewById(R.id.input_codigo);
        _password = (EditText) findViewById(R.id.input_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView) findViewById(R.id.link_signup);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(utils.checkInternetConnection()){
                    login();
                }else{
                    Intent intent = new Intent(LoginActivity.this, InternetActivity.class);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    intent.putExtra("desde","login");
                    startActivity(intent);
                    finish();
                }
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfigUrlActivity.class);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                startActivity(intent);
                finish();
            }
        });
    }

    public void login() {
        String codigoText = _codigo.getText().toString();
        String passwordText = _password.getText().toString();
        if (!validate(codigoText,passwordText)) {
            onLoginFailed();
            return;
        }
        final GoTask gotask = new GoTask(codigoText,passwordText);
        gotask.execute();
    }

    public void onLoginFailed() {
        btn_login.setEnabled(true);
    }

    public boolean validate(String codigoText,String passwordText) {
        boolean valid = true;
        if (codigoText.isEmpty()) {
            _codigo.setError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else if((!codigoText.isEmpty()) && (codigoText.length() < 4) || (codigoText.length()>10)) {
            _codigo.setError(getResources().getString(R.string.error_code));
            valid = false;
        }else {
            _codigo.setError(null);
        }
        if (passwordText.isEmpty()) {
            _password.setError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else  if((!passwordText.isEmpty()) && (passwordText.length() < 6)){
            _password.setError(getResources().getString(R.string.error_password));
            valid = false;
        }
        else {
            _password.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        this.utils.salir();
    }

    private class GoTask extends AsyncTask<String, Integer, String> {

        private String codigo;
        private String password;

        public GoTask(String codigo, String password) {
            this.codigo = codigo;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            verificarLogin();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            btn_login.setEnabled(false);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            btn_login.setEnabled(true);
        }

        private void verificarLogin(){
            Call<UserLoginResponse> call = retrofitApiJson.login(codigo,password);
            call.enqueue(new Callback<UserLoginResponse>() {
                @Override
                public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                    if (!response.isSuccessful()) {
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    } else {
                        UserLoginResponse userLoginResponse = response.body();
                        if(userLoginResponse.getObjectLogin().isLogged().equals("true")){
                            utils.getAux().setLogged(true);
                            getDescrip(codigo);
                        }
                        else {
                            _password.setText("");
                            progressDialog.dismiss();
                            utils.showInfo(userLoginResponse.getObjectLogin().getMsj());
                        }
                    }
                }
                @Override
                public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    utils.showError(t.getMessage());
                }
            });
        }
    }

    private void getDescrip(String codigo){

        Call<VendedorResponse> call = retrofitApiJson.getVendedorActual(codigo);
        call.enqueue(new Callback<VendedorResponse>() {
            @Override
            public void onResponse(Call<VendedorResponse> call, Response<VendedorResponse> response) {
                if (!response.isSuccessful()) {
                    progressDialog.dismiss();
                    utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                }
                else {
                    VendedorResponse vendedorResponse = response.body();
                    try {
                        String CodeEncript = utils.encriptar(vendedorResponse.getObjVendedor().getCodVend(),utils.getClavaEcncript());
                        utils.getMap().registrarUserLogged(CodeEncript,vendedorResponse.getObjVendedor().getDescrip(),"","");
                        progressDialog.dismiss();
                        utils.onLoginSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<VendedorResponse> call, Throwable t) {
                progressDialog.dismiss();
                utils.showError(t.getMessage());
            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_type_info));



            if ((ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    ||(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    ||(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED)
                    ||(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                    ||(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
                    ||(ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED))
            {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.INSTALL_SHORTCUT,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED}, 0);
            }

        }else {
            utils.showErrorFinish(getResources().getString(R.string.no_use));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int [] grantResults){
        switch (requestCode){
            case 0:{
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }else {
                    LoginActivity.super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                }
            }
            break;
            default:
                LoginActivity.super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

}

