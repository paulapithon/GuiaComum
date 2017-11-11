package com.gov.guia.guiacomumdorecife.view.guia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gov.guia.guiacomumdorecife.GuiaComumApplication;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.BtnMapa;
import com.gov.guia.guiacomumdorecife.model.Livro;
import com.gov.guia.guiacomumdorecife.util.Constants;
import com.gov.guia.guiacomumdorecife.util.PlayAudioManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConteudoActivity extends AppCompatActivity {

    @BindView(R.id.titulo_conteudo)
    TextView mTitulo;
    @BindView(R.id.imagem_conteudo)
    ImageButton mImagem;
    @BindView(R.id.resumo_conteudo)
    TextView mResumo;
    @BindView(R.id.btn_audio)
    ImageButton mAudioBtn;
    @BindView(R.id.btn_local)
    ImageButton mLocalBtn;

    private Livro livro;
    private String audio;
    private boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudo);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {

        livro = (Livro) getIntent().getSerializableExtra(Constants.DATABASE_LIVRO_INDEX);
        audio = GuiaComumApplication.getsBotoesMapa().get(GuiaComumApplication.getsCurrentMap()).getAudio();

        mTitulo.setText(livro.getNome().toUpperCase());

        //Reajustar layout baseado no resumo e imagem
        if (livro.getResumo() != null) { mResumo.setText(livro.getResumo()); }
        else { mResumo.setVisibility(View.GONE); }

        if (livro.getImagens() != null) { Glide.with(this).load(livro.getImagens().get(0)).into(mImagem); }
        else { mImagem.setVisibility(View.GONE); }

        //Setar botões de interação
        if (audio == null) {  mAudioBtn.setVisibility(View.GONE); }
        if (livro.getLocal() == null) { mLocalBtn.setVisibility(View.GONE); }

    }

    private void setupPlayer (boolean play) {
        try {
            if (play) { PlayAudioManager.playAudio(this, audio); }
            else { PlayAudioManager.killMediaPlayer(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_audio)
    public void onAudio () {
        playing = !playing;
        setupPlayer(playing);
    }

    @OnClick(R.id.btn_local)
    public void onLocal () {
        String[] local = livro.getLocal().split(getResources().getString(R.string.maps_split));
        String uri = getResources().getString(R.string.maps_pesquisa, local[0], local[1], local[2]);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @OnClick(R.id.btn_compartilhar)
    public void onCompartilhar () {

        final Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.conteudo_enviar, livro.getNome()));

        if (livro.getImagens() != null) {
            Glide.with(this).asBitmap().load(livro.getImagens().get(0)).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    sendIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                    sendIntent.setType("image/jpeg");
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.texto_enviar)));
                }
            });
        } else {
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.texto_enviar)));
        }

    }

    public Uri getLocalBitmapUri(Bitmap bitmao) {
        Uri uri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "guiaComum_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmao.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    @OnClick(R.id.imagem_conteudo)
    public void onConteudo () {
        Intent intent = new Intent(this, GaleriaActivity.class);
        intent.putExtra(Constants.DATABASE_IMAGENS_INDEX, livro.getImagens());
        startActivity(intent);
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setupPlayer(false);
        super.onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
