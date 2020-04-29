package id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import id.sch.smktelkom_mlg.afinal.xirpl3060724.servicelaptoponline.model.ArticleModel;

public class DetailArticle extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView tv_judu, tv_katalog, tv_sebab, tv_solusi, tv_sumbber, tv_date;
    Button btnLike;
    Map<String, Object> data = new HashMap<>();
    Boolean status;
    ProgressDialog progressDialog;
    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        status = false;

        Intent intent = getIntent();
        final String docID = intent.getStringExtra("DocumArt");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);

        firestoreDB = FirebaseFirestore.getInstance();

        tv_judu = findViewById(R.id.judultv);
        tv_katalog = findViewById(R.id.katalogtv);
        tv_sebab = findViewById(R.id.isisebabtv);
        tv_solusi = findViewById(R.id.isisolusitv);
        tv_sumbber = findViewById(R.id.sumbertv);
        tv_date = findViewById(R.id.datetv);
        btnLike = findViewById(R.id.suka);

        if (user != null) {
            firestoreDB.collection("article").document(docID).collection("likes").whereEqualTo("email", user.getEmail())
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


        final DocumentReference documentReference = firestoreDB.document("article/" + docID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    final ArticleModel articleModel = documentSnapshot.toObject(ArticleModel.class);

                    tv_judu.setText(articleModel.getTitle());
                    tv_katalog.setText(articleModel.getCatalog());
                    tv_sebab.setText(articleModel.getSebab());
                    tv_solusi.setText(articleModel.getSolusi());
                    tv_sumbber.setText(articleModel.getSource());
                    tv_date.setText("Updated at: " + String.valueOf(articleModel.getDate()));

                    //update
                    btnLike.setOnClickListener(new View.OnClickListener() {
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

                                    firestoreDB.collection("article").document(docID).collection("likes").add(data);

                                    documentReference.update("like", articleModel.getLike() + 1)
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
