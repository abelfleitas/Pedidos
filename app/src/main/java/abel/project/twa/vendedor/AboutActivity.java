package abel.project.twa.vendedor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Suggestion
        LinearLayout sug=(LinearLayout) findViewById(R.id.linearItemSugerencia);
        sug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.abel_email)});
                emailIntent.putExtra(Intent.EXTRA_CC, new String[]{getResources().getString(R.string.luis_email)
                        , getResources().getString(R.string.luis_email2), getResources().getString(R.string.luis_email3)
                        , getResources().getString(R.string.juan_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sugerencia) + " " + getString(R.string.app_name));
                emailIntent.setType("message/rfc822");
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.choose)));
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noappemail), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Problem
        LinearLayout problem=(LinearLayout) findViewById(R.id.linearItemReporte);
        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.abel_email)});
                emailIntent.putExtra(Intent.EXTRA_CC, new String[]{getResources().getString(R.string.luis_email)
                        , getResources().getString(R.string.luis_email2), getResources().getString(R.string.luis_email3)
                        , getResources().getString(R.string.juan_email)});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.error_app_version));
                emailIntent.setType("message/rfc822");
                PackageManager packageManager = getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.choose)));
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noappemail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AboutActivity.this, PrincipalActivity.class));
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
