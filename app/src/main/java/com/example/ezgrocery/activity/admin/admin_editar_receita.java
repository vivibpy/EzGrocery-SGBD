package com.example.ezgrocery.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.model.Receita;

public class admin_editar_receita extends AppCompatActivity {

    private Button cadastrarbtt;
    private EditText nometxt;
    private EditText descricaotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editar_receita);
        cadastrarbtt = findViewById(R.id.cadastroReceitabtt);
        nometxt = findViewById(R.id.nomereceitatxt);
        descricaotxt = findViewById(R.id.descricaoreceitatxt);

        Intent intent = getIntent();
        final int receitaId = intent.getExtras().getInt("receitaId");

        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        Receita rec = bd.obterReceita(receitaId);

        nometxt.setText(rec.Nome);
        descricaotxt.setText(rec.Descricao);

        cadastrarbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()) {
                    bd.atualizarReceita(receitaId, nometxt.getText().toString(),descricaotxt.getText().toString());

                    Intent intent = new Intent(admin_editar_receita.this, admin_detalhe_receita.class);
                    intent.putExtra("receitaId", receitaId);
                    startActivity(intent);
                } else{
                    Toast.makeText(admin_editar_receita.this,"Favor preencha todos os campos para seguir com o cadastro",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    private boolean validaCampos()
    {
        if(nometxt.getText().toString().matches("") || descricaotxt.getText().toString().matches(""))
        {
            return false;
        }
        else{
            return true;
        }
    }

}
