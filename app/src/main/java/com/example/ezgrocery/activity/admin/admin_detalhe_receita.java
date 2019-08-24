package com.example.ezgrocery.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ezgrocery.R;
import com.example.ezgrocery.activity.CadastroListaActivity;
import com.example.ezgrocery.activity.DetalheListaProdutosActivity;
import com.example.ezgrocery.activity.MainActivity;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Receita;

import java.util.ArrayList;

public class admin_detalhe_receita extends AppCompatActivity {

    private ListView listaProduto;
    private Button editarReceitabtn;
    private Button criarListabtn;
    private TextView txtNome;
    private TextView txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detalhe_receita);

        listaProduto = findViewById(R.id.listaProdutosReceita);
        editarReceitabtn = findViewById(R.id.editarReceitaBtt);
        criarListabtn = findViewById(R.id.addListaAdmbtt);
        txtNome = findViewById(R.id.nomeReceitaAdm);
        txtDesc = findViewById(R.id.descricaoReceitaAdm);

        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        Intent intent = getIntent();
        final int receitaId = intent.getExtras().getInt("receitaId");

        Receita rec = bd.obterReceita(receitaId);

        txtNome.setText(rec.Nome);
        txtDesc.setText(rec.Descricao);


        editarReceitabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_detalhe_receita.this, admin_editar_receita.class);
                intent.putExtra("receitaId", receitaId);
                startActivity(intent);
            }
        });

        if(rec.listadeProdutos == null)
        {
            criarListabtn.setVisibility(View.VISIBLE);

            criarListabtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(admin_detalhe_receita.this, CadastroListaActivity.class);
                    intent.putExtra("receitaId", receitaId);
                    startActivity(intent);
                }
            });
        }
        else{
            ArrayAdapter<String> itensAdaptador;
            ArrayList<String> itens = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();

            try {

                itens.add(rec.listadeProdutos.nome);
                ids.add(rec.listadeProdutos.id);

                    itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_2,
                            android.R.id.text2,
                            itens);

                    listaProduto.setAdapter( itensAdaptador );

                    final ArrayList<Integer> finalIds = ids;

                    listaProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent = new Intent(admin_detalhe_receita.this, DetalheListaProdutosActivity.class);
                            intent.putExtra("listaId", finalIds.get(i));
                            startActivity(intent);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
