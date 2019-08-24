package com.example.ezgrocery.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.ezgrocery.R;
import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.example.ezgrocery.helper.OperacaoBancoDeDados;
import com.example.ezgrocery.helper.UsuarioLogado;
import com.example.ezgrocery.model.CarteiraVirtual;
import com.example.ezgrocery.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private TextView nome;
    private TextView sobrenome;
    private TextView emailtxt;
    private TextView endereco;
    private TextView saldo;
    private Button editarperfil;
    private Button deleteperfil;
    //nav
    private TextView email;
    private DrawerLayout navDrawerlayout;
    private ActionBarDrawerToggle navToggle;
    private FirebaseAuth autenticacao;
    //nav
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //nav
        navDrawerlayout = findViewById(R.id.drawerperfil);
        navToggle = new ActionBarDrawerToggle(this,navDrawerlayout,R.string.open, R.string.close);
        navDrawerlayout.addDrawerListener(navToggle);
        navToggle.syncState();

        NavigationView nvDrawer = findViewById(R.id.nvperfil);
        nvDrawer.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Usuario user = UsuarioLogado.obtemUsuarioLogado();

        View header = nvDrawer.getHeaderView(0);

        email = header.findViewById(R.id.emailtxtnav);
        email.setText(user.getEmail().isEmpty() ? "Login por Telefone" : user.getEmail());
        //nav
        final OperacaoBancoDeDados bd = new OperacaoBancoDeDados(this);

        CarteiraVirtual carta = bd.obterCarteira(user.getId());
        deleteperfil = findViewById(R.id.deleteperfilbtt);
        nome = findViewById(R.id.nometxt);
        sobrenome = findViewById(R.id.sobrenometxt);
        emailtxt = findViewById(R.id.emailtxt);
        emailtxt.setText(user.getEmail().isEmpty() ? "Login por Telefone" : user.getEmail());
        endereco = findViewById(R.id.enderecotxt);
        editarperfil = findViewById(R.id.editbtt);
        saldo = findViewById(R.id.saldoperfiltxt);
        saldo.append(""+ carta.valor + ",00 reais");
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
                intent = new Intent(PerfilActivity.this, ListaListaProdutosActivity.class);
                startActivity(intent);
                break;
            case R.id.receitasmenu:
                intent = new Intent(PerfilActivity.this, ListaReceitasActivity.class);
                startActivity(intent);
                break;
            case R.id.codigosmenu:
                intent = new Intent(PerfilActivity.this, ResgatarCodigoActivity.class);
                startActivity(intent);
                break;

            case R.id.perfilmenu:

                break;
            case R.id.adicionarsaldo:
                intent = new Intent(PerfilActivity.this, AdicionarSaldoActivity.class);
                startActivity(intent);
                break;
            case R.id.lojasfisicas:
                intent = new Intent(PerfilActivity.this, MapaActivity.class);
                startActivity(intent);
                break;

            case R.id.logoutmenu:
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signOut();
                intent = new Intent(PerfilActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(PerfilActivity.this,"_",Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }
}


