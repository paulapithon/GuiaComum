package com.gov.guia.guiacomumdorecife.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
        //Cria diálogo para nova feature em breve
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.conteudo_dialogo);
        builder.setTitle(R.string.titulo_dialogo);
        builder.setPositiveButton(R.string.ok_dialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
