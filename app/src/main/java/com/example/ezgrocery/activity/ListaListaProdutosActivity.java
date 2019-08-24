package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListaListaProdutosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listasProdutos;
    private Button cadastroListaprodutobtt;
    private Button deleteListaprodutobtt;
    //nav
    private TextView email;
    private DrawerLayout navDrawerlayout;
    private ActionBarDrawerToggle navToggle;
    private FirebaseAuth autenticacao;
    //nav
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_lista_produtos);

        //nav
        navDrawerlayout = findViewById(R.id.drawerlistalistaprod);
        navToggle = new ActionBarDrawerToggle(this,navDrawerlayout,R.string.open, R.string.close);
        navDrawerlayout.addDrawerListener(navToggle);
        navToggle.syncState();

        NavigationView nvDrawer = findViewById(R.id.nvlistalistaprod);
        nvDrawer.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Usuario user1 = UsuarioLogado.obtemUsuarioLogado();

        View header = nvDrawer.getHeaderView(0);

        email = header.findViewById(R.id.emailtxtnav);
        email.setText(user1.getEmail().isEmpty() ? "Login por Telefone" : user1.getEmail());
        //nav


        //delete
        Intent intent = getIntent();
        if (intent.hasExtra("listaId")){
            int listaId = intent.getExtras().getInt("listaId");
            OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);
            bd.deletarlistaProdutos(listaId);
        }

        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        //lista
        listasProdutos = findViewById(R.id.listalistaprodutos);
        cadastroListaprodutobtt = findViewById(R.id.cadastrolistaprodutobtt);
        deleteListaprodutobtt = findViewById(R.id.deletelistaprodutobtt);

        cadastroListaprodutobtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaListaProdutosActivity.this, CadastroListaActivity.class);
                startActivity(intent);
            }
        });

        deleteListaprodutobtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaListaProdutosActivity.this, DeleteListaProdutoActivity.class);
                startActivity(intent);
            }
        });

        try {
            //Banco dados
            OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

            Usuario user = UsuarioLogado.obtemUsuarioLogado();

            ArrayList<ListadeProdutos> listas  = bd.recuperarListasProdutos(user.getId());

            if(listas.isEmpty() == false){

                for(ListadeProdutos l : listas){
                    if(l.comprada) {
                        itens.add("[Comprada] " + l.nome);
                    }else
                    {
                        itens.add(l.nome);
                    }

                    ids.add(l.id);
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

                        Intent intent = new Intent(ListaListaProdutosActivity.this, DetalheListaProdutosActivity.class);
                        intent.putExtra("listaId", finalIds.get(i));
                        startActivity(intent);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(navToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.listasmenu:
                // User chose the "Settings" item, show the app settings UI...
                intent = new Intent(ListaListaProdutosActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
                break;
            case R.id.receitasmenu:
                intent = new Intent(ListaListaProdutosActivity.this, ListaReceitasActivity.class);
                startActivity(intent);
                break;
            case R.id.codigosmenu:
                intent = new Intent(ListaListaProdutosActivity.this, ResgatarCodigoActivity.class);
                startActivity(intent);
                break;

            case R.id.perfilmenu:
                intent = new Intent(ListaListaProdutosActivity.this, PerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.adicionarsaldo:
                intent = new Intent(ListaListaProdutosActivity.this, AdicionarSaldoActivity.class);
                startActivity(intent);
                break;
            case R.id.lojasfisicas:
                intent = new Intent(ListaListaProdutosActivity.this, MapaActivity.class);
                startActivity(intent);
                break;

            case R.id.logoutmenu:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                intent = new Intent(ListaListaProdutosActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(ListaListaProdutosActivity.this,"_",Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }
}


