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
import com.example.ezgrocery.model.CupomDesconto;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Usuario;

import java.util.ArrayList;

public class admin_lista_cupoms extends AppCompatActivity {

    private ListView listaCupons;
    private Button criarCupombtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lista_cupoms);

        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        if(!user.getEmail().equals("admin@gmail.com"))
        {
            Intent intent = new Intent(admin_lista_cupoms.this, MainActivity.class);
            startActivity(intent);
        }
        listaCupons = findViewById(R.id.admlistacupons);
        criarCupombtn = findViewById(R.id.admincadastroCupom);

        //Banco dados
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        criarCupombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(admin_lista_cupoms.this, admin_cadastrar_cupom.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        try {

            ArrayList<CupomDesconto> cups  = bd.recuperarCupoms();

            if(cups.isEmpty() == false){

                for(CupomDesconto c : cups){
                    itens.add(c.codigo + " - " + c.descricao);
                    ids.add(c.id);
                }

                itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text2,
                        itens);

                listaCupons.setAdapter( itensAdaptador );

                final ArrayList<Integer> finalIds = ids;

                listaCupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(admin_lista_cupoms.this, admin_editar_cupom.class);
                        intent.putExtra("cupomId", finalIds.get(i));
                        startActivity(intent);
                    }
                });

                listaCupons.setLongClickable(true);

                listaCupons.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        bd.deletarCupom(finalIds.get(position));
                        Intent intent = new Intent(admin_lista_cupoms.this, admin_lista_cupoms.class);
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
