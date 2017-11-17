package com.gov.guia.guiacomumdorecife.util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Sugestao;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Paula Pithon on 17/11/2017.
 */

public class ParticiparService extends Service {

    private String localCoordenadas;
    private String imageUrl;
    private String audioUrl;

    private String name;
    private String descricao;
    private String email;
    private Uri imageUri;
    private Uri audioUri;
    private boolean hasLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle args = intent.getBundleExtra(Constants.SERVICO_BUNDLE);
        name = args.getString(Constants.SERVICO_NOME);
        email = args.getString(Constants.SERVICO_EMAIL);
        descricao = args.getString(Constants.SERVICO_DESCRICAO);
        if (args.getString(Constants.SERVICO_AUDIO) != null) {
            audioUri = Uri.parse(args.getString(Constants.SERVICO_AUDIO));
        }
        if (args.getString(Constants.SERVICO_IMAGEM) != null) {
            imageUri = Uri.parse(args.getString(Constants.SERVICO_IMAGEM));
        }
        hasLocation = args.getBoolean(Constants.SERVICO_LOCALIZACAO);

        salvarArquivos();

        return super.onStartCommand(intent, flags, startId);
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
                        if (localCoordenadas == null) {
                            localCoordenadas = getResources().getString(
                                    R.string.participe_coordenadas,
                                    location.getLatitude(),
                                    location.getLongitude()
                            );
                            salvarArquivos();
                        }
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) { }
                    @Override
                    public void onProviderEnabled(String s) { }
                    @Override
                    public void onProviderDisabled(String s) { }
                });

    }

    private void loadFiles (final boolean isPhoto) {
        try {
            InputStream stream = getContentResolver().openInputStream(isPhoto ? imageUri : audioUri);

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
                    salvarArquivos();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void salvarArquivos() {
        if (imageUri != null && imageUrl == null) {
            loadFiles(true);
        } else if (audioUri != null && audioUrl == null) {
            loadFiles(false);
        } else if (hasLocation && localCoordenadas == null) {
            getLocalizacaoAtual();
        } else {
            saveDatabase();
        }
    }

    private void saveDatabase () {

        FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_SUGESTAO).push().setValue(new Sugestao(
                name,
                email,
                descricao,
                imageUrl,
                audioUrl,
                localCoordenadas
        ));

        Toast.makeText(this, getResources().getString(R.string.envio_participe), Toast.LENGTH_SHORT).show();

        stopSelf();
    }

}
