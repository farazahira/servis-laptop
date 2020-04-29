package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter.ArticleAdapter;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.ArticleModel;


public class ArticleActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private ArticleAdapter mAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_article);
        firestoreDB = FirebaseFirestore.getInstance();
        loadArticleList();

        firestoreListener = firestoreDB.collection("article")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<ArticleModel> articleList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            ArticleModel articleModel = doc.toObject(ArticleModel.class);
                            articleModel.setId(doc.getId());
                            articleList.add(articleModel);
                        }

                        mAdapter = new ArticleAdapter(articleList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

    private void loadNotesList() {
        firestoreDB.collection("article")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ArticleModel> articleList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                ArticleModel articleModel = doc.toObject(ArticleModel.class);
                                articleModel.setId(doc.getId());
                                articleList.add(articleModel);
                            }

                            mAdapter = new ArticleAdapter(articleList, getApplicationContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void loadArticleList() {
        firestoreDB.collection("article")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ArticleModel> articleModelList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                ArticleModel articleModel = doc.toObject(ArticleModel.class);
                                articleModel.setId(doc.getId());
                                articleModelList.add(articleModel);
                            }

                            mAdapter = new ArticleAdapter(articleModelList, getApplicationContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}


