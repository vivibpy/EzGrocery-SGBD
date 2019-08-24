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
import com.example.ezgrocery.model.Produto;

public class admin_cadastrar_produto extends AppCompatActivity {

    private Button cadastrarrbtt;
    private EditText nometxt;
    private EditText precotxt;
    private EditText codigotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cadastrar_produto);

        cadastrarrbtt = findViewById(R.id.cadastrarProdutoBtt);
        nometxt = findViewById(R.id.nomeprodutotxt);
        precotxt = findViewById(R.id.precoprodutotxt);
        codigotxt = findViewById(R.id.codigoprodutotxt);


        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        cadastrarrbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()) {

                    int preco = Integer.parseInt(precotxt.getText().toString());
                    bd.cadastrarProduto(nometxt.getText().toString(),codigotxt.getText().toString(),preco);

                    Intent intent = new Intent(admin_cadastrar_produto.this, admin_lista_produto.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(admin_cadastrar_produto.this,"Favor preencha todos os campos para seguir com o cadastro",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private boolean validaCampos()
    {
        if(nometxt.getText().toString().matches("") || precotxt.getText().toString().matches("") ||
                codigotxt.getText().toString().matches("") )
        {
            return false;
        }
        else{
            return true;
        }
    }
}
