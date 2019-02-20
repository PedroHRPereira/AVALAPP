package com.example.eryck.avalapp.Avaliacao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.eryck.avalapp.Activity.Perfil;
import com.example.eryck.avalapp.Auxiliares.Conexao;
import com.example.eryck.avalapp.Auxiliares.Usuario;
import com.example.eryck.avalapp.Jogos.Jogo4;
import com.example.eryck.avalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Avaliacao4 extends AppCompatActivity {
    private Button btnVoltar, btnSalvar;
    private RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4, ratingBar5, ratingBar6, ratingBar7,
            ratingBar8, ratingBar9, ratingBar10, ratingBar11, ratingBar12, ratingBar13, ratingBar14, ratingBar15,
            ratingBar16, ratingBar17, ratingBar18, ratingBar19, ratingBar20;
    private DatabaseReference databaseReference;
    private Double aval;
    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao4);

        inicializaFuncao();
        eventoClick();
    }

    private void eventoClick() {

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Avaliacao4.this, Jogo4.class);
                startActivity(i);
                finish();

            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Se qualquer perguntar deixar de ser avaliada não será gerada a nota.
                if (ratingBar1.getRating() == 0 || ratingBar2.getRating() == 0 ||
                        ratingBar3.getRating() == 0 || ratingBar4.getRating() == 0 ||
                        ratingBar5.getRating() == 0 || ratingBar6.getRating() == 0 ||
                        ratingBar7.getRating() == 0 || ratingBar8.getRating() == 0 ||
                        ratingBar9.getRating() == 0 || ratingBar10.getRating() == 0 ||
                        ratingBar11.getRating() == 0 || ratingBar12.getRating() == 0 ||
                        ratingBar13.getRating() == 0 || ratingBar14.getRating() == 0 ||
                        ratingBar15.getRating() == 0 || ratingBar16.getRating() == 0 ||
                        ratingBar17.getRating() == 0 || ratingBar18.getRating() == 0 ||
                        ratingBar19.getRating() == 0 || ratingBar20.getRating() == 0) {

                    alert("Avalie todos os itens! A nota só será atribuida ao Jogo depois disso! :)");

                } else {

                    // M[étodo para geração da nota do jogo
                    double peso = 1.5d;
                    aval = ((ratingBar1.getRating() * peso + ratingBar2.getRating() * peso + ratingBar3.getRating() * peso + ratingBar4.getRating() * peso +
                            ratingBar5.getRating() * peso + ratingBar6.getRating() * peso + ratingBar7.getRating() * peso + ratingBar8.getRating() +
                            ratingBar9.getRating() + ratingBar10.getRating() + ratingBar11.getRating() + ratingBar12.getRating() +
                            ratingBar13.getRating() + ratingBar14.getRating() + ratingBar15.getRating() + ratingBar16.getRating() +
                            ratingBar17.getRating() + ratingBar18.getRating() + ratingBar19.getRating() + ratingBar20.getRating()) / 23.5d);

                    // Instanciando o usuario por meio da autenticação
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    usuario = new Usuario();
                    DecimalFormat df = new DecimalFormat("0.#");
                    Double nota = Double.valueOf(df.format(aval).replace(".", "").replace(",", "."));
                    usuario.setNota4("" + nota);

                    // Referencia ao banco de dados onde será salva a nota
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("Usuario").child(user.getUid()).child("nota1").setValue(usuario.getNota4());

                    Intent i = new Intent(Avaliacao4.this, Perfil.class);
                    startActivity(i);
                    finish();

                }
            }
        });

    }


    private void inicializaFuncao() {

        // Botões de Salvar Avaliação e Voltar a Tela
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);

        // Botões de Avaliação dos Jogos por meio de estrelas de 1 a 10
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar2 = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar3 = (RatingBar) findViewById(R.id.ratingBar3);
        ratingBar4 = (RatingBar) findViewById(R.id.ratingBar4);
        ratingBar5 = (RatingBar) findViewById(R.id.ratingBar5);
        ratingBar6 = (RatingBar) findViewById(R.id.ratingBar6);
        ratingBar7 = (RatingBar) findViewById(R.id.ratingBar7);
        ratingBar8 = (RatingBar) findViewById(R.id.ratingBar8);
        ratingBar9 = (RatingBar) findViewById(R.id.ratingBar9);
        ratingBar10 = (RatingBar) findViewById(R.id.ratingBar10);
        ratingBar11 = (RatingBar) findViewById(R.id.ratingBar11);
        ratingBar12 = (RatingBar) findViewById(R.id.ratingBar12);
        ratingBar13 = (RatingBar) findViewById(R.id.ratingBar13);
        ratingBar14 = (RatingBar) findViewById(R.id.ratingBar14);
        ratingBar15 = (RatingBar) findViewById(R.id.ratingBar15);
        ratingBar16 = (RatingBar) findViewById(R.id.ratingBar16);
        ratingBar17 = (RatingBar) findViewById(R.id.ratingBar17);
        ratingBar18 = (RatingBar) findViewById(R.id.ratingBar18);
        ratingBar19 = (RatingBar) findViewById(R.id.ratingBar19);
        ratingBar20 = (RatingBar) findViewById(R.id.ratingBar20);

    }

    /*Função de Criação de Alerta*/
    private void alert(String msg) {

        Toast.makeText(Avaliacao4.this, msg, Toast.LENGTH_SHORT).show();
    }

    // Método para manter as Credenciais do Usuário Instanciadas
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }

}
