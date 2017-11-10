package com.gov.guia.guiacomumdorecife.view.guia;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gov.guia.guiacomumdorecife.GuiaComumApplication;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.BtnMapa;
import com.gov.guia.guiacomumdorecife.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LivrosActivity extends AppCompatActivity {

    @BindView(R.id.lista_livros)
    ListView mListaLivros;
    @BindView(R.id.nome_livros)
    TextView mNomeLivros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livros);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {

        BtnMapa mapa = GuiaComumApplication.getsBotoesMapa().get(getIntent().getStringExtra(Constants.DATABASE_MAPA_INDEX));
        mListaLivros.setAdapter(new LivrosAdapter(this, R.layout.item_livro, mapa.getLivros()));

        mNomeLivros.setText(mapa.getNome().toUpperCase());

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
