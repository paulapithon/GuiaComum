package com.gov.guia.guiacomumdorecife.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gov.guia.guiacomumdorecife.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuInicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_guia)
    public void onGuia () {
        //Inicia tela de mapa
        startActivity(new Intent(this, MapaActivity.class));
    }

    @OnClick(R.id.btn_participe)
    public void onParticipe () {

    }

    @OnClick(R.id.btn_camera)
    public void onCamera () {

    }
}
