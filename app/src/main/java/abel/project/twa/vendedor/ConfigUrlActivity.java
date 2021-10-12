package abel.project.twa.vendedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.app.infideap.stylishwidget.view.AButton;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.BodegasResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigUrlActivity extends AppCompatActivity {

    AButton btnChangeUrl;
    EditText textUrl;
    Utils utils;
    private Retrofit retrofit;
    private RetrofitApiJson retrofitApiJson;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_url);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        btnChangeUrl = (AButton) findViewById(R.id.btnChangeUrl);
        textUrl = (EditText) findViewById(R.id.url);
        textUrl.setText(utils.getAux().getUrlBase());

        progressDialog = new ProgressDialog(ConfigUrlActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.validate_url));

        btnChangeUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificar();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ConfigUrlActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void verificar() {
        String codigoText = textUrl.getText().toString();
        if (!validate(codigoText)) {
            return;
        }else{
            final GoTask gotask = new GoTask();
            gotask.execute();
        }
    }


    public boolean validate(String codigoText) {
        boolean valid = true;
        if (codigoText.isEmpty()) {
            textUrl.setError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else if((!codigoText.isEmpty()) && (!validarUrl(codigoText))) {
            textUrl.setError(getResources().getString(R.string.error_url));
            valid = false;
        }
        else if((!codigoText.isEmpty()) && (validarUrl(codigoText)) && !codigoText.endsWith("/")) {
            textUrl.setError(getResources().getString(R.string.error_url1));
            valid = false;
        }
        else{
            textUrl.setError(null);
        }
        return valid;
    }

    private boolean validarUrl(String text){
        try {
            new URL(text).toURI();
            return true;

        }catch (URISyntaxException uris){
            return false;
        }catch (MalformedURLException e){
            return false;
        }
    }

    private class GoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            verificarURL();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
            btnChangeUrl.setEnabled(false);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            btnChangeUrl.setEnabled(true);
        }

        private void verificarURL(){

            retrofit = new Retrofit.Builder()
                    .baseUrl(textUrl.getText().toString())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitApiJson = retrofit.create(RetrofitApiJson.class);

           Call<BodegasResponse> call = retrofitApiJson.getBodegas();
            call.enqueue(new Callback<BodegasResponse>() {
                @Override
                public void onResponse(Call<BodegasResponse> call, Response<BodegasResponse> response) {
                    if (!response.isSuccessful()) {
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    } else {
                        BodegasResponse bodegasResponse = response.body();
                        if(bodegasResponse.getData().size()>0){
                            utils.getAux().setUrlBase(textUrl.getText().toString());
                            utils.showSatisfactorio(getResources().getString(R.string.url_save));
                            textUrl.setText(utils.getAux().getUrlBase());
                            progressDialog.dismiss();
                        }else{
                            utils.showInfo(getResources().getString(R.string.error_url_sin_bodegas));
                        }
                    }
                }
                @Override
                public void onFailure(Call<BodegasResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    utils.showError(t.getMessage());
                }
            });
        }
    }

}