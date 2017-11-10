package com.gov.guia.guiacomumdorecife.view.guia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GaleriaActivity extends AppCompatActivity {

    private static final int INITIAL_INDEX = 1;

    @BindView(R.id.cabecalho_titulo)
    TextView mCabecalho;
    @BindView(R.id.preview)
    PhotoView mFoto;
    @BindView(R.id.btn_voltar_imagem)
    ImageButton mVoltarBtn;
    @BindView(R.id.btn_passar_imagem)
    ImageButton mPassarImagem;

    private ArrayList<String> imagens;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {
        imagens = getIntent().getStringArrayListExtra(Constants.DATABASE_IMAGENS_INDEX);

        //Setar imagem como a inicial do array
        currentIndex = INITIAL_INDEX;
        setImagemAtual(currentIndex);
    }

    private void setImagemAtual(int index) {

        Glide.with(this).load(imagens.get(index - 1)).into(mFoto);
        mCabecalho.setText(getResources().getString(R.string.imagens_cabecalho, index, imagens.size()));

        //Verificar botão da direita e da esquerda
        if (index >= imagens.size()) { mPassarImagem.setVisibility(View.GONE); }
        else { mPassarImagem.setVisibility(View.VISIBLE); }

        if (index <= INITIAL_INDEX) { mVoltarBtn.setVisibility(View.GONE); }
        else { mVoltarBtn.setVisibility(View.VISIBLE); }

    }

    @OnClick(R.id.btn_passar_imagem)
    public void onPassarImagem () {
        if (currentIndex + 1 <= imagens.size()) {
            setImagemAtual(++currentIndex);
        }
    }

    @OnClick(R.id.btn_voltar_imagem)
    public void onVoltarImagem () {
        if (currentIndex - 1 >= INITIAL_INDEX) {
            setImagemAtual(--currentIndex);
        }
    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}