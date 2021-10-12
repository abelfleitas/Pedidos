package abel.project.twa.vendedor.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PedidosBD extends SQLiteOpenHelper{


    public PedidosBD(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase bd) {

        bd.execSQL("CREATE TABLE Users (" +
                "codigo varchar NOT NULL," +
                "name varchar NOT NULL," +
                "codbodega varchar NULL," +
                "namebodega varchar NULL," +
                "UNIQUE(codigo,name,codbodega,namebodega)," +
                "CONSTRAINT pk_id_user PRIMARY KEY (codigo)"+
                ")");

        bd.execSQL("CREATE TABLE Pedidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "codigo_cliente varchar NOT NULL,"+
                "nom_cliente varchar NOT NULL," +
                "codbodega varchar NOT NULL,"+
                "nombodega varchar NOT NULL,"+
                "codconvenio varchar NULL,"+
                "convenio varchar NULL,"+
                "codprod varchar NOT NULL,"+
                "nomprod varchar NOT NULL,"+
                "cantprod double NOT NULL,"+
                "unidadprod varchar NOT NULL,"+
                "precioprod double NOT NULL,"+
                "totalprod double NOT NULL,"+
                "precioconvertprod double NOT NULL,"+
                "totalconvertprod double NOT NULL,"+
                "iva double NOT NULL,"+
                "precioInicial double NOT NULL"+
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase bd, int i, int i1) {


        bd.execSQL("DROP TABLE IF EXISTS Users");
        bd.execSQL("CREATE TABLE Users (" +
                "codigo varchar NOT NULL," +
                "name varchar NOT NULL," +
                "codbodega varchar NULL," +
                "namebodega varchar NULL," +
                "UNIQUE(codigo,name,codbodega,namebodega)," +
                "CONSTRAINT pk_id_user PRIMARY KEY (codigo)"+
                ")");


        bd.execSQL("DROP TABLE IF EXISTS Pedidos");
        bd.execSQL("CREATE TABLE Pedidos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "codigo_cliente varchar NOT NULL,"+
                "nom_cliente varchar NOT NULL," +
                "codbodega varchar NOT NULL,"+
                "nombodega varchar NOT NULL,"+
                "codconvenio varchar NULL,"+
                "convenio varchar NULL,"+
                "codprod varchar NOT NULL,"+
                "nomprod varchar NOT NULL,"+
                "cantprod double NOT NULL,"+
                "unidadprod varchar NOT NULL,"+
                "precioprod double NOT NULL,"+
                "totalprod double NOT NULL,"+
                "precioconvertprod double NOT NULL,"+
                "totalconvertprod double NOT NULL,"+
                "iva double NOT NULL,"+
                "precioInicial double NOT NULL"+
                ");");
    }
}

//"CONSTRAINT pk_id_cliente PRIMARY KEY (id)"+
