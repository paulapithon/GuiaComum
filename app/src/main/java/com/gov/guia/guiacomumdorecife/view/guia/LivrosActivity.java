package com.gov.guia.guiacomumdorecife.view.guia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.gov.guia.guiacomumdorecife.GuiaComumApplication;
import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.model.Mapa;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        Mapa mapa = GuiaComumApplication.getsBotoesMapa().get(GuiaComumApplication.getsCurrentMap());
        mListaLivros.setAdapter(new LivrosAdapter(this, R.layout.item_livro, mapa.getLivros()));

        mNomeLivros.setText(mapa.getNome().toUpperCase());

    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
