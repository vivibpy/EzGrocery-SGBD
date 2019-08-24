package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ezgrocery.R;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;

import java.util.ArrayList;

public class ListaProdutosActivity extends AppCompatActivity {

    private ListView listasProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        //lista
        listasProdutos = findViewById(R.id.listaProdutos);

        Intent intent = getIntent();
        final int listaId = intent.getExtras().getInt("listaId");


        try {
            //Banco dados
            final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);
            ArrayList<Produto> listas  = bd.recuperarTodosProdutos();

            if(listas.isEmpty() == false){

                for(Produto p : listas){
                    String produtoLinha = p.nome + "- Preço R$" + p.preco;
                    itens.add(produtoLinha);

                    ids.add(p.id);
                }

                itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text2,
                        itens);

                listasProdutos.setAdapter( itensAdaptador );

                final ArrayList<Integer> finalIds = ids;

                listasProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        bd.cadastrarProdutosALista(listaId,finalIds.get(i));

                        Intent intent = new Intent(ListaProdutosActivity.this, DetalheListaProdutosActivity.class);
                        intent.putExtra("listaId", listaId);
                        startActivity(intent);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
