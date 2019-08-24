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
import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.Usuario;

public class AdicionarSaldoActivity extends AppCompatActivity {

    private Button addSaldoBtn;
    private EditText saldoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_saldo);

        addSaldoBtn = findViewById(R.id.adicionarSaldoBtt);
        saldoTxt = findViewById(R.id.saldoTxt);

        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        final Usuario usu = UsuarioLogado.obtemUsuarioLogado();

        addSaldoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos())
                {
                    int valorAAdicionar = Integer.parseInt(saldoTxt.getText().toString());

                    CarteiraVirtual carta = bd.obterCarteira(usu.getId());

                    int novoValor = carta.valor + valorAAdicionar;

                    bd.atualizarCarteira(carta.id, novoValor);

                    Intent intent = new Intent(AdicionarSaldoActivity.this, ListaListaProdutosActivity.class);
                    startActivity(intent);
                } else
                {
                    Toast.makeText(AdicionarSaldoActivity.this,"Favor preencha todos os campos para seguir",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private boolean validaCampos()
    {
        if(saldoTxt.getText().toString().matches("") )
        {
            return false;
        }
        else{
            return true;
        }
    }
}
