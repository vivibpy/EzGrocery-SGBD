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
import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.ListadeProdutos;
import com.example.ezgrocery.model.Produto;
import com.example.ezgrocery.model.Receita;
import com.example.ezgrocery.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class DetalheListaProdutosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private TextView nometxt;
    private Button compartilharbtt;
    private Button addProdutobtt;
    private Button comprarListabtt;
    private Button dividir;
    private ListView listaProdutos;
    //nav
    private TextView email;
    private DrawerLayout navDrawerlayout;
    private ActionBarDrawerToggle navToggle;
    private FirebaseAuth autenticacao;
    //nav
    ArrayAdapter<String> itensAdaptador;
    ArrayList<String> itens = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_lista_produtos);

        //nav
        navDrawerlayout = findViewById(R.id.drawerlistadet);
        navToggle = new ActionBarDrawerToggle(this,navDrawerlayout,R.string.open, R.string.close);
        navDrawerlayout.addDrawerListener(navToggle);
        navToggle.syncState();

        NavigationView nvDrawer = findViewById(R.id.nvlistadet);
        nvDrawer.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        View header = nvDrawer.getHeaderView(0);

        email = header.findViewById(R.id.emailtxtnav);
        email.setText(user.getEmail().isEmpty() ? "Login por Telefone" : user.getEmail());
        //nav

        nometxt = findViewById(R.id.titulotxt);
        compartilharbtt = findViewById(R.id.compartilharbtt2);
        addProdutobtt = findViewById(R.id.addProdutobtt);
        listaProdutos = findViewById(R.id.produtoslist);
        comprarListabtt = findViewById(R.id.btnComprarLista);
        dividir = findViewById(R.id.dividirbtt);

        Intent intent = getIntent();
        final int listaId = intent.getExtras().getInt("listaId");

        dividir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalheListaProdutosActivity.this, DividirListaActivity.class);
                intent.putExtra("listaId", listaId);
                startActivity(intent);
            }
        });

        //Banco dados
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);
        final ListadeProdutos listadeProdutos = bd.obterListadeProdutos(listaId);
        final Usuario usu = UsuarioLogado.obtemUsuarioLogado();

        if(listadeProdutos != null) {
            if(listadeProdutos.comprada) {
                nometxt.setText("[Comprada] " +listadeProdutos.nome);
            }else
            {
                nometxt.setText(listadeProdutos.nome);
            }
        }

        carregarLista();

        listaProdutos.setLongClickable(true);
        final ArrayList<Integer> produtosIds = ids;

        listaProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                bd.deletarProdutoDeUmaLista(listaId,produtosIds.get(position));
                carregarLista();
                Toast.makeText(DetalheListaProdutosActivity.this, "Produto removido da lista", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        compartilharbtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textToShare = "Essa é minha lista de produtos " + listadeProdutos.nome+ " \n\n";

                for(Produto p : listadeProdutos.item)
                {
                    textToShare += p.nome + " - Preço R$" + p.preco + "\n";
            }
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
                startActivity(Intent.createChooser(shareIntent, "Compartilhar Lista..."));
            }
        });

        addProdutobtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetalheListaProdutosActivity.this, ListaProdutosActivity.class);
                intent.putExtra("listaId", listaId);
                startActivity(intent);
            }
        });

        comprarListabtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarteiraVirtual carteira = bd.obterCarteira(usu.getId());

                int totalDaLista = 0;

                for(Produto p : listadeProdutos.item)
                {
                    totalDaLista += p.preco;
                }

                int dinheiroRestante = carteira.valor - totalDaLista;

                if(dinheiroRestante >= 0)
                {
                    bd.atualizarCarteira(carteira.id,dinheiroRestante);
                    bd.comprarLista(listaId);

                    Toast.makeText(DetalheListaProdutosActivity.this, "Lista comprada com sucesso! Seu saldo restante: " + dinheiroRestante, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(DetalheListaProdutosActivity.this, ListaListaProdutosActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(DetalheListaProdutosActivity.this, "Você não possui saldo para comprar essa lista. Seu saldo: " + carteira.valor, Toast.LENGTH_LONG).show();

                }
            }
        });

        if(listadeProdutos.comprada)
        {
            comprarListabtt.setEnabled(false);
            dividir.setEnabled(false);
            addProdutobtt.setEnabled(false);
        }
    }

    private void carregarLista()
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

        listaProdutos.setAdapter(itensAdaptador);
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
                intent = new Intent(DetalheListaProdutosActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
                break;
            case R.id.receitasmenu:
                intent = new Intent(DetalheListaProdutosActivity.this, ListaReceitasActivity.class);
                startActivity(intent);
                break;
            case R.id.codigosmenu:
                intent = new Intent(DetalheListaProdutosActivity.this, ResgatarCodigoActivity.class);
                startActivity(intent);
                break;

            case R.id.perfilmenu:
                intent = new Intent(DetalheListaProdutosActivity.this, PerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.adicionarsaldo:
                intent = new Intent(DetalheListaProdutosActivity.this, AdicionarSaldoActivity.class);
                startActivity(intent);
                break;
            case R.id.lojasfisicas:
                intent = new Intent(DetalheListaProdutosActivity.this, MapaActivity.class);
                startActivity(intent);
                break;

            case R.id.logoutmenu:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                intent = new Intent(DetalheListaProdutosActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(DetalheListaProdutosActivity.this,"_",Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }
}

