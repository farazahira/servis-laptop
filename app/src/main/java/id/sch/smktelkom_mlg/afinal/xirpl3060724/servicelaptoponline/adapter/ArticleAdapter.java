package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.DetailArticle;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.R;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.ArticleModel;

/**
 * Created by Hp on 17/04/2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<ArticleModel> articleModelList;
    private Context context;
    private FirebaseFirestore firestoreDB;

    public ArticleAdapter(List<ArticleModel> centerList, Context context, FirebaseFirestore firestoreDB) {
        this.articleModelList = centerList;
        this.context = context;
        this.firestoreDB = firestoreDB;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        final ArticleModel articleModel = articleModelList.get(position);

        holder.tvTitle.setText(articleModel.getTitle());
        holder.tvCatalog.setText(articleModel.getCatalog());
        holder.tvLike.setText(String.valueOf(articleModel.getLike()));
        holder.btninfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailArticle.class);
                intent.putExtra("DocumArt", articleModel.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (articleModelList != null) {
            return articleModelList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCatalog, tvLike;
        Button btninfo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_art_title);
            tvCatalog = itemView.findViewById(R.id.tv_catalog);
            tvLike = itemView.findViewById(R.id.tv_plikes);
            btninfo = itemView.findViewById(R.id.btninfo);
        }
    }
}
