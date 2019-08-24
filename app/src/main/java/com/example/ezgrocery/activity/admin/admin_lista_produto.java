package com.example.ezgrocery.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ezgrocery.R;
import com.example.ezgrocery.activity.MainActivity;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Usuario;

import java.util.ArrayList;

public class admin_lista_produto extends AppCompatActivity {

    private ListView listasProdutos;
    private Button criarProdutobtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista_produto);

        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        if(!user.getEmail().equals("admin@gmail.com"))
        {
            Intent intent = new Intent(admin_lista_produto.this, MainActivity.class);
            startActivity(intent);
        }
        listasProdutos = findViewById(R.id.admlistaprodutos);
        criarProdutobtn = findViewById(R.id.admincadastroProduto);

        //Banco dados
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        criarProdutobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 Intent intent = new Intent(admin_lista_produto.this, admin_cadastrar_produto.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        try {

            ArrayList<Produto> prods  = bd.recuperarTodosProdutos();

            if(prods.isEmpty() == false){

                for(Produto pr : prods){
                    itens.add(pr.nome);
                    ids.add(pr.id);
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

                        Intent intent = new Intent(admin_lista_produto.this, admin_editar_produto.class);
                        intent.putExtra("produtoId", finalIds.get(i));
                        startActivity(intent);
                    }
                });

                listasProdutos.setLongClickable(true);

                listasProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        bd.deletarProduto(finalIds.get(position));
                        Intent intent = new Intent(admin_lista_produto.this, admin_lista_produto.class);
                        startActivity(intent);
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
