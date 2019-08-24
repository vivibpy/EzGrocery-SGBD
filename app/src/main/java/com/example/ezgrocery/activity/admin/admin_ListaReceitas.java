package com.example.ezgrocery.activity.admin;

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
import com.example.ezgrocery.activity.CadastroListaActivity;
import com.example.ezgrocery.activity.DetalheListaProdutosActivity;
import com.example.ezgrocery.activity.DetalheReceitaActivity;
import com.example.ezgrocery.activity.ListaReceitasActivity;
import com.example.ezgrocery.activity.MainActivity;
import com.example.ezgrocery.activity.PagPrincipalActivity;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.Receita;
import com.example.ezgrocery.model.Usuario;

import java.util.ArrayList;

public class admin_ListaReceitas extends AppCompatActivity {

    private ListView listasReceitas;
    private Button criarReceitabtn;
    private Button verProdutosBtn;
    private Button verCuponsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista_receitas);

        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        if(!user.getEmail().equals("admin@gmail.com"))
        {
            Intent intent = new Intent(admin_ListaReceitas.this, MainActivity.class);
            startActivity(intent);
        }

        listasReceitas = findViewById(R.id.admlistareceitas);
        criarReceitabtn = findViewById(R.id.admincadastroReceitabtn);
        verProdutosBtn = findViewById(R.id.adminProdutosbtn);
        verCuponsBtn = findViewById(R.id.adminCuponsbtn);

        //Banco dados
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        verProdutosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(admin_ListaReceitas.this, admin_lista_produto.class);
                startActivity(intent);
            }
        });

        verCuponsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(admin_ListaReceitas.this, admin_lista_cupoms.class);
                startActivity(intent);
            }
        });

        criarReceitabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bd.cadastrarReceita("Nova receita", "descrição");
                Receita rec = bd.obterUltimaReceita();

                Intent intent = new Intent(admin_ListaReceitas.this, admin_detalhe_receita.class);
                intent.putExtra("receitaId", rec.Id);
                startActivity(intent);
            }
        });

        criarReceitabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bd.cadastrarReceita("Nova receita", "descrição");
                Receita rec = bd.obterUltimaReceita();

                Intent intent = new Intent(admin_ListaReceitas.this, admin_detalhe_receita.class);
                intent.putExtra("receitaId", rec.Id);
                startActivity(intent);
            }
        });
        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        try {

            ArrayList<Receita> receitas  = bd.recuperarReceitas();

            if(receitas.isEmpty() == false){

                for(Receita r : receitas){
                    itens.add(r.Nome);
                    ids.add(r.Id);
                }

                itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text2,
                        itens);

                listasReceitas.setAdapter( itensAdaptador );

                final ArrayList<Integer> finalIds = ids;

                listasReceitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(admin_ListaReceitas.this, admin_detalhe_receita.class);
                        intent.putExtra("receitaId", finalIds.get(i));
                        startActivity(intent);
                    }
                });

                listasReceitas.setLongClickable(true);

                listasReceitas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        bd.deletarReceita(finalIds.get(position));
                        Intent intent = new Intent(admin_ListaReceitas.this, admin_ListaReceitas.class);
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
