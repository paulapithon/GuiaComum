package com.gov.guia.guiacomumdorecife;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AberturaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        //Mostra tela inicial por 2 segundos
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                iniciarMenu();
            }
        }, 2000);
    }

    private void iniciarMenu () {
        //Inicia tela de menu principal
        startActivity(new Intent(AberturaActivity.this, MenuPrincipalActivity.class));
    }
}
