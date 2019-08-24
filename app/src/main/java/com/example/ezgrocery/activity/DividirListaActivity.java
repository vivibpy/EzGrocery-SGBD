package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;

import java.util.ArrayList;

public class DividirListaActivity extends AppCompatActivity {

    private Button dividir;
    private ListView listaProdutosMinha;
    private ListView listaProdutosAmiguinho;

    final ArrayList<Produto> produtosDoAmiguinho = new ArrayList<>();

    ArrayAdapter<String> itensAdaptador;
    ArrayList<String> itens = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();

    ArrayAdapter<String> itensAdaptador2;
    ArrayList<String> itens2 = new ArrayList<>();
    ArrayList<Integer> ids2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividir_lista);

        listaProdutosMinha = findViewById(R.id.listaMeusProdutos);
        listaProdutosAmiguinho = findViewById(R.id.listaSeusProdutos);
        dividir = findViewById(R.id.dividirBtt);

        Intent intent = getIntent();
        final int listaId = intent.getExtras().getInt("listaId");


        //Banco dados
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        carregarMinhaLista();
        carregarAmigoLista(produtosDoAmiguinho);

        listaProdutosMinha.setLongClickable(true);
        final ArrayList<Integer> produtosIds = ids;

        listaProdutosMinha.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Produto prod = bd.obterProduto(produtosIds.get(position));
                produtosDoAmiguinho.add(prod);

                bd.deletarProdutoDeUmaLista(listaId,produtosIds.get(position));
                carregarMinhaLista();
                carregarAmigoLista(produtosDoAmiguinho);
                return true;
            }
        });


        dividir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListadeProdutos lista = bd.obterListadeProdutos(listaId);

                String codigo = "L-";

                for(Produto p : produtosDoAmiguinho)
                {
                    codigo += p.id+"-";
                }

                codigo += ""+lista.nome;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Olá, dividi uma receita com voce pelo EzGrocery. Insira este codigo na area de cupons" +
                        " do aplicativo:\n\n" + codigo);
                startActivity(Intent.createChooser(shareIntent, "Compartilhar Lista..."));
            }
        });
    }


    private void carregarMinhaLista()
    {
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        Intent intent = getIntent();
        final int listaId = intent.getExtras().getInt("listaId");

        ListadeProdutos listadeProdutos = bd.obterListadeProdutos(listaId);

        itens.clear();
        ids.clear();

        for (Produto p : listadeProdutos.item) {

            String produtoLinha = p.nome + " - Preço R$" + p.preco;
            itens.add(produtoLinha);
            ids.add(p.id);
        }

        itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                itens);

        listaProdutosMinha.setAdapter(itensAdaptador);
    }


    private void carregarAmigoLista(ArrayList<Produto> prods)
    {

        itens2.clear();
        ids2.clear();

        for (Produto p : prods) {

            String produtoLinha = p.nome + " - Preço R$" + p.preco;
            itens2.add(produtoLinha);
            ids2.add(p.id);
        }

        itensAdaptador2 = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                itens2);

        listaProdutosAmiguinho.setAdapter(itensAdaptador2);
    }
}
