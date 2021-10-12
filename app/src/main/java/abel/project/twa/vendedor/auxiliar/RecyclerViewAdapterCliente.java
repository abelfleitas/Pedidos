package abel.project.twa.vendedor.auxiliar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import abel.project.twa.vendedor.R;
import abel.project.twa.vendedor.modelos.Cliente;

public class RecyclerViewAdapterCliente extends RecyclerView.Adapter<RecyclerViewAdapterCliente.DataObjectHolder>{

    private ArrayList<Cliente> mDataset;
    private Context context;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView label,label1;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.itemText);
            label1 = (TextView) itemView.findViewById(R.id.itemText1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public RecyclerViewAdapterCliente(ArrayList<Cliente> myDataset, Context pcontext) {
        mDataset = myDataset;
        context = pcontext;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_cliente, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }


    public ArrayList<Cliente> getDataset(){
        return  mDataset;
    }


    @Override
    public void onBindViewHolder(final DataObjectHolder holder,int position) {
        final int pos = position;
        holder.label.setText(""+mDataset.get(position).getCodClie());
        holder.label1.setText(""+mDataset.get(position).getDescrip());
    }

    public void addItem(Cliente dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


}
