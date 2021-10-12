package abel.project.twa.vendedor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import abel.project.twa.vendedor.auxiliar.Utils;

public class InternetActivity extends AppCompatActivity {

    private Button reload;
    private Bundle bundle;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        utils = new Utils(this);
        reload = (Button) findViewById(R.id.reload);
        bundle = getIntent().getExtras();
        final String deDondeVino = bundle.getString("desde");
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.checkInternetConnection()) {
                    switch (deDondeVino){
                        case "splash":
                            Intent intent = new Intent(InternetActivity.this,LoginActivity.class);
                            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                            startActivity(intent);
                            finish();
                            break;
                        case "login":
                            Intent intent1 = new Intent(InternetActivity.this,LoginActivity.class);
                            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                            startActivity(intent1);
                            finish();
                            break;
                        case "principal":
                            if(utils.verificarLogin()){
                                Intent intent2 = new Intent(InternetActivity.this,PrincipalActivity.class);
                                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                                startActivity(intent2);
                                finish();
                            }else{
                                Intent intent2 = new Intent(InternetActivity.this,LoginActivity.class);
                                overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                                startActivity(intent2);
                                finish();
                            }
                            break;
                    }
                } else {
                    showDialog();
                }
            }
        });
    }

    public void showDialog(){
        this.utils.alertInternet();
    }

    @Override
    public void onBackPressed() {
        this.utils.salir();
    }
}
