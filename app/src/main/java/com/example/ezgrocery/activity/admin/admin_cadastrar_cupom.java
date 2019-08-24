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

public class admin_cadastrar_cupom extends AppCompatActivity {

    private Button cadastrarbtt;
    private EditText descricaotxt;
    private EditText valorxt;
    private EditText codigotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cadastrar_cupom);
        cadastrarbtt = findViewById(R.id.cadastrarCupomBtt);
        descricaotxt = findViewById(R.id.descricaocupomtxt);
        valorxt = findViewById(R.id.valorcupomtxt);
        codigotxt = findViewById(R.id.codigocupomtxt);

        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        cadastrarbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()) {

                    int valor = Integer.parseInt(valorxt.getText().toString());
                    bd.cadastrarCupom(descricaotxt.getText().toString(),codigotxt.getText().toString(),valor);

                    Intent intent = new Intent(admin_cadastrar_cupom.this, admin_lista_cupoms.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(admin_cadastrar_cupom.this,"Favor preencha todos os campos para seguir com o cadastro",Toast.LENGTH_LONG).show();

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
