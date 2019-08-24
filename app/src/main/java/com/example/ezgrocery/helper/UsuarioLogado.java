package com.example.ezgrocery.helper;

import com.example.ezgrocery.config.ConfiguracaoFirebase;
import com.example.ezgrocery.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioLogado {

    public static Usuario obtemUsuarioLogado()
    {
        Usuario usu = null;

        FirebaseAuth autenticacao =  ConfiguracaoFirebase.getFirebaseAutenticacao();

        FirebaseUser user = autenticacao.getCurrentUser();
        if(user != null)
        {
            usu = new Usuario();
            usu.setEmail(user.getEmail());
            usu.setId(user.getUid());
        }

        return usu;
    }
}
