package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Usuario;

import java.util.ArrayList;

public class CadastroListaActivity extends AppCompatActivity {

    private Button cadastrarListabtt;
    private EditText nomelistatxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_lista);

        cadastrarListabtt = findViewById(R.id.cadastroListabtt);
        nomelistatxt = findViewById(R.id.nomelistatxt);

        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        cadastrarListabtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()) {
                    ListadeProdutos lista = new ListadeProdutos(nomelistatxt.getText().toString(), new ArrayList<Produto>());

                    Usuario user = UsuarioLogado.obtemUsuarioLogado();

                    if(!user.getEmail().equals("admin@gmail.com")) {
                        bd.cadastrarListaProdutos(lista.nome, user.getId());
                    } else{
                        bd.cadastrarListaProdutos(lista.nome, null);

                        ListadeProdutos lp = bd.obterUltimaListadeProdutos();

                        Intent intent = getIntent();
                        int receitaId = intent.getExtras().getInt("receitaId");

                        if(receitaId != 0) {
                            bd.atualizarListaDeProdutosdaReceita(receitaId, lp.id);
                        }
                    }

                    ListadeProdutos lp = bd.obterUltimaListadeProdutos();

                    Intent intent = new Intent(CadastroListaActivity.this, DetalheListaProdutosActivity.class);
                    intent.putExtra("listaId", lp.id);
                    startActivity(intent);
                } else{
                    Toast.makeText(CadastroListaActivity.this,"Favor preencha todos os campos para seguir com o cadastro",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    private boolean validaCampos()
    {
        if(nomelistatxt.getText().toString().matches(""))
        {
            return false;
        }
        else{
            return true;
        }
    }
}
