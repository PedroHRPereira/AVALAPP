package com.example.eryck.avalapp.Jogos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eryck.avalapp.Auxiliares.Conexao;
import com.example.eryck.avalapp.Auxiliares.Usuario;
import com.example.eryck.avalapp.Avaliacao.Avaliacao;
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


public class Jogo extends AppCompatActivity {

    private ImageButton btnClassificar;
    private FirebaseAuth firebaseAuth;
    private TextView txtAvaliado, txtQtdUsuarios;
    private FirebaseUser user;
    private Double soma, media;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        inicializaFuncao();
        eventoClick();
        quantidadeAvaliacao();
        mediaNota();
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
                    soma = 0.0;
                    media = 0.0;

                    // Gera um contador onde as notas dos usuarios serão resgatadas
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        listaUsuarios.add(u);
                    }

                    // Faz o resgate de todas as notas atribuiadas ao jogo e as soma
                    for (Usuario u : listaUsuarios) {
                        soma += Double.valueOf(u.getNota1());
                    }

                    // Cálculo da média das notas atribuidas ao jogo
                    media = soma / listaUsuarios.size();
                    String txt = String.valueOf(media);
                    txtAvaliado.setText(txt);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alert("Falha ao Carregar a Nota do Jogo!");
                }
            });
        }
    }

    private void quantidadeAvaliacao() {

        // Parâmetro de Iniciação do usuário pelo método de retorno à sua autenticação
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Teste da condição de o Usuário estar logado
        if (user == null) {
            finish();

        } else {

            // Método de Contagem da Quantidade de Usuários que Avaliaram o Jogo
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int contador = (int) dataSnapshot.getChildrenCount();
                    txtQtdUsuarios.setText("" + contador);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    alert("Falha ao Carregar a Nota do Jogo!");
                }
            });
        }
    }

    // Evento de Click do Botão para ir a Tela de Avaliação
    private void eventoClick() {
        btnClassificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Jogo.this, Avaliacao.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Classe Responsável por inicializar todos os componentes da Tela
    private void inicializaFuncao() {

        btnClassificar = (ImageButton) findViewById(R.id.btnClassificar);
        txtAvaliado = (TextView) findViewById(R.id.txtAvaliado);
        txtQtdUsuarios = (TextView) findViewById(R.id.txtQtdUsuarios);

    }

    /*Função de Criação de Alerta*/
    private void alert(String msg) {

        Toast.makeText(Jogo.this, msg, Toast.LENGTH_LONG).show();
    }

    // Método para manter as Credenciais do Usuário Instanciadas
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }

}

