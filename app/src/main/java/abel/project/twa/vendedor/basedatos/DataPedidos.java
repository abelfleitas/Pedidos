package abel.project.twa.vendedor.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

import abel.project.twa.vendedor.modelos.ItemLista;
import abel.project.twa.vendedor.modelos.Vendedor;

public class DataPedidos extends AppCompatActivity{

    private SQLiteDatabase db;
    private Context context;
    private PedidosBD pedidosBD;

    public DataPedidos(Context pcontext) {
        context=pcontext;
        pedidosBD = new PedidosBD(context,"Pedidos",null,1);
    }

    public void registrarUserLogged(String codigo,String name,String codbodega,String namebodega){
        db = pedidosBD.getWritableDatabase();
        db.execSQL("INSERT OR IGNORE INTO Users (codigo,name,codbodega,namebodega) VALUES ('" + codigo + "','" + name +"','" + codbodega +"','"+namebodega +"')");
        db.close();
    }

    public void logoutUser(String codigo){
        db = pedidosBD.getReadableDatabase();
        String[] bind = new String[]{""+codigo};
        db.delete("Users","codigo=?",bind);
        db.close();
    }

    public String getIdUser(){
        db = pedidosBD.getReadableDatabase();
        String codigo = "";
        String[] campos = new String[]{"codigo"};
        Cursor c = db.query("Users",campos,null,null,null,null,null);
        if (c.moveToFirst()) {
            do {
                codigo = c.getString(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return codigo;
    }

    public String getNameUserById(String code){
        String name = "";
        db = pedidosBD.getReadableDatabase();
        ArrayList<Vendedor> results = new ArrayList<>();
        int i = 0;
        String[] campos = new String[]{"name"};
        String[] bind = new String[]{""+code};
        Cursor c = db.query("Users",campos,"codigo=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                name = c.getString(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return name;
    }


    public double getPrecioInicialById(int id){
        double preciI = 0;
        db = pedidosBD.getReadableDatabase();
        ArrayList<Vendedor> results = new ArrayList<>();
        int i = 0;
        String[] campos = new String[]{"precioInicial"};
        String[] bind = new String[]{""+id};
        Cursor c = db.query("Pedidos",campos,"id=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                preciI = c.getDouble(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return preciI;
    }

    public String getNomProductoById(int id){
        String nombre = "";
        db = pedidosBD.getReadableDatabase();
        ArrayList<Vendedor> results = new ArrayList<>();
        int i = 0;
        String[] campos = new String[]{"nomprod"};
        String[] bind = new String[]{""+id};
        Cursor c = db.query("Pedidos",campos,"id=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                nombre = c.getString(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return nombre;
    }


    public void actualizarBodega(String codigo,String codbodega,String namebodega){
        db = pedidosBD.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("codbodega",codbodega);
        valores.put("namebodega",namebodega);
        String[] arg = new String[]{""+codigo};
        db.update("Users",valores,"codigo=?",arg);
        db.close();
    }

    public String getSelectedCodBodega(String code){
        String bodega = "";
        db = pedidosBD.getReadableDatabase();
        int i = 0;
        String[] campos = new String[]{"codbodega"};
        String[] bind = new String[]{""+code};
        Cursor c = db.query("Users",campos,"codigo=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                bodega = c.getString(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return bodega;
    }

    public String getSelectedBodega(String code){
        String bodega = "";
        db = pedidosBD.getReadableDatabase();
        String[] campos = new String[]{"namebodega"};
        String[] bind = new String[]{""+code};
        Cursor c = db.query("Users",campos,"codigo=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                bodega = c.getString(0);
            }
            while (c.moveToNext());
        }
        db.close();
        return bodega;
    }

    public void insertarProducto(String codcliente,String nomcliente,String codbodega,String nombodega,String codConv, String convenio,String codprod,String nomprod,double cant,String und,double precio,double total,double factor,double factorTotal,double iva,double precioInicial){
        db = pedidosBD.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("codigo_cliente",codcliente);
        valores.put("nom_cliente",nomcliente);
        valores.put("codbodega",codbodega);
        valores.put("nombodega",nombodega);
        valores.put("codconvenio",codConv);
        valores.put("convenio",convenio);
        valores.put("codprod",codprod);
        valores.put("nomprod",nomprod);
        valores.put("cantprod",cant);
        valores.put("unidadprod",und);
        valores.put("precioprod",precio);
        valores.put("totalprod",total);
        valores.put("precioconvertprod",String.valueOf(factor));
        valores.put("totalconvertprod",factorTotal);
        valores.put("iva",iva);
        valores.put("precioInicial",precioInicial);
        db.insert("Pedidos",null,valores);
        db.close();
    }

    public void editarProducto(String codcliente,String nomcliente,String codbodega,String nombodega,String codConv, String convenio,String codprod,String nomprod,double cant,String und,double precio,double total,double factor,double factorTotal,double iva,int id,double precioInicial){
        db = pedidosBD.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("codigo_cliente",codcliente);
        valores.put("nom_cliente",nomcliente);
        valores.put("codbodega",codbodega);
        valores.put("nombodega",nombodega);
        valores.put("codconvenio",codConv);
        valores.put("convenio",convenio);
        valores.put("codprod",codprod);
        valores.put("nomprod",nomprod);
        valores.put("cantprod",cant);
        valores.put("unidadprod",und);
        valores.put("precioprod",precio);
        valores.put("totalprod",total);
        valores.put("precioconvertprod",factor);
        valores.put("totalconvertprod",factorTotal);
        valores.put("iva",iva);
        valores.put("precioInicial",precioInicial);
        String[] arg = new String[]{""+id};
        db.update("Pedidos",valores,"id=?",arg);
        db.close();
    }

    public void eliminarProductoPedido(int id){
        db = pedidosBD.getWritableDatabase();
        String[] bind = new String[]{""+id};
        db.delete("Pedidos","id=?",bind);
        db.close();
    }

    public void cancelarPedido(){
        db = pedidosBD.getWritableDatabase();
        db.delete("Pedidos",null,null);
        db.close();
    }

    public ArrayList<ItemLista> getListaPedidos(){
        ArrayList<ItemLista> lista = new ArrayList<>();
        db = pedidosBD.getReadableDatabase();
        String[] campos = new String[]{"id,codprod,nomprod,cantprod,unidadprod,precioprod,totalprod,precioconvertprod,totalconvertprod,codbodega,nombodega,codigo_cliente,nom_cliente,codconvenio,convenio,iva,precioInicial"};
        Cursor c = db.query("Pedidos",campos,null,null,null,null,null);
        if (c.moveToFirst()) {
            do {
                lista.add(new ItemLista(c.getInt(0),c.getString(2),c.getString(1),c.getDouble(3),c.getString(4),c.getDouble(5),c.getDouble(7),c.getDouble(6),c.getDouble(8),c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13),c.getString(14),c.getDouble(15),c.getDouble(16)));
            }
            while (c.moveToNext());
        }
        db.close();
        return lista;
    }

    public ItemLista getItemPedidos(int id)
    {
        ItemLista lista = null;
        db = pedidosBD.getReadableDatabase();
        String[] bind = new String[]{""+id};
        String[] campos = new String[]{"id,codprod,nomprod,cantprod,unidadprod,precioprod,totalprod,precioconvertprod,totalconvertprod,codbodega,nombodega,codigo_cliente,nom_cliente,codconvenio,convenio,iva,precioInicial"};
        Cursor c = db.query("Pedidos",campos,"id=?",bind,null,null,null);
        if (c.moveToFirst()) {
            do {
                lista = new ItemLista(c.getInt(0),c.getString(2),c.getString(1),c.getDouble(3),c.getString(4),c.getDouble(5),c.getDouble(7),c.getDouble(6),c.getDouble(8),c.getString(9),c.getString(10),c.getString(11),c.getString(12),c.getString(13),c.getString(14),c.getDouble(15),c.getDouble(16));
            }
            while (c.moveToNext());
        }
        db.close();
        return lista;
    }

}

