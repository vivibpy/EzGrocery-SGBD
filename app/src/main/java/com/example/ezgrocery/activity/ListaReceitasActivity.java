package com.example.ezgrocery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.Receita;
import com.example.ezgrocery.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListaReceitasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ListView listaReceitas;
    //nav
    private TextView email;
    private DrawerLayout navDrawerlayout;
    private ActionBarDrawerToggle navToggle;
    private FirebaseAuth autenticacao;
    //nav

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_receitas);

        //nav
        navDrawerlayout = findViewById(R.id.drawerlistarec);
        navToggle = new ActionBarDrawerToggle(this,navDrawerlayout,R.string.open, R.string.close);
        navDrawerlayout.addDrawerListener(navToggle);
        navToggle.syncState();

        NavigationView nvDrawer = findViewById(R.id.nvlistarec);
        nvDrawer.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        View header = nvDrawer.getHeaderView(0);

        email = header.findViewById(R.id.emailtxtnav);
        email.setText(user.getEmail().isEmpty() ? "Login por Telefone" : user.getEmail());
        //nav

        ArrayAdapter<String> itensAdaptador;
        ArrayList<String> itens = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        try {
            //Banco dados
            OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

            //lista
            listaReceitas = findViewById(R.id.menulist);

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

                listaReceitas.setAdapter( itensAdaptador );

                final ArrayList<Integer> finalIds = ids;

                listaReceitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(ListaReceitasActivity.this, DetalheReceitaActivity.class);
                        intent.putExtra("receitaId", finalIds.get(i));
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
                intent = new Intent(ListaReceitasActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
                break;
            case R.id.receitasmenu:
                //intent = new Intent(ListaReceitasActivity.this, ListaReceitasActivity.class);
                //startActivity(intent);
                break;
            case R.id.codigosmenu:
                intent = new Intent(ListaReceitasActivity.this, ResgatarCodigoActivity.class);
                startActivity(intent);
                break;

            case R.id.perfilmenu:
                intent = new Intent(ListaReceitasActivity.this, PerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.adicionarsaldo:
                intent = new Intent(ListaReceitasActivity.this, AdicionarSaldoActivity.class);
                startActivity(intent);
                break;
            case R.id.lojasfisicas:
                intent = new Intent(ListaReceitasActivity.this, MapaActivity.class);
                startActivity(intent);
                break;

            case R.id.logoutmenu:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                intent = new Intent(ListaReceitasActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(ListaReceitasActivity.this,"_",Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }
}


