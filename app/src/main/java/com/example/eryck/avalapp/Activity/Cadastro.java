package com.example.eryck.avalapp.Activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eryck.avalapp.Auxiliares.Base64Custom;
import com.example.eryck.avalapp.Auxiliares.Conexao;
import com.example.eryck.avalapp.Auxiliares.Preferencias;
import com.example.eryck.avalapp.Auxiliares.Usuario;
import com.example.eryck.avalapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class Cadastro extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha, editTelefone, editCidade, editEstado, editConfSenha;
    private Button btnCadastro, btnVoltar;
    private ImageButton imgAddPerfil;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference perfilReference;
    private String downloadImagem;
    private Usuario usuario;
    private Uri imagemPerfil;
    private static final int RC_PHOTO_PICKER =  1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicio da referencia ao Storage Firebase
        firebaseStorage = FirebaseStorage.getInstance();
        perfilReference = firebaseStorage.getReference().child("Fotos Perfil");

        inicializaFuncao();
        eventoClick();
    }

    /*Função do Evento de Ação ao Clicar no Botão Registrar e Voltar
    * Também ao evento de Carregamento da Imagem de Perfil*/
    private void eventoClick() {
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editSenha.getText().toString().equals(editConfSenha.getText().toString())) {

                    usuario = new Usuario();
                    usuario.setNome(editNome.getText().toString());
                    usuario.setSenha(editSenha.getText().toString());
                    usuario.setEmail(editEmail.getText().toString());
                    usuario.setCidade(editCidade.getText().toString());
                    usuario.setEstado(editEstado.getText().toString());
                    usuario.setTelefone(editTelefone.getText().toString());

                    criarUser();

                } else {
                    alert("Senhas Não Correspondem. Digite a mesma Senha nos dois campos!");
                }
            }
        });

        imgAddPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete a Ação Usando..."), RC_PHOTO_PICKER);
            }
        });
    }

    // Função de Resgate da Imagem Selecionada aplicada ao Botão ImagemPerfil
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null){

            imagemPerfil = data.getData();
            imgAddPerfil.setImageURI(imagemPerfil);
        }
    }


    // Função responsãvel por Adicionar a Imagem Selecionada ao FirebaseStorage

    private void adcionaFoto() {

        if (imagemPerfil == null) {
            // Se a imagem do perfil for nula não avança o cadastro.
            Toast.makeText(this, "Uma Imagem deve ser adicionada ao Perfil!", Toast.LENGTH_SHORT).show();

        } else {
            // Seleciona a referência ao FirebaseStorage e adiciona a nova foto no proximo local
            final StorageReference fotoRef = perfilReference.child(imagemPerfil.getLastPathSegment());

            final UploadTask uploadTask = fotoRef.putFile(imagemPerfil);

            // Upload da foto do Perfil ao FirebaseStorage
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    alert("Erro ao Adicionar uma Imagem ao Perfil!");

                }

                // Método de Teste de Sucesso da Tentativa de Adicionar a Foto ao Banco
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    alert("Foto enviada ao Banco de Dados com Sucesso...");

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {

                                throw task.getException();
                            }

                            // Adicionando a Url da Foto para uma String e Salvando no Storage
                            downloadImagem = fotoRef.getDownloadUrl().toString();
                            return fotoRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {

                                // Adicionando a Url da Foto para uma String e Salvando no Storage
                                downloadImagem = task.getResult().toString();

                                // Atualizando a Foto de Perfil do Usuário
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                // Método de chamada da atualização da Foto de Perfil do Usuário
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(Uri.parse(downloadImagem))
                                        .build();
                                if (user != null) {
                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                alert("Foto de Perfil adicionada com Sucesso!");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            });
        }
    }

   /*Função de Criação de um novo usuário com email e senha pelo Authentication*/
    private void criarUser() {
        firebaseAuth = Conexao.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            alert("Usuário cadastrado com Sucesso!");

                            String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());

                            firebaseUser = Objects.requireNonNull(task.getResult()).getUser();

                            usuario.setId(firebaseUser.getUid());
                            usuario.salvar();

                            Preferencias preferencias = new Preferencias(Cadastro.this);
                            preferencias.salvarUsuarioPrefencias(identificadorUsuario, usuario.getNome());

                            atualizaNome();
                            adcionaFoto();

                            Intent i = new Intent(Cadastro.this, Login.class);
                            startActivity(i);
                            finish();

                        } else {

                            String erroExcecao = "";
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = " Digite uma senha mais forte, contendo no mínimo 8 caracteres de letras e números";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = " E-mail inválido, digite um novo e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "Esse e-mail já está cadastrado, tente outro e-mail!";
                            } catch (Exception e) {
                                erroExcecao = "Erro ao efetuar o cadastro, tente novamente!";
                                e.printStackTrace();
                            }
                            alert("Erro: " + erroExcecao);
                        }
                    }

                });
    }


    // Funcão que atualiza o Nome do Usuário para o User Authentication
    private void atualizaNome(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(editNome.getText().toString())
                .build();
        if(user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                alert("Nome do Usuário atualizado com Sucesso!");
                            }
                        }
                    });
        }
    }

    /*Função de Criação de Alerta*/
    private void alert(String msg) {

        Toast.makeText(Cadastro.this, msg, Toast.LENGTH_SHORT).show();
    }


    /* Inicia as funções dos botões como instancias R pelo Id*/
    private void inicializaFuncao() {

        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        editNome = (EditText) findViewById(R.id.editNome);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        editCidade = (EditText) findViewById(R.id.editCidade);
        editEstado = (EditText) findViewById(R.id.editEstado);
        editConfSenha = (EditText) findViewById(R.id.editConfSenha);
        btnCadastro = (Button) findViewById(R.id.btnCadastro);
        btnVoltar = (Button) findViewById(R.id.btnVoltar);
        imgAddPerfil = (ImageButton) findViewById(R.id.imgAddPerfil);
    }

}
