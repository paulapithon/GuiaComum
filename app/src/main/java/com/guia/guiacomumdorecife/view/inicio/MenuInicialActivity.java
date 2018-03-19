package com.guia.guiacomumdorecife.view.inicio;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guia.guiacomumdorecife.R;
import com.guia.guiacomumdorecife.view.camera.CameraActivity;
import com.guia.guiacomumdorecife.view.guia.MapaActivity;
import com.guia.guiacomumdorecife.view.participe.ParticiparActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuInicialActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 101;

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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            startActivity(new Intent(this, CameraActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                boolean granted = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager
                            .PERMISSION_GRANTED) {
                        granted = false;
                    }
                }
                if (granted) {
                    startActivity(new Intent(this, CameraActivity.class));
                }
            }
        }
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
