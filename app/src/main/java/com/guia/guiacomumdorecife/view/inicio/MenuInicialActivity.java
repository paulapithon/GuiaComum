package com.guia.guiacomumdorecife.view.inicio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guia.guiacomumdorecife.R;
import com.guia.guiacomumdorecife.view.camera.CameraActivity;
import com.guia.guiacomumdorecife.view.guia.MapaActivity;
import com.guia.guiacomumdorecife.view.participe.ParticiparActivity;

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
        if(checkInternet()) {
            startActivity(new Intent(this, MapaActivity.class));
        }
    }

    @OnClick(R.id.btn_participe)
    public void onParticipe () {
        //Inicia a tela de participação
        if(checkInternet()) {
            startActivity(new Intent(this, ParticiparActivity.class));
        }
    }

    @OnClick(R.id.btn_camera)
    public void onCamera () {
        startActivity(new Intent(this, CameraActivity.class));
    }

    private boolean checkInternet () {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (manager.getActiveNetwork() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.conteudo_internet);
                builder.setTitle(R.string.titulo_internet);
                builder.setPositiveButton(R.string.ok_internet, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
                builder.create().show();
                return false;
            }
        }
        return true;
    }

    @OnClick(R.id.btn_creditos)
    public void onCreditos () {
        startActivity(new Intent(this, CreditosActivity.class));
    }

}
