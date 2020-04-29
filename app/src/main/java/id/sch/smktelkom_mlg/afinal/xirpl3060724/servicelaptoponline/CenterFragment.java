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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.adapter.CenterAdapter;
import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.CenterModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends Fragment {

    private static final String TAG = "MainActivity";
    ProgressBar progressBar;
    ImageView imageView;
    private CenterAdapter centerAdapter;
    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;


    public CenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center, container, false);
        final RecyclerView recyclerView = new RecyclerView(getContext());
        firestoreDB = FirebaseFirestore.getInstance();

        loadCenterList();
        firestoreListener = firestoreDB.collection("centerHome").orderBy("like", Query.Direction.DESCENDING).limit(4)
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

                        centerAdapter = new CenterAdapter(centerModelList, getContext(), firestoreDB);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(centerAdapter);

                    }
                });

        return recyclerView;
    }

    public void loadCenterList() {

        final RecyclerView recyclerView = new RecyclerView(getContext());

        firestoreDB.collection("centerHome").orderBy("like", Query.Direction.ASCENDING).limit(4)
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

                            centerAdapter = new CenterAdapter(centerModelList, getContext(), firestoreDB);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(centerAdapter);
                        } else {
                            Toast.makeText(getActivity(), "Can't Refresh Data", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
