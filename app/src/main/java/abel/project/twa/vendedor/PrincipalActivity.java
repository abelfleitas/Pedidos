package abel.project.twa.vendedor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import com.app.infideap.stylishwidget.view.ATextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.rey.material.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import abel.project.twa.vendedor.auxiliar.Errores;
import abel.project.twa.vendedor.auxiliar.ItemsDataAdapter;
import abel.project.twa.vendedor.auxiliar.RetrofitApiJson;
import abel.project.twa.vendedor.auxiliar.Utils;
import abel.project.twa.vendedor.modelos.Bodega;
import abel.project.twa.vendedor.modelos.Cliente;
import abel.project.twa.vendedor.modelos.Convenio;
import abel.project.twa.vendedor.modelos.FacturaResponse;
import abel.project.twa.vendedor.modelos.ItemLista;
import abel.project.twa.vendedor.auxiliar.SwipeController;
import abel.project.twa.vendedor.auxiliar.SwipeControllerActions;
import abel.project.twa.vendedor.modelos.ObjPedido;
import abel.project.twa.vendedor.modelos.Producto;
import abel.project.twa.vendedor.modelos.ProductoAux;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Utils utils;
    private ItemsDataAdapter adp=null;
    private SwipeController swipeController=null;
    private Bodega bodega;
    private TextView factor,cant_art,total_factor,total_precio;
    private ArrayList<ItemLista> items;
    private MenuItem delete,save;
    private ATextView cliente;
    private TelephonyManager manager;

    private Retrofit retrofit;
    private RetrofitApiJson retrofitApiJson;
    private  Call<FacturaResponse> mcall;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(this);
        if (utils.checkInternetConnection()) {
            setContentView(R.layout.activity_principal);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            String user = utils.getMap().getIdUser();
            String codUbic = utils.getMap().getSelectedCodBodega(user);
            String descripBodega = utils.getMap().getSelectedBodega(user);
            bodega = new Bodega(codUbic,descripBodega,1);

            cliente = (ATextView) findViewById(R.id.cliente);
            factor = (TextView) findViewById(R.id.valorFactor);
            cant_art=(TextView) findViewById(R.id.cantidadArtValue);
            total_factor = (TextView) findViewById(R.id.factortotal);
            total_precio = (TextView) findViewById(R.id.currencyTotal);

            progressDialog = new ProgressDialog(PrincipalActivity.this,R.style.MyDialogTheme);
            progressDialog.setIndeterminate(false);
            progressDialog.setIcon(getResources().getDrawable(R.drawable.ib));
            progressDialog.setTitle(getResources().getString(R.string.app_name));
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.process));
            progressDialog.setButton(getResources().getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mcall.cancel();
                    dialogInterface.dismiss();
                }
            });


            retrofit = new Retrofit.Builder()
                    .baseUrl(utils.getAux().getUrlBase())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitApiJson = retrofit.create(RetrofitApiJson.class);


            FloatingActionButton add = (FloatingActionButton) findViewById(R.id.fab);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(PrincipalActivity.this, PedidosActivity.class);

                    if(adp.items.size()>0){
                        intent.putExtra("cliente",adp.items.get(0).getCliente());
                        intent.putExtra("objCliente", new Cliente(adp.items.get(0).getCodClie(),adp.items.get(0).getCliente()));
                    }
                    else {
                        intent.putExtra("cliente","");
                        intent.putExtra("objCliente",(Serializable) null);
                    }

                    intent.putExtra("convenio","");
                    intent.putExtra("producto","");
                    intent.putExtra("cantidad",0);
                    intent.putExtra("objBodega",(Serializable) bodega);
                    intent.putExtra("objConvenio",(Serializable) null);
                    intent.putExtra("objProducto", (Serializable) null);

                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                    startActivity(intent);
                    finish();

                }
            });

            items = utils.getMap().getListaPedidos();
            adp = new ItemsDataAdapter(items);
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adp);

            llenarVista();

            swipeController = new SwipeController(new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    utils.getMap().eliminarProductoPedido(items.get(position).getId());
                    adp.items.remove(position);
                    adp.notifyItemRemoved(position);
                    adp.notifyItemRangeChanged(position, adp.getItemCount());
                    if(items.size() == 0){
                        utils.getAux().setFactor(0);
                        delete.setVisible(false);
                        save.setVisible(false);
                        utils.getAux().setCliente("");
                    }
                    llenarVista();
                    utils.showSatisfactorio(getResources().getString(R.string.delete_prod));
                }
                @Override
                public void onLeftClicked(int position){
                    Intent intent = new Intent(PrincipalActivity.this, PedidosActivity.class);
                    overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);

                    //System.err.println("El id desde principal es : "+items.get(position).getId());

                    ItemLista aux = utils.getMap().getItemPedidos(items.get(position).getId());

                    intent.putExtra("id",aux.getId());
                    intent.putExtra("cliente",aux.getCliente());
                    intent.putExtra("convenio",aux.getConvenio());
                    intent.putExtra("producto",aux.getProducto());
                    intent.putExtra("cantidad",aux.getCantidad());

                    intent.putExtra("objBodega",(Serializable) (Bodega) new Bodega(aux.getCodbodega(),aux.getNombodega(),1));
                    intent.putExtra("objCliente",(Serializable) (Cliente) new Cliente(aux.getCodClie(),aux.getCliente()));
                    intent.putExtra("objConvenio",(Serializable) (Convenio) new Convenio(aux.getCodConv(),aux.getConvenio()));
                    intent.putExtra("objProducto",(Serializable) (Producto) new Producto(aux.getCodigo(),aux.getProducto(),String.valueOf(aux.getPrecio()),aux.getUnidad(),String.valueOf(aux.getIva())));

                    intent.putExtra("codConv",aux.getCodConv());


                    utils.getAux().setEditar(true);
                    startActivity(intent);
                    finish();
                }

            });
            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(recyclerView);

            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });

        } else {
            Intent intent = new Intent(PrincipalActivity.this, InternetActivity.class);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            intent.putExtra("desde", "principal");
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.utils.salir();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.perfil) {
            Intent intent = new Intent(PrincipalActivity.this, PerfilActivity.class);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            startActivity(intent);
            finish();
        } else if (id == R.id.pedidos) {
            Intent intent = new Intent(PrincipalActivity.this, PedidosActivity.class);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            if(adp.items.size()>0){
                intent.putExtra("cliente",adp.items.get(0).getCliente());
                intent.putExtra("objCliente", new Cliente(adp.items.get(0).getCodClie(),adp.items.get(0).getCliente()));
            }
            else {
                intent.putExtra("cliente","");
                intent.putExtra("objCliente",(Serializable) (Cliente) null);
            }
            intent.putExtra("convenio","");
            intent.putExtra("producto","");
            intent.putExtra("cantidad",(double) 0);
            intent.putExtra("objBodega",(Serializable) (Bodega) bodega);
            intent.putExtra("objConvenio",(Serializable) (Convenio) null);
            intent.putExtra("objProducto", (Serializable) (Producto) null);
            startActivity(intent);
            finish();
        } else if (id == R.id.settings) {
            Intent intent = new Intent(PrincipalActivity.this, SettingsActivity.class);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            startActivity(intent);
            finish();
        } else if (id == R.id.about) {
            Intent intent = new Intent(PrincipalActivity.this, AboutActivity.class);
            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
            startActivity(intent);
            finish();
        }else if (id == R.id.exit) {
            this.utils.salir();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_principal, menu);
        delete = menu.findItem(R.id.delete);
        save =  menu.findItem(R.id.guardar);
        if(adp.items.size()>0){
            delete.setVisible(true);
            save.setVisible(true);
        }else{
            delete.setVisible(false);
            save.setVisible(false);
        }
        return true;
    }

    public double cantidadTotalArticulos(List<ItemLista> lista){
        BigDecimal sumAux = new BigDecimal(0);
        if(lista!=null){
            for (ItemLista item: lista) {
                BigDecimal aux = new BigDecimal(item.getCantidad());
                sumAux = sumAux.add(aux);
                //cant+=item.getCantidad();
            }
        }
        return sumAux.doubleValue();
    }

    public double precioTotal(List<ItemLista> lista){
        BigDecimal sumAux = new BigDecimal(0);
        if(lista!=null){
            for (ItemLista item: lista)
            {
                BigDecimal aux = BigDecimal.valueOf(item.getTotal()).setScale(2, RoundingMode.HALF_EVEN);
                sumAux = sumAux.add(aux);
                System.out.println("El nuemro es : "+sumAux.toString());
                //total+=item.getTotal();
            }
        }
        return sumAux.doubleValue();
    }

    public double precioFactorTotal(List<ItemLista> lista){
        BigDecimal sumAux = new BigDecimal(0);
        if(lista!=null){
            for (ItemLista item: lista)
            {
                BigDecimal aux = new BigDecimal(item.getTasa_total()).setScale(2, RoundingMode.HALF_EVEN);
                sumAux = sumAux.add(aux);
                //total+=item.getTasa_total();
            }
        }
        return sumAux.doubleValue();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.delete:
                    utils.getMap().cancelarPedido();
                    //int size = adp.items.size();
                    adp.items.clear();
                    adp.notifyItemRangeChanged(0,adp.getItemCount());
                    utils.getAux().setFactor(0);
                    delete.setVisible(false);
                    save.setVisible(false);
                    //utils.getAux().setClienteTextConValor(false);
                    utils.getAux().setCliente("");
                    llenarVista();
                    utils.showSatisfactorio(getResources().getString(R.string.successes_msj));
                return true;
            case R.id.guardar:
                GoProcessarPedido goBodegas = new GoProcessarPedido();
                goBodegas.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void llenarVista(){
        if(adp.items.size()>0){
            cliente.setVisibility(View.VISIBLE);
            cliente.setText("Cliente: "+ adp.items.get(0).getCliente());
        }
        else {
            cliente.setVisibility(View.GONE);
        }

        factor.setText(""+utils.getAux().getFactor());
        cant_art.setText(BigDecimal.valueOf(cantidadTotalArticulos(items)).toString());
        total_factor.setText(BigDecimal.valueOf(precioFactorTotal(items)).toString());
        total_precio.setText(BigDecimal.valueOf(precioTotal(items)).toString());
    }

    private class GoProcessarPedido extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... sUrl) {

            String codigoUsuarioDesencriptado = "";
            try {
                codigoUsuarioDesencriptado = utils.deseencriptar(utils.getMap().getIdUser(),utils.getClavaEcncript());
            } catch (Exception e) {
                e.printStackTrace();
            }

            String imei = "";
            if(!manager.getDeviceId().equals("")){
                imei = manager.getDeviceId();
            }else{
                imei = getResources().getString(R.string.unknow);
            }
            imei = imei.substring(0,10);


           ArrayList prod = new ArrayList<ProductoAux>();
            for (ItemLista item : items){
                prod.add(new ProductoAux(
                        item.getCodigo(),
                        new BigDecimal(item.getCantidad()).setScale(2,RoundingMode.HALF_EVEN).toString(),
                        new BigDecimal(item.getPrecioInicial()).setScale(2,RoundingMode.HALF_EVEN).toString(),
                        new BigDecimal(item.getIva()).setScale(2,RoundingMode.HALF_EVEN).toString())
                );
            }

            ObjPedido  objPedido = new ObjPedido(
                    codigoUsuarioDesencriptado,
                    items.get(0).getCodClie(),
                    items.get(0).getCodbodega(),
                    String.valueOf(utils.getAux().getFactor()),
                    imei,
                    prod);

            enviarPedido(objPedido);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private void enviarPedido(ObjPedido json){
            mcall  = retrofitApiJson.sendFactura(json);
            mcall.enqueue(new Callback<FacturaResponse>() {
                @Override
                public void onResponse(Call<FacturaResponse> call, Response<FacturaResponse> response) {
                    if(!response.isSuccessful()){
                        progressDialog.dismiss();
                        utils.showError(new Errores(""+response.code(),getApplicationContext()).codeErrors());
                    }else{
                        FacturaResponse facturaResponse = response.body();
                        if(facturaResponse.getObjAux().getRespuesta().equals("true")){
                            utils.getMap().cancelarPedido();
                            //int size = adp.items.size();
                            adp.items.clear();
                            adp.notifyItemRangeChanged(0,adp.getItemCount());
                            utils.getAux().setFactor(0);
                            delete.setVisible(false);
                            save.setVisible(false);
                            //utils.getAux().setClienteTextConValor(false);
                            utils.getAux().setCliente("");
                            llenarVista();
                            progressDialog.dismiss();
                            utils.showSatisfactorio(facturaResponse.getObjAux().getMensaje());
                        }else{
                            progressDialog.dismiss();
                            utils.showInfo(facturaResponse.getObjAux().getMensaje());
                        }
                    }
                }
                @Override
                public void onFailure(Call<FacturaResponse> call, Throwable t) {
                    if(call.isCanceled()){
                        progressDialog.dismiss();
                        utils.showInfo(getResources().getString(R.string.process_cancel));
                    }else{
                        progressDialog.dismiss();
                        utils.showError(t.getMessage());
                    }
                }
            });
        }
    }


}

