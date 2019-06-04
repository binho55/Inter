

package com.br.opet.inter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SalaoSelecionado extends AppCompatActivity {

    private List<ObjSenha> listSalao = new ArrayList<ObjSenha>();

    private ArrayAdapter<ObjSenha> arrayAdapterSalao;
    private FirebaseAuth auth;
    private TextView listarSalao, senhaChamada;
    private ListView listarNumero;
    private Button somar;
    private FirebaseFirestore db;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private String salaoSelecionado, numeroChamada,tmp;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userLogado = user.getUid();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salaoselecionado);
        auth = FirebaseAuth.getInstance();
        Intent iin = getIntent();
        String nomeSalaoSelecionado = (String) iin.getSerializableExtra("Nome");
        String idSalao = (String) iin.getSerializableExtra("Uid");
        salaoSelecionado = idSalao;

        somar = findViewById(R.id.mais);
        listarNumero = findViewById(R.id.listarNumero);
        listarSalao = findViewById(R.id.nomeSalao);

        TextView Uid = (TextView) findViewById(R.id.nomeSalao);

        Uid.setText(nomeSalaoSelecionado);
        iniciarFirebase();
        eventoDatabase();
        eventoClick();
    }

    private void eventoClick() {
        somar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ObjSenha senha = new ObjSenha();

                int tmpInt = Integer.parseInt(tmp);

                tmpInt++;

                tmp = Integer.toString(tmpInt);

                senha.setSenhaExibida(tmp);
                referencia.child("Senha").child(salaoSelecionado).setValue(senha);

                eventoDatabase();
            }
        });


    }


    private void eventoDatabase() {
        referencia.child("Senha").child(salaoSelecionado).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listSalao.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    tmp = objSnapshot.getValue(String.class);
                    ObjSenha s = new ObjSenha();
                    s.setSenhaExibida(tmp);
                    listSalao.add(s);
                }
                arrayAdapterSalao = new ArrayAdapter<ObjSenha>(SalaoSelecionado.this,
                        android.R.layout.simple_list_item_1, listSalao);
                listarNumero.setAdapter(arrayAdapterSalao);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void iniciarFirebase() {

        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(SalaoSelecionado.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        referencia = firebaseDatabase.getReference();
    }


    public void subSenha(View view) {


    }
}

