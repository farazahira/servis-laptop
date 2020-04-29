package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter.ArticleAdapter;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.ArticleModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProblemFragment extends Fragment {

    private static final String TAG = "MainActivity";

    private ArticleAdapter articleAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;


    public ProblemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);

        final RecyclerView recyclerView = new RecyclerView(getContext());
        firestoreDB = FirebaseFirestore.getInstance();

        loadArticleList();

        firestoreListener = firestoreDB.collection("article").orderBy("like", Query.Direction.DESCENDING).limit(10)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listed Failed", e);
                            return;
                        }

                        List<ArticleModel> articleModelList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            ArticleModel articleModel = doc.toObject(ArticleModel.class);
                            articleModel.setId(doc.getId());
                            articleModelList.add(articleModel);
                        }

                        articleAdapter = new ArticleAdapter(articleModelList, getContext(), firestoreDB);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(articleAdapter);

                    }
                });

        return recyclerView;
    }

    private void loadArticleList() {

        final RecyclerView recyclerView = new RecyclerView(getContext());

        firestoreDB.collection("article").orderBy("like", Query.Direction.ASCENDING).limit(10)
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

                            articleAdapter = new ArticleAdapter(articleModelList, getContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(articleAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
