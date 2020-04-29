package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.CenterModel;

public class DetailCenter extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button map;
    TextView tv_judul, tv_deskrip, tv_alamat, tv_kontak, tv_jam;
    Button btnLikes;
    CollapsingToolbarLayout toolbarLayout;
    Map<String, Object> data = new HashMap<>();
    Boolean status;
    ProgressDialog progressDialog;
    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_center);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestoreDB = FirebaseFirestore.getInstance();

        status = false;

        Intent intent = getIntent();
        final String docID = intent.getStringExtra("Docum");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);

        map = findViewById(R.id.button_map);

        tv_alamat = findViewById(R.id.tv_D_alamat);
        tv_deskrip = findViewById(R.id.tv_D_deskripsi);
        tv_kontak = findViewById(R.id.tv_D_kontak);
        tv_jam = findViewById(R.id.tv_D_jam);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        btnLikes = findViewById(R.id.button_like_cen);

        final DocumentReference documentReference = firestoreDB.document("centerHome/" + docID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    final CenterModel centerModel = documentSnapshot.toObject(CenterModel.class);

                    Glide.with(getApplicationContext()).load(centerModel.getUrlFile()).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            toolbarLayout.setBackground(resource);
                        }
                    });

                    tv_alamat.setText("Alamat: " + centerModel.getAddress());
                    tv_kontak.setText("Kontak: " + centerModel.getNumber());
                    tv_deskrip.setText(centerModel.getDeskripsi());
                    tv_jam.setText("Buka Pukul: " + centerModel.getBukajam());
                    toolbarLayout.setTitle(centerModel.getServiceName());

                    if (user != null) {
                        firestoreDB.collection("centerHome").document(docID).collection("likes").whereEqualTo("email", user.getEmail())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        if (doc.exists()) {
                                            status = true;
                                        }
                                    }
                                }
                            }
                        });
                    }

                    map.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   Uri intentUri = Uri.parse("geo:" + centerModel.getLatitude() + "," + centerModel.getLongtitude() + "?q=" + centerModel.getLatitude() + "," + centerModel.getLongtitude() + "(" + centerModel.getServiceName() + ")");
                                                   Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                                                   mapIntent.setPackage("com.google.android.apps.maps");
                                                   if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                                       startActivity(mapIntent);
                                                   }
                                               }
                                           }
                    );

                    btnLikes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            progressDialog.setMessage("Liking This...");
                            progressDialog.show();
                            if (user != null) {


                                if (!status) {
                                    data.put("email", user.getEmail());
                                    data.put("name", user.getDisplayName());
                                    data.put("Uid", user.getUid());

                                    firestoreDB.collection("centerHome").document(docID).collection("likes").add(data);

                                    documentReference.update("like", centerModel.getLike() + 1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.hide();
                                                    Toast.makeText(getApplication(), "Liked it. Thanks", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    progressDialog.hide();
                                    Toast.makeText(getApplication(), "Only can like this one time", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                progressDialog.hide();
                                Toast.makeText(getApplication(), "Please Sign in First", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplication(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
