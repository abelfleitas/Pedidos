package abel.project.twa.vendedor.auxiliar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import abel.project.twa.vendedor.R;
import abel.project.twa.vendedor.modelos.ItemLista;

public class ItemsDataAdapter extends RecyclerView.Adapter<ItemsDataAdapter.PlayerViewHolder> {
    public List<ItemLista> items;

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView product, code, cant, unidades, precio, precio_tasa, total, totalTasa;

        public PlayerViewHolder(View view) {
            super(view);
            product = (TextView) view.findViewById(R.id.personalInfoTitle);
            code = (TextView) view.findViewById(R.id.codigoValue);
            cant = (TextView) view.findViewById(R.id.cantidadValue);
            unidades = (TextView) view.findViewById(R.id.unidadesValue);
            precio = (TextView) view.findViewById(R.id.precioValue);
            precio_tasa = (TextView) view.findViewById(R.id.precioSummary);
            total = (TextView) view.findViewById(R.id.totalValue);
            totalTasa = (TextView) view.findViewById(R.id.totalSummary);
        }
    }

    public ItemsDataAdapter(List<ItemLista> items) {
        this.items = items;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        ItemLista player = items.get(position);

        holder.product.setText(player.getProducto());
        holder.code.setText(player.getCodigo());

        BigDecimal cantidad = BigDecimal.valueOf(player.getCantidad());
        holder.cant.setText(cantidad.toString());

        holder.unidades.setText(player.getUnidad());


        BigDecimal precio = new BigDecimal(player.getPrecio()).setScale(2,RoundingMode.HALF_EVEN);
        holder.precio.setText(precio.toString());

        BigDecimal tasa_precio = new BigDecimal(player.getTasa_precio()).setScale(2,RoundingMode.HALF_EVEN);
        holder.precio_tasa.setText(tasa_precio.toString());

        BigDecimal total = new BigDecimal(player.getTotal()).setScale(2,RoundingMode.HALF_EVEN);
        holder.total.setText(total.toString());

        BigDecimal tasa_total = new BigDecimal(player.getTasa_total()).setScale(2,RoundingMode.HALF_EVEN);
        holder.totalTasa.setText(tasa_total.toString());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
