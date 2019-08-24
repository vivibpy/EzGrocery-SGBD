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
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Usuario;

import java.util.ArrayList;

public class DeleteListaProdutoActivity extends AppCompatActivity {

    private ListView listadelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_lista_produto);

        OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);
        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        listadelete = findViewById(R.id.listalistadeleteprodutos);

        try {


            Usuario user = UsuarioLogado.obtemUsuarioLogado();
            ArrayList<ListadeProdutos> listas  = bd.recuperarListasProdutos(user.getId());

            if(listas.isEmpty() == false){

                for(ListadeProdutos l : listas){
                    itens.add(l.nome);
                    ids.add(l.id);
                }

                itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text2,
                        itens);

                listadelete.setAdapter( itensAdaptador );

                final ArrayList<Integer> finalIds = ids;

                listadelete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(DeleteListaProdutoActivity.this, ListaListaProdutosActivity.class);
                        intent.putExtra("listaId", finalIds.get(i));
                        intent.putExtra("origem", 2);
                        startActivity(intent);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    }

