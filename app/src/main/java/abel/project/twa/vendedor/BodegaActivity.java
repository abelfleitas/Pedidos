package abel.project.twa.vendedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.app.infideap.stylishwidget.view.AButton;
import java.util.ArrayList;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.Bodega;
import abel.project.twa.vendedor.modelos.BodegasResponse;
import abel.project.twa.vendedor.auxiliar.SimpleListAdapter;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BodegaActivity extends AppCompatActivity {

    private AButton aceptar,cancelar;
    private Utils utils;
    private Retrofit retrofit;
    private RetrofitApiJson retrofitApiJson;
    private ArrayList<String> aux;
    private ProgressDialog progressDialog;
    private SearchableSpinner mSearchableSpinner1;
    private SimpleListAdapter mSimpleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega);
        utils = new Utils(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        aux  = new ArrayList<>();
        aceptar = (AButton) findViewById(R.id.btnAceptar) ;
        cancelar = (AButton) findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiJson = retrofit.create(RetrofitApiJson.class);

        progressDialog = new ProgressDialog(BodegaActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        final GoRecuperarBodegas goBodegas = new GoRecuperarBodegas();
        goBodegas.execute();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aux.size()>0){
                    if(mSearchableSpinner1.getSelectedItem() != null){
                        String DescripBodega = mSearchableSpinner1.getSelectedItem().toString();
                        final GoSeleccionarBodega goBodegas = new GoSeleccionarBodega(DescripBodega);
                        goBodegas.execute();
                    }
                    else{
                        utils.showInfo(getResources().getString(R.string.thereisnot_bodega));
                    }
                }else {
                    utils.showInfo(getResources().getString(R.string.thereisnot_bodegas));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        utils.getAux().setFirst(true);
        startActivity(new Intent(BodegaActivity.this, LoginActivity.class));
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

    private class GoRecuperarBodegas extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            recuperarBodegas();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void recuperarBodegas(){
            Call<BodegasResponse> call = retrofitApiJson.getBodegas();
            call.enqueue(new Callback<BodegasResponse>() {
                @Override
                public void onResponse(Call<BodegasResponse> call, Response<BodegasResponse> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    }else {
                        BodegasResponse  bodegasResponse = response.body();
                        for (Bodega bodega : bodegasResponse.getData()) {
                            aux.add(bodega.getDescrip());
                        }
                        mSimpleListAdapter = new SimpleListAdapter(getApplicationContext(), aux);
                        mSearchableSpinner1 = (SearchableSpinner) findViewById(R.id.SearchableSpinner1);
                        mSearchableSpinner1.setAdapter(mSimpleListAdapter);
                        progressDialog.dismiss();
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

    private class GoSeleccionarBodega extends AsyncTask<String, Integer, String> {

        private String DescripBodega;

        public GoSeleccionarBodega(String descripBodega) {
            DescripBodega = descripBodega;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            guardarBodegaBD();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void guardarBodegaBD(){

            Call<BodegasResponse> call = retrofitApiJson.getBodega(DescripBodega);
            call.enqueue(new Callback<BodegasResponse>() {
                @Override
                public void onResponse(Call<BodegasResponse> call, Response<BodegasResponse> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    }else {
                        BodegasResponse bodegasResponse = response.body();
                        String codeUbic = bodegasResponse.getData().get(0).getCodUbic();
                        String descrip = bodegasResponse.getData().get(0).getDescrip();
                        utils.getMap().actualizarBodega(utils.getMap().getIdUser(),codeUbic,descrip);
                        utils.getAux().setFirst(true);
                        progressDialog.dismiss();


                        Intent intent = new Intent(BodegaActivity.this, PrincipalActivity.class);
                        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                        startActivity(intent);
                        finish();
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


