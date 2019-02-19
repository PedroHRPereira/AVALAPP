package com.example.eryck.avalapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eryck.avalapp.Auxiliares.Conexao;
import com.example.eryck.avalapp.Auxiliares.Usuario;
import com.example.eryck.avalapp.Jogos.Jogo;
import com.example.eryck.avalapp.Jogos.Jogo2;
import com.example.eryck.avalapp.Jogos.Jogo3;
import com.example.eryck.avalapp.Jogos.Jogo4;
import com.example.eryck.avalapp.Jogos.Jogo5;
import com.example.eryck.avalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    private TextView txtNome, txtEmail, txtNota1, txtNota2, txtNota3, txtNota4, txtNota5;
    private TextView txtRanking1, txtRanking2, txtRanking3, txtRanking4, txtRanking5;
    private Button btnLogout;
    private ImageView imgPerfil;
    private Button btnJogo1, btnJogo2, btnJogo3, btnJogo4, btnJogo5;
    private Button btnRanking1, btnRanking2, btnRanking3, btnRanking4, btnRanking5;
    private Button btnAval1, btnAval2, btnAval3, btnAval4, btnAval5;
    private Double soma1, soma2, soma3, soma4, soma5, media1, media2, media3, media4, media5;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        incializaFuncao();
        eventoClick();
        mostraNota();
        mediaNota();

    }

    private void mostraNota() {

        // Parâmetro de Iniciação do usuário pelo método de retorno à sua autenticação
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();

        } else {

            // Views para recuperar o Nome, o Email e a Foto de Perfil do Usuário
            txtEmail.setText("Email: " + user.getEmail());
            txtNome.setText("Bem Vindo " + user.getDisplayName() + "!");
            Glide.with(this).load(user.getPhotoUrl()).into(imgPerfil);

            // Iniciando a Referência ao Banco de Dados para a recuperação das Notas
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuario").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // Mapa de dados contendo a chamada adas Notas de Avaliação do Usuário
                    if (dataSnapshot.exists()) {
                        Map<String, String> mapa = (Map) dataSnapshot.getValue();

                        txtNota1.setText(mapa.get(String.valueOf("nota1")));
                        txtNota2.setText(mapa.get(String.valueOf("nota2")));
                        txtNota3.setText(mapa.get(String.valueOf("nota3")));
                        txtNota4.setText(mapa.get(String.valueOf("nota4")));
                        txtNota5.setText(mapa.get(String.valueOf("nota5")));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alert("Falha ao Carregar a Nota do Jogo!");
                }
            });
        }
    }

    private void mediaNota() {

        // Parâmetro de Iniciação do usuário pelo método de retorno à sua autenticação
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Lista de usuários para adicionar recebidos do banco
        final List<Usuario> listaUsuarios = new ArrayList<Usuario>();

        // Teste da condição de o Usuário estar logado
        if (user == null) {
            finish();

        } else {

            // Método de Contagem da Quantidade de Usuários que Avaliaram o Jogo
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Inicia a soma e a média das notas em 0
                    soma1 = 0.0; soma2 = 0.0; soma3 = 0.0; soma4 = 0.0; soma5 = 0.0;
                    media1 = 0.0; media2 = 0.0; media3 = 0.0; media4 = 0.0; media5 = 0.0;

                    // Gera um contador onde as notas dos usuarios serão resgatadas
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        listaUsuarios.add(u);
                    }

                    // Faz o resgate de todas as notas atribuiadas ao jogo e as soma
                    for (Usuario u : listaUsuarios) {
                        soma1 += Double.parseDouble(u.getNota1());
                        soma2 += Double.parseDouble(u.getNota2());
                        soma3 += Double.parseDouble(u.getNota3());
                        soma4 += Double.parseDouble(u.getNota4());
                        soma5 += Double.parseDouble(u.getNota5());
                    }

                    // Cálculo da média das notas atribuidas aos jogos
                    media1 = soma1 / listaUsuarios.size();
                    String txt1 = String.format("%.1f", media1);
                    txtRanking1.setText(txt1);

                    media2 = soma2 / listaUsuarios.size();
                    String txt2 = String.format("%.1f", media2);
                    txtRanking2.setText(txt2);

                    media3 = soma3 / listaUsuarios.size();
                    String txt3 = String.format("%.1f", media3);
                    txtRanking3.setText(txt3);

                    media4 = soma4 / listaUsuarios.size();
                    String txt4 = String.format("%.1f", media4);
                    txtRanking4.setText(txt4);

                    media5 = soma5 / listaUsuarios.size();
                    String txt5 = String.format("%.1f", media5);
                    txtRanking5.setText(txt5);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alert("Falha ao Carregar a Nota do Jogo!");
                }
            });
        }
    }

    private void eventoClick() {

        // Botões do Jogo 1
        btnJogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota1.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnJogo1.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }

            }
        });

        btnRanking1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota1.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnRanking1.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnAval1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota1.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnAval1.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        // Botões do Jogo 2
        btnJogo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota2.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnJogo2.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnRanking2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota2.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnRanking2.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnAval2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ativo = txtNota2.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnAval2.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        //Botões do Jogo 3
        btnJogo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota3.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnJogo3.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnRanking3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota3.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnRanking3.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnAval3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ativo = txtNota3.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnAval3.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        // Botões do Jogo 4
        btnJogo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota4.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnJogo4.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnRanking4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota4.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnRanking4.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnAval4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Ativo = txtNota4.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnAval4.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        //Botões do Jogo 5
        btnJogo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota5.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnJogo5.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnRanking5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota5.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnRanking5.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnAval5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Ativo = txtNota5.getText().toString().trim();
                if (!Ativo.isEmpty()){
                    btnAval5.setEnabled(false);
                    alert("Você já avaliou este jogo! Tente outro.");

                }else{
                    Intent i = new Intent(Perfil.this, Jogo.class);
                    startActivity(i);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void alert(String s) {
        Toast.makeText(Perfil.this, s, Toast.LENGTH_SHORT).show();
    }

    /* Inicia as funções dos botões como instancias*/
    private void incializaFuncao() {

        // TextViews do Usuário e Foto de Perfil
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtNome = (TextView) findViewById(R.id.txtNome);
        imgPerfil = (ImageView) findViewById(R.id.imgPerfil);

        // TextViews das Notas aplicadas pelos usuários
        txtNota1 = (TextView) findViewById(R.id.txtNota1);
        txtNota2 = (TextView) findViewById(R.id.txtNota2);
        txtNota3 = (TextView) findViewById(R.id.txtNota3);
        txtNota4 = (TextView) findViewById(R.id.txtNota4);
        txtNota5 = (TextView) findViewById(R.id.txtNota5);

        // TextViews das Medias das Notas aplicadas pelos usuários
        txtRanking1 = (TextView) findViewById(R.id.txtRanking1);
        txtRanking2 = (TextView) findViewById(R.id.txtRanking2);
        txtRanking3 = (TextView) findViewById(R.id.txtRanking3);
        txtRanking4 = (TextView) findViewById(R.id.txtRanking4);
        txtRanking5 = (TextView) findViewById(R.id.txtRanking5);

        // Botão Para Sair do App
        btnLogout = (Button) findViewById(R.id.btnLogout);

        // Botões de visualização das Telas dos Jogos Cadastrados
        btnJogo1 = (Button) findViewById(R.id.btnJogo1);
        btnJogo2 = (Button) findViewById(R.id.btnJogo2);
        btnJogo3 = (Button) findViewById(R.id.btnJogo3);
        btnJogo4 = (Button) findViewById(R.id.btnJogo4);
        btnJogo5 = (Button) findViewById(R.id.btnJogo5);

        // Botões de vizualização das Telas dos Jogos por meio dos Rankings
        btnRanking1 = (Button) findViewById(R.id.btnRanking1);
        btnRanking2 = (Button) findViewById(R.id.btnRanking2);
        btnRanking3 = (Button) findViewById(R.id.btnRanking3);
        btnRanking4 = (Button) findViewById(R.id.btnRanking4);
        btnRanking5 = (Button) findViewById(R.id.btnRanking5);

        // Botões de vizualização das Telas dos Jogos por meio dos Avaliados
        btnAval1 = (Button) findViewById(R.id.btnaval1);
        btnAval2 = (Button) findViewById(R.id.btnaval2);
        btnAval3 = (Button) findViewById(R.id.btnaval3);
        btnAval4 = (Button) findViewById(R.id.btnaval4);
        btnAval5 = (Button) findViewById(R.id.btnaval5);
    }

    // Método para manter as Credenciais do Usuário Instanciadas
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }

}
