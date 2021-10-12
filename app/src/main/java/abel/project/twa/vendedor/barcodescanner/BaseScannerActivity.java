package abel.project.twa.vendedor.barcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import java.io.Serializable;
import abel.project.twa.vendedor.ProductoActivity;
import abel.project.twa.vendedor.R;

public class BaseScannerActivity extends AppCompatActivity {

    private Bundle bundle;

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        bundle = getIntent().getExtras();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        // Permisos
        /*if (ActivityCompat.checkSelfPermission(BaseScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(BaseScannerActivity.this,ProductoActivity.class);
                intent.putExtra("codigo_barra","");
                intent.putExtra("cliente",bundle.getString("cliente"));
                intent.putExtra("convenio",bundle.getString("convenio"));
                intent.putExtra("producto",bundle.getString("producto"));
                intent.putExtra("cantidad",bundle.getDouble("cantidad"));
                intent.putExtra("objBodega",(Serializable) bundle.get("objBodega"));
                intent.putExtra("objCliente",(Serializable) bundle.get("objCliente"));
                intent.putExtra("objConvenio",(Serializable) bundle.get("objConvenio"));
                intent.putExtra("objProducto", (Serializable) bundle.get("objProducto"));
                intent.putExtra("codConv",bundle.getString("codConv"));
                intent.putExtra("id",bundle.getInt("id"));
                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int [] grantResults){
        switch (requestCode){
            case 0:{
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                }else {
                    BaseScannerActivity.super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                }
            }
            break;
            default:
                BaseScannerActivity.super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }*/
}
