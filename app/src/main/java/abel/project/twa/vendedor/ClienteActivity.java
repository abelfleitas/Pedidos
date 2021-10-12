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
import android.view.KeyEvent;
import android.view.Menu;
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
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.Cliente;
import abel.project.twa.vendedor.modelos.ClientesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteActivity extends AppCompatActivity {

    private ImageButton editCliente;
    private AEditText cliente;
    private Retrofit retrofit;
    private Utils utils;
    private RetrofitApiJson retrofitApiJson;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Cliente> listado,data;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        bundle = getIntent().getExtras();
        editCliente = (ImageButton) findViewById(R.id.editCliente);
        cliente = (AEditText) findViewById(R.id.cliente);
        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiJson = retrofit.create(RetrofitApiJson.class);

        listado = new ArrayList<>();

        progressDialog = new ProgressDialog(ClienteActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterCliente(new ArrayList<Cliente>(),getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


        cliente.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    if(!cliente.getText().toString().equals("")){

                        ((RecyclerViewAdapterCliente)mAdapter).getDataset().clear();

                        InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                        final GoRecuperarClientes goClientes = new GoRecuperarClientes();
                        goClientes.execute();

                    }else{
                        cliente.setError(getResources().getString(R.string.error_empty));
                    }

                }
                return false;
            }
        });


        editCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!cliente.getText().toString().equals("")){

                    ((RecyclerViewAdapterCliente)mAdapter).getDataset().clear();

                    InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    final GoRecuperarClientes goClientes = new GoRecuperarClientes();
                    goClientes.execute();

                }else{
                    cliente.setError(getResources().getString(R.string.error_empty));
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ClienteActivity.this, PedidosActivity.class);
        intent.putExtra("cliente",bundle.getString("cliente"));
        intent.putExtra("convenio",bundle.getString("convenio"));
        intent.putExtra("producto",bundle.getString("producto"));
        intent.putExtra("cantidad",bundle.getDouble("cantidad"));

        intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
        intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
        intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
        intent.putExtra("objProducto",(Serializable) bundle.get("objProducto"));
        intent.putExtra("id",bundle.getInt("id"));
        intent.putExtra("codConv",bundle.getString("codConv"));

        overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
        startActivity(intent);
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

    private class GoRecuperarClientes extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            recuperarClientes();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void recuperarClientes(){

            Call<ClientesResponse> call = retrofitApiJson.getClienteByAnyWhere(cliente.getText().toString());
            call.enqueue(new Callback<ClientesResponse>() {
                @Override
                public void onResponse(Call<ClientesResponse> call, Response<ClientesResponse> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    }else {
                        ClientesResponse  bodegasResponse = response.body();
                        ArrayList<Cliente> listado = new ArrayList<>();
                        for(Cliente cliente: bodegasResponse.getData() ){
                            listado.add(cliente);
                        }
                        if(listado.size()>0){
                            mAdapter = new RecyclerViewAdapterCliente(listado,getApplicationContext());
                            mRecyclerView.setAdapter(mAdapter);
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                            utils.showInfo(getResources().getString(R.string.error_array_empty));
                        }
                    }
                }
                @Override
                public void onFailure(Call<ClientesResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    utils.showError(t.getMessage());
                }
            });
        }
    }

    /*@Override
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
                        utils.showInfo(getResources().getString(R.string.error_array_empty));
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
                    mAdapter = new RecyclerViewAdapterCliente(data,getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    data = listado;
                    if (!data.isEmpty())
                    {
                        mAdapter = new RecyclerViewAdapterCliente(listado,getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    return false;
                }
            });
        }
        return true;
    }


    public ArrayList<Cliente> getSerachByKeyword(String s){
        ArrayList<Cliente> results = new ArrayList<>();
        for (Cliente cliente: listado){
            if(cliente.getCodClie().contains(s) || cliente.getCodClie().toLowerCase().contains(s) || cliente.getCodClie().toUpperCase().contains(s)||
                    cliente.getDescrip().contains(s) || cliente.getDescrip().toLowerCase().contains(s) || cliente.getDescrip().toUpperCase().contains(s)) {
                results.add(cliente);
            }
        }
        return results;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerViewAdapterCliente) mAdapter).setOnItemClickListener(new RecyclerViewAdapterCliente.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                TextView descrip = (TextView) v.findViewById(R.id.itemText1);
                Intent intent = new Intent(ClienteActivity.this,PedidosActivity.class);
                intent.putExtra("cliente",descrip.getText().toString());
                intent.putExtra("convenio",bundle.getString("convenio"));
                intent.putExtra("producto",bundle.getString("producto"));
                intent.putExtra("cantidad",bundle.getDouble("cantidad"));

                intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
                intent.putExtra("objCliente", (Serializable) ((RecyclerViewAdapterCliente) mAdapter).getDataset().get(position));
                intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                intent.putExtra("objProducto", (Serializable) bundle.get("objProducto"));

                intent.putExtra("codConv",bundle.getString("codConv"));
                intent.putExtra("id",bundle.getInt("id"));

                utils.getAux().setCliente(descrip.getText().toString());

                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                startActivity(intent);
                finish();

            }
        });
    }

}
