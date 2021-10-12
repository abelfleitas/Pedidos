package abel.project.twa.vendedor.barcodescanner;

import android.content.Intent;
import android.os.Bundle;
import java.io.Serializable;
import abel.project.twa.vendedor.ProductoActivity;
import abel.project.twa.vendedor.R;

public class FullScannerFragmentActivity extends BaseScannerActivity {


    private Bundle bundle;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_full_scanner_fragment);
        bundle = getIntent().getExtras();
        setupToolbar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(FullScannerFragmentActivity.this, ProductoActivity.class);
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
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
        finish();
    }



}