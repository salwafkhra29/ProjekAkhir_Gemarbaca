package com.si6a.gemarbaca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.si6a.gemarbaca.R;
import com.si6a.gemarbaca.model.GemarBacaData;
import com.si6a.gemarbaca.utilities.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GemarBacaAdapter extends RecyclerView.Adapter<GemarBacaAdapter.ViewHolder> {

    private final Context context;
    private final List<GemarBacaData> mList;

    public GemarBacaAdapter(Context context, List<GemarBacaData> mList) {
        this.context = context;
        this.mList = mList;
    }

    private OnItemClickListener listener = null;

    public interface OnItemClickListener{
        void onItemDeleted(int position);
        void onItemEdited(int position);
        void onItemDetails(int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gemarbaca, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GemarBacaData data = mList.get(position);

        Picasso.get().load(ImageUtils.convertBase64ToUri(context, data.getImage())).into(holder.ivFoto);
        holder.tvJudul.setText(data.getTitle());
        holder.tvTentang.setText(data.getAbout());

        int adapterPosition = holder.getAdapterPosition();

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    listener.onItemEdited(adapterPosition);
                }
            }
        });
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    listener.onItemDetails(adapterPosition);
                }
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapterPosition != RecyclerView.NO_POSITION){
                    listener.onItemDeleted(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvJudul, tvTentang;
        Button btnHapus, btnEdit, btnDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFoto = itemView.findViewById(R.id.iv_foto);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvTentang = itemView.findViewById(R.id.tv_tentang);
            btnHapus = itemView.findViewById(R.id.btn_hapus);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
