package abel.project.twa.vendedor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.infideap.stylishwidget.view.AButton;
import com.app.infideap.stylishwidget.view.AEditText;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.Bodega;
import abel.project.twa.vendedor.modelos.BodegasResponse;
import abel.project.twa.vendedor.modelos.Cliente;
import abel.project.twa.vendedor.modelos.Convenio;
import abel.project.twa.vendedor.modelos.Factor;
import abel.project.twa.vendedor.modelos.ItemLista;
import abel.project.twa.vendedor.modelos.ObjFactor;
import abel.project.twa.vendedor.modelos.ObjPrecio;
import abel.project.twa.vendedor.modelos.Precio;
import abel.project.twa.vendedor.modelos.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PedidosActivity extends AppCompatActivity {

    private Utils utils;
    private AEditText descripBodega,cliente,convenio,producto,cantidad;
    private Retrofit retrofit;
    private RetrofitApiJson retrofitApiJson;
    private ProgressDialog progressDialog;
    private Bundle bundle;
    private AButton save;
    ImageView limpiarConvenio,llimpiarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils  = new Utils(this);
        bundle = getIntent().getExtras();
        retrofit = new Retrofit.Builder()
                .baseUrl(utils.getAux().getUrlBase())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitApiJson = retrofit.create(RetrofitApiJson.class);

        progressDialog = new ProgressDialog(PedidosActivity.this,R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));

        descripBodega = (AEditText) findViewById(R.id.bodega);
        cliente = (AEditText) findViewById(R.id.cliente);
        convenio = (AEditText) findViewById(R.id.convenio);
        producto = (AEditText) findViewById(R.id.producto);
        cantidad = (AEditText) findViewById(R.id.cantidad);

        save = (AButton) findViewById(R.id.btnGuardarPedido);
        limpiarConvenio = (ImageView) findViewById(R.id.limpiarConvenio);
        llimpiarProducto = (ImageView) findViewById(R.id.limpiarProducto);

        getWindow().getDecorView().clearFocus();

        limpiarConvenio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convenio.setText("");
                //utils.getAux().setConvenio(false);
            }
        });

        llimpiarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                producto.setText("");
            }
        });

        if(utils.getAux().isEdit()){
            getSupportActionBar().setTitle(getResources().getString(R.string.is_edit));
            save.setText(getResources().getString(R.string.edit));
            save.setBackgroundColor(getResources().getColor(R.color.color_type_success));
        }

        if(((Bodega)bundle.get("objBodega")) == null){
            descripBodega.setText(utils.getMap().getSelectedBodega(utils.getMap().getIdUser()));
        }else {
            descripBodega.setText(((Bodega)bundle.get("objBodega")).getDescrip());
        }
        cliente.setText(bundle.getString("cliente"));
        convenio.setText(bundle.getString("convenio"));
        producto.setText(bundle.getString("producto"));
        if(bundle.getString("producto") !=null && !bundle.getString("producto").isEmpty()){
            cantidad.requestFocus();
            //cantidad.requestFocusFromTouch();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }




        if(bundle.getDouble("cantidad") != 0){
            cantidad.setText(String.valueOf(bundle.getDouble("cantidad")));
        }else{
            cantidad.setText("");
        }
        descripBodega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final GoSeleccionarBodega goBodegas = new GoSeleccionarBodega();
                goBodegas.execute();
            }
        });
        cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(utils.getAux().getCliente().equals("")){

                    Intent intent = new Intent(PedidosActivity.this, ClienteActivity.class);
                    intent.putExtra("cliente",cliente.getText().toString());
                    intent.putExtra("convenio",convenio.getText().toString());
                    intent.putExtra("producto",producto.getText().toString());
                    if(!cantidad.getText().toString().isEmpty()){
                        intent.putExtra("cantidad",Double.parseDouble(cantidad.getText().toString()));
                    }else {
                        intent.putExtra("cantidad",0);
                    }
                    intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
                    intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
                    intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                    intent.putExtra("objProducto",(Serializable) bundle.get("objProducto"));
                    intent.putExtra("id",bundle.getInt("id"));

                    overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
                    startActivity(intent);
                    finish();
                }else{
                    utils.showInfo(getResources().getString(R.string.client_block));
                    cantidad.clearFocus();
                }
            }
        });
        convenio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cliente.getText().toString().isEmpty()){
                    Intent intent = new Intent(PedidosActivity.this, ConvenioActivity.class);
                    intent.putExtra("cliente",cliente.getText().toString());
                    intent.putExtra("convenio",convenio.getText().toString());
                    intent.putExtra("producto",producto.getText().toString());
                    if(!cantidad.getText().toString().isEmpty()){
                        intent.putExtra("cantidad",Double.parseDouble(cantidad.getText().toString()));
                    }else {
                        intent.putExtra("cantidad",0);
                    }

                    intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
                    intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
                    intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                    intent.putExtra("objProducto",(Serializable) bundle.get("objProducto"));

                    intent.putExtra("codConv",bundle.getString("codConv"));
                    intent.putExtra("id",bundle.getInt("id"));

                    overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
                    startActivity(intent);
                    finish();
                }else{
                    utils.showInfo(getResources().getString(R.string.client_first));
                    cantidad.clearFocus();
                }

            }
        });
        producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cliente.getText().toString().isEmpty()){

                    Intent intent = new Intent(PedidosActivity.this, ProductoActivity.class);
                    intent.putExtra("codigo_barra","");
                    intent.putExtra("cliente",cliente.getText().toString());
                    intent.putExtra("convenio",convenio.getText().toString());
                    intent.putExtra("producto",producto.getText().toString());
                    if(!cantidad.getText().toString().isEmpty()){
                        intent.putExtra("cantidad",Double.parseDouble(cantidad.getText().toString()));
                    }else {
                        intent.putExtra("cantidad",0);
                    }

                    intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
                    intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
                    intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                    intent.putExtra("objProducto",(Serializable) bundle.get("objProducto"));

                    intent.putExtra("codConv",bundle.getString("codConv"));
                    intent.putExtra("id",bundle.getInt("id"));

                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
                finish();
                }else{
                    utils.showInfo(getResources().getString(R.string.client_first));
                    cantidad.clearFocus();
                }
            }
        });

        cantidad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == true && producto.getText().toString().equals("")){
                    utils.showInfo(getResources().getString(R.string.product_first));
                    view.clearFocus();
                }
            }
        });

        cantidad.setOnKeyListener(new View.OnKeyListener() {
            @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!validate(descripBodega.getText().toString(), cliente.getText().toString(), producto.getText().toString(), cantidad.getText().toString())) {
                        return false;
                    }
                    else {
                        final GoSeleccionarFactor goBodegas = new GoSeleccionarFactor();
                        goBodegas.execute();
                    }
                }
                return false;
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate(descripBodega.getText().toString(), cliente.getText().toString(), producto.getText().toString(), cantidad.getText().toString())) {
                    return;
                }
                else {
                    final GoSeleccionarFactor goBodegas = new GoSeleccionarFactor();
                    goBodegas.execute();
                }
            }
        });
    }


    public boolean validate(String pbodega,String pcliente,String pproducto,String pcantidad){
        boolean valid = true;

        if (pbodega.isEmpty()) {
            descripBodega.setError(getResources().getString(R.string.error_empty));
            utils.showError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else if(pcliente.isEmpty()) {
            cliente.setError(getResources().getString(R.string.error_empty));
            utils.showError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else if(pproducto.isEmpty()) {
            producto.setError(getResources().getString(R.string.error_empty));
            utils.showError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else if(pcantidad.isEmpty()) {
            cantidad.setError(getResources().getString(R.string.error_empty));
            valid = false;
        }
        else if((!pcantidad.isEmpty()) && !isDecimal(pcantidad)) {
            cantidad.setError(getResources().getString(R.string.num_integer));
            valid = false;
        }
        else{
            descripBodega.setError(null);
            cliente.setError(null);
            convenio.setError(null);
            producto.setError(null);
            cantidad.setError(null);
        }
        return valid;
    }

    public boolean isDecimal(String cantidad){
        try{
            double aux = Double.parseDouble(cantidad);
            if(aux>0){
                return true;
            }else{
                return false;
            }
        }catch (NumberFormatException e){
            return  false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        utils.getAux().setEditar(false);
        if(utils.getMap().getListaPedidos().size() == 0){
            utils.getAux().setCliente("");
            //utils.getAux().setClienteTextConValor(false);
        }
        Intent intent = new Intent(PedidosActivity.this, PrincipalActivity.class);
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

    private class GoSeleccionarBodega extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... sUrl) {
            recopilarBodegas();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void recopilarBodegas() {

            Call<BodegasResponse> call = retrofitApiJson.getBodegas();
            call.enqueue(new Callback<BodegasResponse>() {
                @Override
                public void onResponse(Call<BodegasResponse> call, Response<BodegasResponse> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    }else {
                        BodegasResponse lista = response.body();
                        ArrayList<String> listado = new ArrayList<>();
                        for(Bodega bodega: lista.getData()){
                            listado.add(bodega.getDescrip());
                        }
                        progressDialog.dismiss();
                        Intent intent = new Intent(PedidosActivity.this, EditBodegaActivity.class);
                        intent.putExtra("bodegas", listado);
                        intent.putExtra("cliente",cliente.getText().toString());
                        intent.putExtra("convenio",convenio.getText().toString());
                        intent.putExtra("producto",producto.getText().toString());
                        if(!cantidad.getText().toString().isEmpty()){
                            intent.putExtra("cantidad",(double) Double.parseDouble(cantidad.getText().toString()));
                        }else {
                            intent.putExtra("cantidad",(double) 0);
                        }
                        intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
                        intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
                        intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                        intent.putExtra("objProducto",(Serializable) bundle.get("objProducto"));
                        intent.putExtra("id",bundle.getInt("id"));

                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
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

    private class GoSeleccionarFactor extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {
            recopilarFactor();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void recopilarFactor() {

            if(utils.getAux().isEdit()){

                Call<ObjFactor> call = retrofitApiJson.getFactor();
                call.enqueue(new Callback<ObjFactor>() {
                    @Override
                    public void onResponse(Call<ObjFactor> call, Response<ObjFactor> response) {
                        if(!response.isSuccessful()){
                            progressDialog.dismiss();
                            utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                        }else
                        {

                            final ObjFactor objFactor = response.body();
                            final Bodega objBodega = (Bodega) bundle.get("objBodega");
                            final Cliente objCliente = (Cliente) bundle.get("objCliente");
                            final Convenio objConvenio = (Convenio) bundle.get("objConvenio");
                            final Producto objProducto = (Producto) bundle.get("objProducto");
                            if(convenio.getText().toString().isEmpty()){

                                BigDecimal factor = new BigDecimal(objFactor.getObjFactor().getFactor());
                                BigDecimal cant = new BigDecimal(cantidad.getText().toString());
                                BigDecimal iva = new BigDecimal(objProducto.getImpuesto());


                                BigDecimal precioInicial,precio;

                                if(utils.getMap().getNomProductoById(bundle.getInt("id")).equals(objProducto.getDescrip())){

                                    precioInicial = new BigDecimal(utils.getMap().getPrecioInicialById(bundle.getInt("id")));
                                    precio = precioInicial;

                                }else{
                                    precioInicial = new BigDecimal(objProducto.getPrecio1());
                                    precio = precioInicial;
                                }

                                if(iva.doubleValue() != 0){
                                    BigDecimal aux = precio.multiply(iva);
                                    BigDecimal aux1 = aux.divide(BigDecimal.valueOf(100),9,RoundingMode.HALF_EVEN);
                                    precio = aux1.add(precio);
                                }

                                BigDecimal total = cant.multiply(precio);
                                BigDecimal tasa =  precio.divide(factor,9,RoundingMode.HALF_EVEN);
                                BigDecimal tasaTotal = total.divide(factor,9,RoundingMode.HALF_EVEN);

                                utils.getAux().setFactor(factor.floatValue());

                                /*System.err.println(
                                    "id  "+ bundle.getInt("id")+"\n"+
                                    "producto  " + objProducto.getDescrip()+"\n"+
                                    "codigo  " + objProducto.getCodProd()+"\n"+
                                    "cantidad  " + cant.doubleValue()+"\n"+
                                    "unidad  " + objProducto.getUnidad()+"\n"+
                                    "precio  " + precio.doubleValue()+"\n"+
                                    "tasa_precio  " + tasa.doubleValue()+"\n"+
                                    "total  " + total.doubleValue()+"\n"+
                                    "tasa_total  " + tasaTotal.doubleValue()+"\n"+
                                    "codbodega  " +  objBodega.getCodUbic()+"\n"+
                                    "nombodega  " + objBodega.getDescrip()+"\n"+
                                    "codcliente  " + objCliente.getCodClie()+"\n"+
                                    "nomcliente  " + objCliente.getDescrip()+"\n"+
                                    "codConv  " + objConvenio.getCodConv()+"\n"+
                                    "convenio " + objConvenio.getDescrip()+"\n"+
                                    "iva  " + iva.doubleValue()+"\n");*/

                                //System.err.println("El id desde pedidos es : "+bundle.getInt("id"));



                                utils.getMap().editarProducto(
                                        objCliente.getCodClie(),
                                        objCliente.getDescrip(),
                                        objBodega.getCodUbic(),
                                        objBodega.getDescrip(),
                                        "",
                                        "",
                                        objProducto.getCodProd(),
                                        objProducto.getDescrip(),
                                        cant.doubleValue(),
                                        objProducto.getUnidad(),
                                        precio.doubleValue(),
                                        total.doubleValue(),
                                        tasa.doubleValue(),
                                        tasaTotal.doubleValue(),
                                        iva.doubleValue(),
                                        bundle.getInt("id"),
                                        precioInicial.doubleValue());



                                progressDialog.dismiss();
                                onBackPressed();


                            }else{

                                //System.err.println("Entre por convenio lleno");

                                Call<ObjPrecio> call1 = retrofitApiJson.getPrecioVarianteConvenio(objProducto.getCodProd(),objConvenio.getCodConv(),cantidad.getText().toString());
                                call1.enqueue(new Callback<ObjPrecio>() {
                                    @Override
                                    public void onResponse(Call<ObjPrecio> call, Response<ObjPrecio> response) {
                                        if (!response.isSuccessful()) {
                                            progressDialog.dismiss();
                                            if (response.code() == 404) {
                                                utils.showInfo(getResources().getString(R.string.error_array_empty));
                                            } else {
                                                utils.showError(new Errores("" + response.code(), getApplicationContext()).codeErrors());
                                            }
                                        } else {

                                            ObjPrecio objPrecio = response.body();

                                            BigDecimal factor = new BigDecimal(objFactor.getObjFactor().getFactor());
                                            BigDecimal cant = new BigDecimal(cantidad.getText().toString());
                                            BigDecimal iva = new BigDecimal(objProducto.getImpuesto());


                                            BigDecimal precioInicial,precio;

                                            if(utils.getMap().getNomProductoById(bundle.getInt("id")).equals(objProducto.getDescrip())){

                                                precioInicial = new BigDecimal(utils.getMap().getPrecioInicialById(bundle.getInt("id")));
                                                precio = precioInicial;

                                            }else{
                                                precioInicial = new BigDecimal(objProducto.getPrecio1());
                                                precio = precioInicial;
                                            }

                                            if (Double.parseDouble(objPrecio.getObjPrecio().getPrecio()) != 0) {
                                                precio = new BigDecimal(objPrecio.getObjPrecio().getPrecio());
                                                precioInicial = new BigDecimal(objPrecio.getObjPrecio().getPrecio());
                                            }

                                            if (iva.doubleValue() != 0) {
                                                BigDecimal aux = precio.multiply(iva);
                                                BigDecimal aux1 = aux.divide(BigDecimal.valueOf(100), 9, RoundingMode.HALF_EVEN);
                                                precio = aux1.add(precio);
                                            }

                                            BigDecimal total = cant.multiply(precio);
                                            BigDecimal tasa = precio.divide(factor, 9, RoundingMode.HALF_EVEN);
                                            BigDecimal tasaTotal = total.divide(factor, 9, RoundingMode.HALF_EVEN);

                                            utils.getAux().setFactor(factor.floatValue());

                                           /* System.err.println(
                                                    "id  "+ bundle.getInt("id")+"\n"+
                                                            "producto  " + objProducto.getDescrip()+"\n"+
                                                            "codigo  " + objProducto.getCodProd()+"\n"+
                                                            "cantidad  " + cant.doubleValue()+"\n"+
                                                            "unidad  " + objProducto.getUnidad()+"\n"+
                                                            "precio  " + precio.doubleValue()+"\n"+
                                                            "tasa_precio  " + tasa.doubleValue()+"\n"+
                                                            "total  " + total.doubleValue()+"\n"+
                                                            "tasa_total  " + tasaTotal.doubleValue()+"\n"+
                                                            "codbodega  " +  objBodega.getCodUbic()+"\n"+
                                                            "nombodega  " + objBodega.getDescrip()+"\n"+
                                                            "codcliente  " + objCliente.getCodClie()+"\n"+
                                                            "nomcliente  " + objCliente.getDescrip()+"\n"+
                                                            "codConv  " + objConvenio.getCodConv()+"\n"+
                                                            "convenio " + objConvenio.getDescrip()+"\n"+
                                                            "iva  " + iva.doubleValue()+"\n");*/

                                            utils.getMap().editarProducto(
                                                    objCliente.getCodClie(),
                                                    objCliente.getDescrip(),
                                                    objBodega.getCodUbic(),
                                                    objBodega.getDescrip(),
                                                    objConvenio.getCodConv(),
                                                    objConvenio.getDescrip(),
                                                    objProducto.getCodProd(),
                                                    objProducto.getDescrip(),
                                                    cant.doubleValue(),
                                                    objProducto.getUnidad(),
                                                    precio.doubleValue(),
                                                    total.doubleValue(),
                                                    tasa.doubleValue(),
                                                    tasaTotal.doubleValue(),
                                                    iva.doubleValue(),
                                                    bundle.getInt("id"),
                                                    precioInicial.doubleValue());

                                            progressDialog.dismiss();
                                            onBackPressed();

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ObjPrecio> call1, Throwable t) {
                                        progressDialog.dismiss();
                                        utils.showError(t.getMessage());
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ObjFactor> call, Throwable t) {
                        progressDialog.dismiss();
                        utils.showError(t.getMessage());
                    }
                });

            }
            else{

                Call<ObjFactor> call = retrofitApiJson.getFactor();
                call.enqueue(new Callback<ObjFactor>() {
                    @Override
                    public void onResponse(Call<ObjFactor> call, Response<ObjFactor> response) {
                        if(!response.isSuccessful()){
                            progressDialog.dismiss();
                            utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                        }else
                        {

                            final ObjFactor objFactor = response.body();
                            final Bodega objBodega = (Bodega) bundle.get("objBodega");
                            final Cliente objCliente = (Cliente) bundle.get("objCliente");
                            final Convenio objConvenio = (Convenio) bundle.get("objConvenio");
                            final Producto objProducto = (Producto) bundle.get("objProducto");

                            if(convenio.getText().toString().isEmpty()){

                                BigDecimal factor = new BigDecimal(objFactor.getObjFactor().getFactor());
                                BigDecimal cant = new BigDecimal(cantidad.getText().toString());
                                BigDecimal iva = new BigDecimal(objProducto.getImpuesto());
                                BigDecimal precio = new BigDecimal(objProducto.getPrecio1());

                                BigDecimal precioInicial = new BigDecimal(objProducto.getPrecio1());

                                if(iva.doubleValue() != 0){
                                    BigDecimal aux = precio.multiply(iva);
                                    BigDecimal aux1 = aux.divide(BigDecimal.valueOf(100),9,RoundingMode.HALF_EVEN);
                                    precio = aux1.add(precio);
                                }

                                BigDecimal total = cant.multiply(precio);
                                BigDecimal tasa =  precio.divide(factor,9,RoundingMode.HALF_EVEN);
                                BigDecimal tasaTotal = total.divide(factor,9,RoundingMode.HALF_EVEN);

                                utils.getAux().setFactor(factor.floatValue());

                                utils.getMap().insertarProducto(
                                        objCliente.getCodClie(),
                                        objCliente.getDescrip(),
                                        objBodega.getCodUbic(),
                                        objBodega.getDescrip(),
                                        "",
                                        "",
                                        objProducto.getCodProd(),
                                        objProducto.getDescrip(),
                                        cant.doubleValue(),
                                        objProducto.getUnidad(),
                                        precio.doubleValue(),
                                        total.doubleValue(),
                                        tasa.doubleValue(),
                                        tasaTotal.doubleValue(),
                                        iva.doubleValue(),
                                        precioInicial.doubleValue());

                                convenio.setText(null);
                                producto.setText(null);
                                cantidad.setText(null);
                                cantidad.clearFocus();
                                progressDialog.dismiss();
                                utils.showSatisfactorio(getResources().getString(R.string.successes_add_pedido));

                            }else{
                                Call<ObjPrecio> call1 = retrofitApiJson.getPrecioVarianteConvenio(objProducto.getCodProd(),objConvenio.getCodConv(),cantidad.getText().toString());
                                call1.enqueue(new Callback<ObjPrecio>() {
                                    @Override
                                    public void onResponse(Call<ObjPrecio> call, Response<ObjPrecio> response) {
                                        if(!response.isSuccessful()){
                                            progressDialog.dismiss();
                                            if(response.code() == 404){
                                                utils.showInfo(getResources().getString(R.string.error_array_empty));
                                            }
                                            else
                                            {
                                                utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                                            }
                                        }
                                        else
                                        {

                                            ObjPrecio objPrecio = response.body();

                                            BigDecimal factor = new BigDecimal(objFactor.getObjFactor().getFactor());
                                            BigDecimal cant = new BigDecimal(cantidad.getText().toString());
                                            BigDecimal iva = new BigDecimal(objProducto.getImpuesto());
                                            BigDecimal precio = new BigDecimal(objProducto.getPrecio1());

                                            BigDecimal precioInicial = new BigDecimal(objProducto.getPrecio1());

                                            if(Double.parseDouble(objPrecio.getObjPrecio().getPrecio()) != 0)
                                            {
                                                precio = new BigDecimal(objPrecio.getObjPrecio().getPrecio());
                                                precioInicial = new BigDecimal(objPrecio.getObjPrecio().getPrecio());
                                            }

                                            if(iva.doubleValue() != 0){
                                                BigDecimal aux = precio.multiply(iva);
                                                BigDecimal aux1 = aux.divide(BigDecimal.valueOf(100),9,RoundingMode.HALF_EVEN);
                                                precio = aux1.add(precio);
                                            }

                                            BigDecimal total = cant.multiply(precio);
                                            BigDecimal tasa =  precio.divide(factor,9,RoundingMode.HALF_EVEN);
                                            BigDecimal tasaTotal = total.divide(factor,9,RoundingMode.HALF_EVEN);

                                            utils.getAux().setFactor(factor.floatValue());

                                            utils.getMap().insertarProducto(
                                                    objCliente.getCodClie(),
                                                    objCliente.getDescrip(),
                                                    objBodega.getCodUbic(),
                                                    objBodega.getDescrip(),
                                                    objConvenio.getCodConv(),
                                                    objConvenio.getDescrip(),
                                                    objProducto.getCodProd(),
                                                    objProducto.getDescrip(),
                                                    cant.doubleValue(),
                                                    objProducto.getUnidad(),
                                                    precio.doubleValue(),
                                                    total.doubleValue(),
                                                    tasa.doubleValue(),
                                                    tasaTotal.doubleValue(),
                                                    iva.doubleValue(),
                                                    precioInicial.doubleValue());

                                            convenio.setText(null);
                                            producto.setText(null);
                                            cantidad.setText(null);
                                            cantidad.clearFocus();
                                            progressDialog.dismiss();
                                            utils.showSatisfactorio(getResources().getString(R.string.successes_add_pedido));
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ObjPrecio> call1, Throwable t) {
                                        progressDialog.dismiss();
                                        utils.showError(t.getMessage());
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ObjFactor> call, Throwable t) {
                        progressDialog.dismiss();
                        utils.showError(t.getMessage());
                    }
                });

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {

        super.onPostResume();
    }


}

