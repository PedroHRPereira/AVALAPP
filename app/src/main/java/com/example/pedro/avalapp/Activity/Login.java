package com.example.pedro.avalapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedro.avalapp.Auxiliares.Conexao;
import com.example.pedro.avalapp.Auxiliares.Usuario;
import com.example.pedro.avalapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private Button btnEntrar, btnRegistrar;
    private TextView txtResetSenha;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;


    /*Classe Principal de inicialização dos metodos do programa*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializaFuncao();
        eventoClick();

    }

    /*Função do Evento Clicar no Botão Registrar*/
    private void eventoClick() {

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Cadastro.class);
                startActivity(i);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editEmail.getText().toString().equals("") && !editSenha.getText().toString().equals("")) {

                    usuario = new Usuario();
                    usuario.setEmail(editEmail.getText().toString());
                    usuario.setSenha(editSenha.getText().toString());
                    validarLogin();

                } else {
                    alert("Preencha os campos e-mail e senha!");
                }
            }
        });

        txtResetSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ResetSenha.class);
                startActivity(i);
            }
        });

    }

    private void validarLogin() {

        firebaseAuth = Conexao.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    alert("Login Efetuado com Sucesso!");
                    Intent i = new Intent(Login.this, Perfil.class);
                    startActivity(i);
                    finish();
                } else {

                    alert("Email ou Senha Incorretos!");
                }
            }
        });
    }

    private void alert(String s) {
        Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
    }

    /* Inicia as funções dos botões como instancias*/
    private void inicializaFuncao() {

        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        txtResetSenha = (TextView) findViewById(R.id.txtResetSenha);

    }

}
