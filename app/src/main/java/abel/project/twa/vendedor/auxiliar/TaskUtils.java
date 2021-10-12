package abel.project.twa.vendedor.auxiliar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.widget.Toast;
import java.util.List;
import abel.project.twa.vendedor.R;

public class TaskUtils extends Activity{

    public void taskReboot(Intent intent, Context context, Object obj, Class activityclass) {
        boolean reboot = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("reboot", false);
        if (!(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) || !reboot) {
            obj = null;
        }
        if (obj != null) {
            Intent intent2 = new Intent(context, activityclass);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }

    public Dialog showMessageAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.alerta2) + " " + context.getResources().getString(R.string.app_name));
        builder.setMessage(context.getResources().getString(R.string.rebootError2));
        builder.setPositiveButton(context.getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNeutralButton(context.getResources().getString(R.string.contact_us), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"afleitasp@@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                //Recordad que la barra invertida más "n" es un salto de línea "n" así,
                // escribiremos el email con varios saltos de linea.
                //  String textoApp = "Envio un email desde mi App de androidnnnCreado
                // gracias a:nhttp://ekiketa.es";
                emailIntent.setType("message/rfc822");
                //Verificamos que existan apps para enviar correo:
                PackageManager packageManager = context.getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    //Damos la opción al usuario que elija desde que app enviamos el email.
                    context.startActivity(Intent.createChooser(emailIntent, context.getResources().getString(R.string.choose)));
                } else {
                    Toast.makeText(context,getResources().getString(R.string.noappemail),Toast.LENGTH_LONG).show();
                }
            }
        });
        return builder.create();
    }


}
