package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.model.CarteiraVirtual;

public class CadastroTelefone extends AppCompatActivity {

    private Button autenticarPhoneBtn;
    private EditText phoneTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_telefone);

        autenticarPhoneBtn = findViewById(R.id.cadastrarTelefoneBtt);
        phoneTxt = findViewById(R.id.telefoneTxt);

        autenticarPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos())
                {
                    Intent intent = new Intent(CadastroTelefone.this, AutenticarTelefone.class);
                    intent.putExtra("phoneNumber", phoneTxt.getText().toString());
                    startActivity(intent);
                } else
                {
                    Toast.makeText(CadastroTelefone.this,"Favor preencha todos os campos para seguir",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validaCampos()
    {
        if(phoneTxt.getText().toString().matches("") )
        {
            return false;
        }
        else{
            return true;
        }
    }
}
