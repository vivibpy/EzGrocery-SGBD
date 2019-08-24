package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ezgrocery.R;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Receita;
import com.example.ezgrocery.model.Usuario;

public class DetalheReceitaActivity extends AppCompatActivity {

    private TextView nometxt;
    private TextView descricaotxt;
    private Button compartilharbtt;
    private Button comprarbtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_receita);

        nometxt = findViewById(R.id.titulotxt);
        descricaotxt = findViewById(R.id.descricaotxt);
        compartilharbtt = findViewById(R.id.compartilharbtt);
        comprarbtt = findViewById(R.id.comprarbtt);

        //Banco dados
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        Intent intent = getIntent();
        int receitaId = intent.getExtras().getInt("receitaId");

        final Receita rec = bd.obterReceita(receitaId);

        if(rec != null){
            nometxt.setText(rec.Nome);
            descricaotxt.setText(rec.Descricao);
        }

        compartilharbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textToShare = nometxt.getText().toString() + "\n\n" + descricaotxt.getText().toString();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,textToShare);
                startActivity(Intent.createChooser(shareIntent, "Compartilhar Receita..."));
            }
        });

        comprarbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Usuario user = UsuarioLogado.obtemUsuarioLogado();
                bd.cadastrarListaProdutos(rec.listadeProdutos.nome, user.getId());

                ListadeProdutos lp = bd.obterUltimaListadeProdutos();

                for(Produto p : rec.listadeProdutos.item)
                {
                    bd.cadastrarProdutosALista(lp.id, p.id);
                }

                Intent intent = new Intent(DetalheReceitaActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
            }
        });
    }

}
