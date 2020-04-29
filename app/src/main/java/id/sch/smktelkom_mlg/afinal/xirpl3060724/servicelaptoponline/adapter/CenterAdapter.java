package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.DetailCenter;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.R;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.CenterModel;

/**
 * Created by Hp on 11/04/2018.
 */

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.ViewHolder> {

    private List<CenterModel> centerModelList;
    private Context context;
    private FirebaseFirestore firestoreDB;


    public CenterAdapter(List<CenterModel> centerList, Context context, FirebaseFirestore firestoreDB) {
        this.centerModelList = centerList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CenterModel centerModel = centerModelList.get(position);

        holder.tvServiceName.setText(centerModel.getServiceName());
        holder.tvDeskripsi.setText(centerModel.getDeskripsi());
        holder.tvAddress.setText(centerModel.getAddress());
        holder.tvLike.setText(String.valueOf(centerModel.getLike()));
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailCenter.class);
                intent.putExtra("Docum", centerModel.getId());
                view.getContext().startActivity(intent);
            }
        });

        Glide.with(context).load(centerModel.getUrlFile()).into(holder.urlImage);

    }

    @Override
    public int getItemCount() {
        if (centerModelList != null) {
            return centerModelList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvServiceName, tvDeskripsi, tvAddress, tvLike;
        ImageView urlImage;
        Button mButton;

        public ViewHolder(View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_title);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskrip);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvLike = itemView.findViewById(R.id.tv_plikes);
            urlImage = itemView.findViewById(R.id.imageView);
            mButton = itemView.findViewById(R.id.btn_select);
        }
    }
}
