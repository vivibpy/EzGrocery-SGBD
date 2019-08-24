package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class PagPrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //private Toolbar navtoolbar;
    //nav
    private DrawerLayout navDrawerlayout;
    private ActionBarDrawerToggle navToggle;
    private TextView email;
    private FirebaseAuth autenticacao;
    //nav



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //nav
        setContentView(R.layout.activity_pag_principal);
        navDrawerlayout = findViewById(R.id.drawer);
        navToggle = new ActionBarDrawerToggle(this,navDrawerlayout,R.string.open, R.string.close);
        navDrawerlayout.addDrawerListener(navToggle);
        navToggle.syncState();

        NavigationView nvDrawer = findViewById(R.id.nv);
        nvDrawer.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //nav
        OperacaoBancoDeDados op = new OperacaoBancoDeDados(this);
        //navtb
        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        View header = nvDrawer.getHeaderView(0);

        email = header.findViewById(R.id.emailtxtnav);
        email.setText(user.getEmail().isEmpty() ? "Login por Telefone" : user.getEmail());
        //navtb
        CarteiraVirtual carta = op.obterCarteira(user.getId());

        if(carta == null)
        {
            op.cadastrarCarteiraVazia(user.getId());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(navToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setUpDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;

            }
        });
    }


    public void selectItemDrawer(MenuItem item){
        Intent intent;
        switch (item.getItemId()) {
            case R.id.listasmenu:
                // User chose the "Settings" item, show the app settings UI...
                intent = new Intent(PagPrincipalActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
                break;
            case R.id.receitasmenu:
                intent = new Intent(PagPrincipalActivity.this, ListaReceitasActivity.class);
                startActivity(intent);
                break;
            case R.id.codigosmenu:
                intent = new Intent(PagPrincipalActivity.this, ResgatarCodigoActivity.class);
                startActivity(intent);
                break;

            case R.id.perfilmenu:
                intent = new Intent(PagPrincipalActivity.this, PerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.adicionarsaldo:
                intent = new Intent(PagPrincipalActivity.this, AdicionarSaldoActivity.class);
                startActivity(intent);
                break;
            case R.id.lojasfisicas:
                intent = new Intent(PagPrincipalActivity.this, MapaActivity.class);
                startActivity(intent);
                break;

            case R.id.logoutmenu:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                intent = new Intent(PagPrincipalActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(PagPrincipalActivity.this,"_",Toast.LENGTH_LONG).show();
                break;

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.listasmenu:
                // User chose the "Settings" item, show the app settings UI...
                intent = new Intent(PagPrincipalActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
                break;
            case R.id.receitasmenu:
                intent = new Intent(PagPrincipalActivity.this, ListaReceitasActivity.class);
                startActivity(intent);
                break;
            case R.id.codigosmenu:
                intent = new Intent(PagPrincipalActivity.this, ResgatarCodigoActivity.class);
                startActivity(intent);
                break;

            case R.id.perfilmenu:
                intent = new Intent(PagPrincipalActivity.this, PerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.adicionarsaldo:
                intent = new Intent(PagPrincipalActivity.this, AdicionarSaldoActivity.class);
                startActivity(intent);
                break;
            case R.id.lojasfisicas:
                intent = new Intent(PagPrincipalActivity.this, MapaActivity.class);
                startActivity(intent);
                break;

            case R.id.logoutmenu:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                intent = new Intent(PagPrincipalActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(PagPrincipalActivity.this,"_",Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }
}
