package abel.project.twa.vendedor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.app.infideap.stylishwidget.view.AEditText;
import com.rey.material.widget.CheckedImageView;
import com.rey.material.widget.ImageButton;
import java.io.Serializable;
import java.util.ArrayList;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RecyclerViewAdapterProducto;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.barcodescanner.FullScannerFragmentActivity;
import abel.project.twa.vendedor.modelos.Bodega;
import abel.project.twa.vendedor.modelos.Cliente;
import abel.project.twa.vendedor.modelos.Convenio;
import abel.project.twa.vendedor.modelos.Producto;
import abel.project.twa.vendedor.modelos.ProductoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductoActivity extends AppCompatActivity {

    private Bundle bundle;
    private Retrofit retrofit;
    private Utils utils;
    private RetrofitApiJson retrofitApiJson;
    private ImageButton editProducto;
    private CheckedImageView scan;
    private AEditText producto;
    private ProgressDialog progressDialog;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    //private ArrayList<Producto> listado,data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        bundle = getIntent().getExtras();
        scan = (CheckedImageView) findViewById(R.id.scanner);
        editProducto = (ImageButton) findViewById(R.id.editProducto);
        producto = (AEditText) findViewById(R.id.producto);
        producto.setText(bundle.getString("codigo_barra"));

        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitApiJson = retrofit.create(RetrofitApiJson.class);

        progressDialog = new ProgressDialog(ProductoActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapterProducto(new ArrayList<Producto>(),getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        producto.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if(!producto.getText().toString().equals("")){

                        ((RecyclerViewAdapterProducto)mAdapter).getDataset().clear();

                        InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                        final GoRecuperarProducto goProducto = new GoRecuperarProducto();
                        goProducto.execute();

                    }else{
                        producto.setError(getResources().getString(R.string.error_empty));
                    }
                }
                return false;
            }
        });

        editProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!producto.getText().toString().equals("")){

                    ((RecyclerViewAdapterProducto)mAdapter).getDataset().clear();

                    InputMethodManager imn = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imn.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                    final GoRecuperarProducto goProducto = new GoRecuperarProducto();
                    goProducto.execute();

                }else{
                    producto.setError(getResources().getString(R.string.error_empty));
                }
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductoActivity.this, FullScannerFragmentActivity.class);
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
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductoActivity.this, PedidosActivity.class);
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

    private class GoRecuperarProducto extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            recuperarProductos();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void recuperarProductos(){


            if(bundle.getString("convenio").isEmpty()){

                Call<ProductoResponse> call = retrofitApiJson.getProducto(producto.getText().toString());
                call.enqueue(new Callback<ProductoResponse>() {
                    @Override
                    public void onResponse(Call<ProductoResponse> call, Response<ProductoResponse> response) {
                        if(!response.isSuccessful()){
                            progressDialog.dismiss();
                            if(response.code() == 404){
                                utils.showInfo(getResources().getString(R.string.error_array_empty));
                            }else{
                                utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                            }
                        }else {
                            ProductoResponse productosResponse = response.body();
                            ArrayList<Producto> listado = new ArrayList<>();
                            listado.add(productosResponse.getObjProducto());

                            if(listado.size()>0){
                                mAdapter = new RecyclerViewAdapterProducto(listado,getApplicationContext());
                                mRecyclerView.setAdapter(mAdapter);
                                progressDialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                utils.showInfo(getResources().getString(R.string.error_array_empty));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ProductoResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        utils.showError(t.getMessage());
                    }
                });

            }else{

                System.err.println("El convenio parabuscar el producto es: "+bundle.getString("codConv"));

                Call<ProductoResponse> call = retrofitApiJson.getProductoWhitConvenio(producto.getText().toString(),bundle.getString("codConv"));
                call.enqueue(new Callback<ProductoResponse>() {
                    @Override
                    public void onResponse(Call<ProductoResponse> call, Response<ProductoResponse> response) {
                        if(!response.isSuccessful()){
                            progressDialog.dismiss();
                            if(response.code() == 404){
                                utils.showInfo(getResources().getString(R.string.error_array_empty));
                            }else{
                                utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                            }
                        }else {
                            ProductoResponse productosResponse = response.body();
                            ArrayList<Producto> listado = new ArrayList<>();
                            listado.add(productosResponse.getObjProducto());

                            if(listado.size()>0){
                                mAdapter = new RecyclerViewAdapterProducto(listado,getApplicationContext());
                                mRecyclerView.setAdapter(mAdapter);
                                progressDialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                utils.showInfo(getResources().getString(R.string.error_array_empty));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ProductoResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        utils.showError(t.getMessage());
                    }
                });
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((RecyclerViewAdapterProducto) mAdapter).setOnItemClickListener(new RecyclerViewAdapterProducto.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                TextView descrip = (TextView) v.findViewById(R.id.itemText);
                Intent intent = new Intent(ProductoActivity.this,PedidosActivity.class);
                intent.putExtra("cliente",bundle.getString("cliente"));
                intent.putExtra("convenio",bundle.getString("convenio"));
                intent.putExtra("producto",descrip.getText().toString());
                intent.putExtra("cantidad",bundle.getDouble("cantidad"));

                intent.putExtra("objBodega",(Serializable) (Bodega) bundle.get("objBodega"));
                intent.putExtra("objCliente",(Serializable) (Cliente) bundle.get("objCliente"));
                intent.putExtra("objConvenio",(Serializable) (Convenio) bundle.get("objConvenio"));
                intent.putExtra("objProducto", (Serializable)(Producto) ((RecyclerViewAdapterProducto) mAdapter).getDataset().get(position));

                intent.putExtra("codConv",bundle.getString("codConv"));
                intent.putExtra("id",bundle.getInt("id"));

                //utils.getAux().setClienteTextConValor(true);

                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                startActivity(intent);
                finish();

            }
        });
    }
}
