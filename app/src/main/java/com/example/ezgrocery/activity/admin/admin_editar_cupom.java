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
import com.example.ezgrocery.model.CupomDesconto;
import com.example.ezgrocery.model.Produto;

public class admin_editar_cupom extends AppCompatActivity {

    private Button editarrbtt;
    private EditText descricaotxt;
    private EditText valorxt;
    private EditText codigotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editar_cupom);

        editarrbtt = findViewById(R.id.editarCupomBtt);
        descricaotxt = findViewById(R.id.descricaocupomtxt);
        valorxt = findViewById(R.id.valorcupomtxt);
        codigotxt = findViewById(R.id.codigocupomtxt);

        Intent intent = getIntent();
        final int cupomId = intent.getExtras().getInt("cupomId");

        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        CupomDesconto cup = bd.obterCupomPorId(cupomId);

        descricaotxt.setText(cup.descricao);
        codigotxt.setText(cup.codigo);
        valorxt.setText(cup.valor + "");

        editarrbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()) {

                    int valor = Integer.parseInt(valorxt.getText().toString());
                    bd.atualizarCupom(cupomId, descricaotxt.getText().toString(),codigotxt.getText().toString(),valor);

                    Intent intent = new Intent(admin_editar_cupom.this, admin_lista_cupoms.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(admin_editar_cupom.this,"Favor preencha todos os campos para seguir com a edição",Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    private boolean validaCampos()
    {
        if(descricaotxt.getText().toString().matches("") || valorxt.getText().toString().matches("") ||
                codigotxt.getText().toString().matches("") )
        {
            return false;
        }
        else{
            return true;
        }
    }
}
