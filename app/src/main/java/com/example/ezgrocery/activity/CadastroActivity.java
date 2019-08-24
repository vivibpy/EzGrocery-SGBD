package com.example.ezgrocery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.model.Usuario;
import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button btCadastrar;
    private Usuario usuario;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //TODO //adicionar os outros campos da classe usuario
        email = findViewById(R.id.emailtxt);
        senha = findViewById(R.id.senhatxt);
        btCadastrar = findViewById(R.id.cadastrobtt);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validaCampos()) {
                    Toast.makeText(CadastroActivity.this,"Favor preencha todos os campos para seguir com o cadastro",Toast.LENGTH_LONG).show();
                }else
                {
                    usuario = new Usuario(":D", email.getText().toString(), senha.getText().toString());
                    cadastrarUsuario();
                }
            }
        });

    }

    private void cadastrarUsuario()
    {
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        final OperacaoBancoDeDados op = new OperacaoBancoDeDados(this);

        //Gravar no auth
        auth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(CadastroActivity.this,"Usuário cadastrado com sucesso",Toast.LENGTH_SHORT).show();

                            //Gravar no banco
                            usuario.setId(task.getResult().getUser().getUid());
                            usuario.salvar();

                            op.cadastrarCarteiraVazia(usuario.getId());

                            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else
                        {
                            String exception = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                exception = "Digite uma senha mais forte, contendo mais caracteres, com letras e números";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                exception = "O e-mail digitado é inválido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                exception = "O e-mail já está existe, utilize um outro";
                            } catch (Exception e) {
                                exception = "Erro ao efetuar o cadastro";
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this,"Erro: " + exception,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validaCampos()
    {
        if(email.getText().toString().matches("") || senha.getText().toString().matches(""))
        {
            return false;
        }
        else{
            return true;
        }
    }
}