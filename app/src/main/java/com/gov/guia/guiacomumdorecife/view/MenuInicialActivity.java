package com.gov.guia.guiacomumdorecife.view;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.view.guia.MapaActivity;
import com.gov.guia.guiacomumdorecife.view.participe.ParticiparActivity;

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
        //Inicia a tela de participação
        startActivity(new Intent(this, ParticiparActivity.class));
    }

    @OnClick(R.id.btn_camera)
    public void onCamera () {
        //TODO adicionar filtro
        startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

}
