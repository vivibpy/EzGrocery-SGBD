package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.activity.admin.admin_ListaReceitas;
import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
private EditText emailtxt;
private EditText senhatxt;
private Button logarbtt;
private Usuario usuario;
private FirebaseAuth autenticacao;
private Button cadastrobtt;
private Button logarTelefonebtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Redireciona para a proxima pagina se usuario ja estiver logado
        verificarUsuarioLogado();

        emailtxt = findViewById(R.id.emailtxt);
        senhatxt = findViewById(R.id.senhatxt);
        logarbtt = findViewById(R.id.logarbtt);
        cadastrobtt = findViewById(R.id.cadastrobtt);
        logarTelefonebtt = findViewById(R.id.logarTelefonebtt);

        logarbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validaCampos())
                {
                    Toast.makeText(MainActivity.this,"Favor entre seu usuario e senha",Toast.LENGTH_LONG).show();
                } else {
                    usuario = new Usuario();
                    usuario.setEmail(emailtxt.getText().toString());
                    usuario.setSenha(senhatxt.getText().toString());
                    validarLogin();
                }
            }
        });
        cadastrobtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CadastroActivity.class);
                startActivity(intent);
            }
        });

        logarTelefonebtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CadastroTelefone.class);
                startActivity(intent);
            }
        });
    }

    private void verificarUsuarioLogado()
    {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null)
        {
            Usuario user = UsuarioLogado.obtemUsuarioLogado();
            if(!user.getEmail().equals("admin@gmail.com")) {
                abrirTelaPrincipal();
            }
            else {
                abrirTelaAdm();
            }
        }
    }

    private void validarLogin()
    {
        final OperacaoBancoDeDados op = new OperacaoBancoDeDados(this);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Usuario user = UsuarioLogado.obtemUsuarioLogado();

                            CarteiraVirtual carta = op.obterCarteira(user.getId());

                            if(carta == null)
                            {
                                op.cadastrarCarteiraVazia(user.getId());
                            }

                            if(!user.getEmail().equals("admin@gmail.com")) {
                                abrirTelaPrincipal();
                            }
                            else{
                                abrirTelaAdm();
                            }
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Não foi possivel fazer o login, verifique seus dados",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void abrirTelaPrincipal()
    {
        Intent intent = new Intent(MainActivity.this, PagPrincipalActivity.class);
        startActivity(intent);
    }

    private void abrirTelaAdm()
    {
        Intent intent = new Intent(MainActivity.this, admin_ListaReceitas.class);
        startActivity(intent);
    }

    public void abrirCadastroUsuario(View view)
    {
        Intent intent = new Intent(MainActivity.this,CadastroActivity.class);
        startActivity(intent);
    }

    private boolean validaCampos()
    {
        if(emailtxt.getText().toString().matches("") || senhatxt.getText().toString().matches(""))
        {
            return false;
        }
        else{
            return true;
        }
    }

}
