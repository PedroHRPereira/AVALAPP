package com.example.eryck.avalapp.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eryck.avalapp.Auxiliares.Conexao;
import com.example.eryck.avalapp.Auxiliares.Usuario;
import com.example.eryck.avalapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetSenha extends AppCompatActivity {

    private EditText editEmail;
    private Button btnResetSenha;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_senha);
        inicializaFuncao();
        eventoClick();
    }

    private void eventoClick() {

        btnResetSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(editEmail.getText().toString());
                resetSenha();
                finish();
            }
        });

    }

    private void resetSenha(){

        firebaseAuth = Conexao.getFirebaseAuth();
        firebaseAuth.sendPasswordResetEmail(usuario.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    alert("O Email foi encaminhado para que você altere sua senha! ");

                }else{
                    alert("Email vazio, digite um email válido para a solicitação!");
                }
            }
        });
    }

    private void alert(String s) {

        Toast.makeText(ResetSenha.this,s,Toast.LENGTH_SHORT).show();
    }

    private void inicializaFuncao() {

        editEmail = (EditText) findViewById(R.id.editEmail);
        btnResetSenha = (Button) findViewById(R.id.btnResetSenha);
    }
}
