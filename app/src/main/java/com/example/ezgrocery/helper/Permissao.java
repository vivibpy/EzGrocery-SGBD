package com.example.ezgrocery.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes)
    {
        if(Build.VERSION.SDK_INT >= 23)
        {
            List<String> listaPermissoes = new ArrayList<>() ;

            for(String permissao : permissoes)
            {
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;

                if(validaPermissao == false)
                {
                    listaPermissoes.add(permissao);
                }
            }

            if(listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity,novasPermissoes,requestCode);
        }

        return true;
    }

}
