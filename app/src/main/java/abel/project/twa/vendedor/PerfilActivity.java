package abel.project.twa.vendedor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rey.material.widget.Switch;
import abel.project.twa.vendedor.auxiliar.Utils;

public class PerfilActivity extends AppCompatActivity {

    private LinearLayout cerrarsesion;
    private Switch activeLogin;
    private TextView idcodigo,nombrevendedor,codBodega,descripBodega,status;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utils = new Utils(this);
        cerrarsesion = (LinearLayout) findViewById(R.id.close);
        idcodigo = (TextView) findViewById(R.id.idcodigo);
        nombrevendedor = (TextView) findViewById(R.id.nombrevendedor);
        codBodega = (TextView) findViewById(R.id.codBodega);
        descripBodega = (TextView) findViewById(R.id.descripBodega);
        status = (TextView) findViewById(R.id.status);
        if(utils.checkInternetConnection()){
            status.setText("Estatus : "+utils.getEstatus());
        }else{
            status.setText("Estatus : Desconectado");
        }
        activeLogin = (Switch) findViewById(R.id.switcher);
        activeLogin.setChecked(utils.getAux().isActiveSessionClose());
        try {
            String codigoUsuarioDesencriptado = utils.deseencriptar(utils.getMap().getIdUser(),utils.getClavaEcncript());
            idcodigo.setText(codigoUsuarioDesencriptado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        nombrevendedor.setText(utils.getMap().getNameUserById(utils.getMap().getIdUser()));
        codBodega.setText(utils.getMap().getSelectedCodBodega(utils.getMap().getIdUser()));
        descripBodega.setText(utils.getMap().getSelectedBodega(utils.getMap().getIdUser()));
        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.dialogCerrarSesion();
            }
        });
        activeLogin.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final Switch view, boolean checked) {
                if(checked) {
                    utils.getAux().setSessionClose(true);
                }else{
                    utils.getAux().setSessionClose(false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PerfilActivity.this, PrincipalActivity.class));
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

}
