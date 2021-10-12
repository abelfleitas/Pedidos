package abel.project.twa.vendedor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import com.app.infideap.stylishwidget.view.AEditText;
import java.io.Serializable;
import java.util.ArrayList;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RecyclerViewAdapterCliente;
import abel.project.twa.vendedor.auxiliar.RecyclerViewAdapterConvenio;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.Bodega;
import abel.project.twa.vendedor.modelos.Cliente;
import abel.project.twa.vendedor.modelos.Convenio;
import abel.project.twa.vendedor.modelos.ConvenioResponse;
import abel.project.twa.vendedor.modelos.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConvenioActivity extends AppCompatActivity {

    private ImageButton editConvenio;
    private AEditText convenio;
    private Retrofit retrofit;
    private Utils utils;
    private RetrofitApiJson retrofitApiJson;
    private Bundle bundle;
    private ArrayList<Convenio> listado,data;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        bundle = getIntent().getExtras();
        editConvenio = (ImageButton) findViewById(R.id.editConvenio);
        convenio = (AEditText) findViewById(R.id.convenio);
        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiJson = retrofit.create(RetrofitApiJson.class);


        listado = new ArrayList<>();

        progressDialog = new ProgressDialog(ConvenioActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterConvenio(new ArrayList<Convenio>(),getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        convenio.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if(!convenio.getText().toString().equals("")){

                        ((RecyclerViewAdapterConvenio)mAdapter).getDataset().clear();

                        InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                        final GoRecuperarConvenio goClientes = new GoRecuperarConvenio();
                        goClientes.execute();

                    }else{
                        convenio.setError(getResources().getString(R.string.error_empty));
                    }
                }
                return false;
            }
        });


        editConvenio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!convenio.getText().toString().equals("")){

                    ((RecyclerViewAdapterConvenio)mAdapter).getDataset().clear();

                    InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    final GoRecuperarConvenio goClientes = new GoRecuperarConvenio();
                    goClientes.execute();

                }else{
                    convenio.setError(getResources().getString(R.string.error_empty));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConvenioActivity.this, PedidosActivity.class);
        intent.putExtra("cliente",bundle.getString("cliente"));
        intent.putExtra("convenio",bundle.getString("convenio"));
        intent.putExtra("producto",bundle.getString("producto"));
        intent.putExtra("cantidad",bundle.getDouble("cantidad"));

        intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
        intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
        intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
        intent.putExtra("objProducto",(Serializable) bundle.get("objProducto"));

        intent.putExtra("codConv",bundle.getString("codConv"));
        intent.putExtra("id",bundle.getInt("id"));
        //System.out.println("VALOR DE codConv "+bundle.getString("codConv"));


        startActivity(intent);
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


    private class GoRecuperarConvenio extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            recuperarConvenio();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void recuperarConvenio(){

            Call<ConvenioResponse> call = retrofitApiJson.getConvenio(convenio.getText().toString());
            call.enqueue(new Callback<ConvenioResponse>() {
                @Override
                public void onResponse(Call<ConvenioResponse> call, Response<ConvenioResponse> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        System.err.println(response.headers());
                        if(response.code() == 404){
                            utils.showInfo(getResources().getString(R.string.error_array_empty));
                        }else{
                            utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                        }
                    }else {
                        ConvenioResponse conveniosResponse = response.body();
                        ArrayList<Convenio> listado = new ArrayList<>();
                        listado.add(conveniosResponse.getObjConvenio());
                        if(listado.size()>0){
                            mAdapter = new RecyclerViewAdapterConvenio(listado,getApplicationContext());
                            mRecyclerView.setAdapter(mAdapter);
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            utils.showInfo(getResources().getString(R.string.error_array_empty));
                        }
                    }
                }
                @Override
                public void onFailure(Call<ConvenioResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    utils.showError("La Respuesta:" +t.getMessage());
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerViewAdapterConvenio) mAdapter).setOnItemClickListener(new RecyclerViewAdapterConvenio.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                TextView descrip = (TextView) v.findViewById(R.id.itemText);
                Intent intent = new Intent(ConvenioActivity.this,PedidosActivity.class);
                intent.putExtra("cliente",bundle.getString("cliente"));
                intent.putExtra("convenio",descrip.getText().toString());
                intent.putExtra("producto",bundle.getString(""));
                intent.putExtra("cantidad",bundle.getDouble("cantidad"));
                intent.putExtra("objBodega",(Serializable) (Bodega) bundle.get("objBodega"));
                intent.putExtra("objCliente", (Serializable) (Cliente) bundle.get("objCliente"));
                intent.putExtra("objConvenio", (Serializable) (Convenio)((RecyclerViewAdapterConvenio) mAdapter).getDataset().get(position));
                intent.putExtra("objProducto", (Serializable) (Producto) bundle.get("objProducto"));
                intent.putExtra("codConv",((RecyclerViewAdapterConvenio) mAdapter).getDataset().get(position).getCodConv());
                intent.putExtra("id",bundle.getInt("id"));
                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                startActivity(intent);
                finish();

            }
        });
    }

}
