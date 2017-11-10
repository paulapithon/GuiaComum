package com.gov.guia.guiacomumdorecife.view.guia;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Livro;
import com.gov.guia.guiacomumdorecife.util.Constants;
import com.gov.guia.guiacomumdorecife.util.PlayAudioManager;

import java.security.spec.ECField;

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

    private Livro livro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudo);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {

        livro = (Livro) getIntent().getSerializableExtra(Constants.DATABASE_LIVRO_INDEX);

        mTitulo.setText(livro.getNome().toUpperCase());

        //Reajustar layout baseado no resumo e imagem
        if (livro.getResumo() != null) { mResumo.setText(livro.getResumo()); }
        else { mResumo.setVisibility(View.GONE); }

        if (livro.getImagens() != null) { Glide.with(this).load(livro.getImagens().get(0)).into(mImagem); }
        else { mImagem.setVisibility(View.GONE); }

        //Setar botões de interação
        if (livro.getAudio() == null) {  mAudioBtn.setVisibility(View.GONE); }

    }

    private void setupPlayer (boolean play) {
        try {
            if (play) { PlayAudioManager.playAudio(this, livro.getAudio()); }
            else { PlayAudioManager.killMediaPlayer(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_audio)
    public void onAudio () {
        setupPlayer(true);
    }

    @OnClick(R.id.imagem_conteudo)
    public void onConteudo () {
        Intent intent = new Intent(this, GaleriaActivity.class);
        intent.putExtra(Constants.DATABASE_IMAGENS_INDEX, livro.getImagens());
        startActivity(intent);
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        setupPlayer(false);
        onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
