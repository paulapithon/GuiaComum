package com.gov.guia.guiacomumdorecife.view.participe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Sugestao;
import com.gov.guia.guiacomumdorecife.util.Constants;

import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParticiparActivity extends AppCompatActivity {

    final private int SELECIONAR_FOTO = 123;
    final private int SELECIONAR_AUDIO = 321;
    final private int SELECIONAR_LOCAL = 444;

    @BindView(R.id.edit_nome)
    EditText mEditNome;
    @BindView(R.id.edit_email)
    EditText mEditEmail;
    @BindView(R.id.edit_descricao)
    EditText mEditDescricao;
    @BindView(R.id.termos_aceitar)
    CheckBox mCheckBox;

    String imageUrl;
    String audioUrl;
    String localCoordenadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_local_atual)
    public void onLocalAtual() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    SELECIONAR_LOCAL);

        } else {
            getLocalizacaoAtual();
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocalizacaoAtual() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                new LocationListener() {

                    @Override
                    public void onLocationChanged(final Location location) {
                        localCoordenadas = getResources().getString(
                                R.string.participe_coordenadas,
                                location.getLatitude(),
                                location.getLongitude()
                        );
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) { }
                    @Override
                    public void onProviderEnabled(String s) { }
                    @Override
                    public void onProviderDisabled(String s) { }
                });
    }

    @OnClick(R.id.btn_tirar_foto)
    public void onTirarFoto () {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECIONAR_FOTO);
    }

    @OnClick(R.id.btn_gravar_audio)
    public void onGravarAudio () {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECIONAR_AUDIO);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK && (requestCode == SELECIONAR_FOTO || requestCode == SELECIONAR_AUDIO)) {
            try {
                final boolean isPhoto = requestCode == SELECIONAR_FOTO;
                InputStream stream = getContentResolver().openInputStream(intent.getData());

                StorageReference referencia = FirebaseStorage.getInstance().getReference().child(
                        (isPhoto ? Constants.DATABASE_IMAGENS_SUGESTAO : Constants.DATABASE_AUDIOS_SUGESTAO) +
                        System.currentTimeMillis() +
                        (isPhoto ? Constants.TIPO_FOTO : Constants.TIPO_AUDIO)
                );

                UploadTask uploadTask = referencia.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (isPhoto) { imageUrl = taskSnapshot.getDownloadUrl().toString(); }
                        else { audioUrl = taskSnapshot.getDownloadUrl().toString(); }
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == SELECIONAR_LOCAL) {
            getLocalizacaoAtual();
        }
    }

    @OnClick(R.id.btn_compartilhar)
    public void onAceitar () {
        saveDatabase();
        finish();
    }

    private void saveDatabase () {
        if (mCheckBox.isChecked()) {
            FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_SUGESTAO).push().setValue(new Sugestao(
                    mEditNome.getText().toString(),
                    mEditEmail.getText().toString(),
                    mEditDescricao.getText().toString(),
                    imageUrl,
                    audioUrl,
                    localCoordenadas
            ));
        }
    }

    @OnClick(R.id.termos_ver)
    public void onTermos () {
        startActivity(new Intent(this, TermosActivity.class));
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
