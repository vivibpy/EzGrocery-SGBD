package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.activity.admin.admin_cadastrar_produto;
import com.example.ezgrocery.activity.admin.admin_lista_produto;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.CupomDesconto;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Usuario;

public class ResgatarCodigoActivity extends AppCompatActivity {

    private Button resgatarbtt;
    private EditText codigotxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgatar_codigo);

        resgatarbtt = findViewById(R.id.resgatarCodigoBtt);
        codigotxt = findViewById(R.id.codigotxt);


        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        resgatarbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validaCampos()) {

                    String codigo = codigotxt.getText().toString();
                    String[] arrayCodigo = codigo.split("-", -1);

                    //L-11-11-11-55-Nome
                    if(arrayCodigo.length == 0 || (arrayCodigo[0].equals("L") == false && arrayCodigo[0].equals("D") == false) )
                    {
                        Toast.makeText(ResgatarCodigoActivity.this,"Código Inválido!",Toast.LENGTH_LONG).show();
                    }
                    else {

                        if(arrayCodigo[0].equals("L"))
                        {
                            Usuario user = UsuarioLogado.obtemUsuarioLogado();

                            int tamanho = arrayCodigo.length;
                            String nomeLista = arrayCodigo[tamanho - 1];

                            bd.cadastrarListaProdutos(nomeLista,user.getId());
                            ListadeProdutos listadeProdutos = bd.obterUltimaListadeProdutos();

                            for(int i = 1; i<tamanho -1; i++)
                            {
                                int idProduto = Integer.parseInt(arrayCodigo[i]);
                                Produto produto = bd.obterProduto(idProduto);

                                if(produto != null) {
                                    bd.cadastrarProdutosALista(listadeProdutos.id, idProduto);
                                }
                            }

                            Intent intent = new Intent(ResgatarCodigoActivity.this, ListaListaProdutosActivity.class);
                            startActivity(intent);
                        }
                        else if(arrayCodigo[0].equals("D"))
                        {
                            String codigoText = arrayCodigo[1];

                            CupomDesconto cup = bd.obterCupom(codigoText);

                            if(cup != null)
                            {
                                Usuario usu = UsuarioLogado.obtemUsuarioLogado();

                                CarteiraVirtual carta = bd.obterCarteira(usu.getId());

                                boolean jaRecuperado = bd.cupomJaAdicionadoNaCarteira(cup.id,carta.id);

                                if(jaRecuperado == false)
                                {
                                    int novoSaldo = carta.valor + cup.valor;

                                    bd.cadastrarCupomNaCarteira(carta.id,cup.id);
                                    bd.atualizarCarteira(carta.id, novoSaldo);

                                    String texto = cup.descricao + " resgatado. Foram adicionados R$ " + cup.valor + " à sua carteira como desconto";
                                    Toast.makeText(ResgatarCodigoActivity.this,texto,Toast.LENGTH_LONG).show();
                                }else
                                {
                                    String texto = "Inválido!! Esse desconto já foi resgatado anteriormente para sua carteira.";
                                    Toast.makeText(ResgatarCodigoActivity.this,texto,Toast.LENGTH_LONG).show();
                                }

                            }else
                            {
                                Toast.makeText(ResgatarCodigoActivity.this,"Código de cupom inválido",Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                } else{
                    Toast.makeText(ResgatarCodigoActivity.this,"Favor preencha todos os campos para seguir com o cadastro",Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private boolean validaCampos()
    {
        if(codigotxt.getText().toString().matches("") )
        {
            return false;
        }
        else{
            return true;
        }
    }
}
