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

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter.CenterAdapter;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.CenterModel;

public class CenterActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private CenterAdapter centerAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_list_center);
        firestoreDB = FirebaseFirestore.getInstance();

        loadCenterList();

        firestoreListener = firestoreDB.collection("centerHome")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listed Failed", e);
                            return;
                        }

                        List<CenterModel> centerModelList = new ArrayList<>();

                        for (DocumentSnapshot doc : documentSnapshots) {
                            CenterModel centerModel = doc.toObject(CenterModel.class);
                            centerModel.setId(doc.getId());
                            centerModelList.add(centerModel);

                        }

                        centerAdapter = new CenterAdapter(centerModelList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(centerAdapter);

                    }
                });

    }

    private void loadCenterList() {
        firestoreDB.collection("centerHome")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<CenterModel> centerModelList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                CenterModel centerModel = doc.toObject(CenterModel.class);
                                centerModel.setId(doc.getId());
                                centerModelList.add(centerModel);
                            }

                            centerAdapter = new CenterAdapter(centerModelList, getApplicationContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(centerAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
