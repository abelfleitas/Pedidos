package abel.project.twa.vendedor;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import java.io.Serializable;
import java.util.ArrayList;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RecyclerViewAdapter;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.BodegasResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditBodegaActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Bundle bundle;
    private ArrayList<String> data;
    private Utils utils;
    private Retrofit retrofit;
    private RetrofitApiJson retrofitApiJson;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bodega);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        bundle = getIntent().getExtras();
        data = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(bundle.getStringArrayList("bodegas"),getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiJson = retrofit.create(RetrofitApiJson.class);

        progressDialog = new ProgressDialog(EditBodegaActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditBodegaActivity.this, PedidosActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerViewAdapter) mAdapter).setOnItemClickListener(new RecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                TextView descrip = (TextView) v.findViewById(R.id.itemText1);
                final GoSaveBodega goBodegas = new GoSaveBodega(descrip.getText().toString());
                goBodegas.execute();
            }
        });
    }



    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    data = getSerachByKeyword(s);
                    if (data.isEmpty())
                    {
                        utils.showInfo("La búsqueda no ha encontrado ningún resultado");
                    }
                    else
                    {
                        if(data.size() == 1)
                        {
                            utils.showSatisfactorio(data.size() + " resultado encontrado");
                        }
                        else{
                            utils.showSatisfactorio(data.size() + " resultados encontrados");
                        }
                    }
                    mAdapter = new RecyclerViewAdapter(data,getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    data = bundle.getStringArrayList("bodegas");
                    if (!data.isEmpty())
                    {
                        mAdapter = new RecyclerViewAdapter(data,getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    return false;
                }
            });
        }
        return true;
    }

    public ArrayList<String> getSerachByKeyword(String s){
        ArrayList<String> results = new ArrayList<>();
        for (String cadena: bundle.getStringArrayList("bodegas")){
            if(cadena.contains(s) || cadena.toLowerCase().contains(s) || cadena.toUpperCase().contains(s)){
                results.add(cadena);
            }
        }
        return results;
    }


    private class GoSaveBodega extends AsyncTask<String, Integer, String> {

        private String DescripBodega;

        public GoSaveBodega(String descripBodega) {
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
                        Intent intent = new Intent(EditBodegaActivity.this, PedidosActivity.class);
                        intent.putExtra("cliente",bundle.getString("cliente"));
                        intent.putExtra("convenio",bundle.getString("convenio"));
                        intent.putExtra("producto",bundle.getString("producto"));
                        intent.putExtra("cantidad",bundle.getDouble("cantidad"));

                        intent.putExtra("objBodega", (Serializable) bodegasResponse.getData().get(0));
                        intent.putExtra("objCliente",(Serializable)  bundle.get("objCliente"));
                        intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                        intent.putExtra("objProducto", (Serializable) bundle.get("objProducto"));

                        intent.putExtra("codConv",bundle.getString("codConv"));

                        intent.putExtra("id",bundle.getInt("id"));

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
