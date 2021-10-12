package abel.project.twa.vendedor;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import abel.project.twa.vendedor.auxiliar.Utils;

public class SplashActivity extends AppCompatActivity {

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        utils = new Utils(this);
        changeStatusBarColor();
        this.ejecutarProgreso();
    }

    private void ejecutarProgreso(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    this.timeWait();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    if (utils.checkInternetConnection()) {
                        if(utils.verificarLogin()){
                            Intent intent = new Intent(SplashActivity.this, PrincipalActivity.class);
                            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(SplashActivity.this, InternetActivity.class);
                        overridePendingTransition(R.anim.zoom_back_in, R.anim.zoom_back_out);
                        intent.putExtra("desde","splash");
                        startActivity(intent);
                        finish();
                    }

                    }
                });
            }

            private void timeWait() {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        System.exit(0);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_type_info));
        }
    }

}
