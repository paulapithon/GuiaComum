package com.guia.guiacomumdorecife.util;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.guia.guiacomumdorecife.R;
import com.guia.guiacomumdorecife.model.Sugestao;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Paula Pithon on 17/11/2017.
 */

public class ParticiparService extends Service {

    private String localCoordenadas;
    private String imageUrl;
    private String audioUrl;

    private String nome;
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
        nome = args.getString(Constants.SERVICO_NOME);
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
                nome,
                email,
                descricao,
                imageUrl,
                audioUrl,
                localCoordenadas
        ));

        sendEmail();
        stopSelf();
    }

    private void sendEmail () {


        new Thread(new Runnable() {
            @Override
            public void run() {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    GMailSender sender = new GMailSender(getResources().getString(R.string.envio_email), getResources().getString(R.string.envio_senha));
                    sender.sendMail(getResources().getString(R.string.envio_titulo),
                            getMessage(),
                            getResources().getString(R.string.envio_email),
                            email);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
    }

    private String getMessage () {
        return "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t<tbody>\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td align=\"center\" bgcolor=\"#ffffff\" style=\"background-color:#ffffff;\" valign=\"top\">\n" +
                "\t\t\t\t\t\t<br />\n" +
                "\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"padding-left:13px; padding-right:13px; background-color:rgb(246, 174, 177);\" valign=\"top\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"background-color: rgb(246, 174, 177);\">&nbsp;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-family: noto serif; font-size: 36px; text-align: center; vertical-align: middle; background-color: rgb(246, 174, 177);\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<strong><span style=\"color:#161781;\">" + nome + ", bem-vindo(a) ao Recife que resiste com afeto</span></strong></td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding-top: 15px; text-align: center; vertical-align: middle; background-color: rgb(246, 174, 177);\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<img src=\"https://firebasestorage.googleapis.com/v0/b/guiacomum.appspot.com/o/sugestao%2Femail%2Fhtml_titulo.png?alt=media&token=74c45022-b71f-4721-9a87-35f09a77a948\" alt=\"\" height=\"326\" style=\"width: 580px; height: 70px;\" /></td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"font-family: Noto serif; color: rgb(0, 0, 0); font-size: 15px; text-align: center; vertical-align: middle; background-color: rgb(246, 174, 177);\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"font-size:18px;\"><span style=\"color: rgb(22, 23, 129);\"><br>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tFicamos gratos com sua participa&ccedil;&atilde;o em nossa vers&atilde;o beta. Iremos analisar o conte&uacute;do enviado e verificar a possibilidade de publica&ccedil;&atilde;o. Em caso de aprova&ccedil;&atilde;o, informaremos o momento em que seu material ser&aacute; publicado.<br />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<br />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tEm breve voc&ecirc; receber&aacute; uma pr&oacute;xima newsletter para acompanhar nossas atualiza&ccedil;&otilde;es.<br />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<br />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tAtenciosamente,<br />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tSua equipe Guia Comum do Recife</span></span></td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td style=\"text-align: center; vertical-align: middle; background-color: rgb(246, 174, 177);\">&nbsp;\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"padding-left:13px; padding-right:13px; background-color:rgb(246, 174, 177);\" valign=\"top\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<img alt=\"\" height=\"318\" src=\"https://firebasestorage.googleapis.com/v0/b/guiacomum.appspot.com/o/sugestao%2Femail%2Fhtml_rodape.jpg?alt=media&token=c5571fa7-72cd-4e09-8023-3b0aa7b80944\" style=\"width: 590px; height: 78px;\" width=\"2390\" /></td>\n" +
                "\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t<br />\n" +
                "\t\t\t\t\t\t&nbsp;</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t</tbody>\n" +
                "\t\t</table>";
    }
}
