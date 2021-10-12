package abel.project.twa.vendedor.auxiliar;

import android.content.Context;
import abel.project.twa.vendedor.R;

public class Errores {

    private String error;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Errores(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Errores(String error, Context context) {
        this.error = error;
        this.context = context;
    }

    public String codeErrors(){
        String var = "";
        switch (error){
            case "401":
                var = "(401) "+context.getResources().getString(R.string.error_401_all);
                break;
            case "402":
                var = "(402) "+context.getResources().getString(R.string.error_402_all);
                break;
            case "403":
                var = "(403) "+context.getResources().getString(R.string.error_403_all);
                break;
            case "404":
                var = "(404) "+context.getResources().getString(R.string.error_404_all);
                break;
            case "405":
                var = "(405) "+context.getResources().getString(R.string.error_405_all);
                break;
            case "406":
                var = "(406) "+context.getResources().getString(R.string.error_406_all);
                break;
            case "407":
                var = "(407) "+context.getResources().getString(R.string.error_407_all);
                break;
            case "408":
                var = "(408) "+context.getResources().getString(R.string.error_408_all);
                break;
            case "409":
                var = "(409) "+context.getResources().getString(R.string.error_409_all);
                break;
            case "410":
                var = "(410) "+context.getResources().getString(R.string.error_410_all);
                break;
            case "411":
                var = "(411) "+context.getResources().getString(R.string.error_411_all);
                break;
            case "412":
                var = "(412) "+context.getResources().getString(R.string.error_412_all);
                break;
            case "413":
                var = "(413) "+context.getResources().getString(R.string.error_413_all);
                break;
            case "414":
                var = "(414) "+context.getResources().getString(R.string.error_414_all);
                break;
            case "415":
                var = "(415) "+context.getResources().getString(R.string.error_415_all);
                break;
            case "416":
                var = "(416) "+context.getResources().getString(R.string.error_416_all);
                break;
            case "417":
                var = "(417) "+context.getResources().getString(R.string.error_417_all);
                break;
            case "418":
                var = "(418) "+context.getResources().getString(R.string.error_418_all);
                break;
            case "422":
                var = "(422) "+context.getResources().getString(R.string.error_422_all);
                break;
            case "423":
                var = "(423) "+context.getResources().getString(R.string.error_423_all);
                break;
            case "424":
                var = "(424) "+context.getResources().getString(R.string.error_424_all);
                break;
            case "425":
                var = "(425) "+context.getResources().getString(R.string.error_425_all);
                break;
            case "426":
                var = "(426) "+context.getResources().getString(R.string.error_426_all);
                break;
            case "429":
                var = "(429) "+context.getResources().getString(R.string.error_429_all);
                break;
            case "449":
                var = "(449) "+context.getResources().getString(R.string.error_449_all);
                break;
            case "500":
                var = "(500) "+context.getResources().getString(R.string.error_500_all);
                break;
            case "501":
                var = "(501) "+context.getResources().getString(R.string.error_501_all);
                break;
            case "502":
                var = "(502) "+context.getResources().getString(R.string.error_502_all);
                break;
            case "503":
                var = "(503) "+context.getResources().getString(R.string.error_503_all);
                break;
            case "504":
                var = "(504) "+context.getResources().getString(R.string.error_504_all);
                break;
            case "505":
                var = "(505) "+context.getResources().getString(R.string.error_505_all);
                break;
            case "506":
                var = "(506) "+context.getResources().getString(R.string.error_506_all);
                break;
            case "507":
                var = "(507) "+context.getResources().getString(R.string.error_507_all);
                break;
            case "509":
                var = "(509) "+context.getResources().getString(R.string.error_509_all);
                break;
            case "510":
                var = "(510) "+context.getResources().getString(R.string.error_510_all);
                break;
            default:
                var = context.getResources().getString(R.string.error_general);
                break;
        }
        return var;
    }


}
