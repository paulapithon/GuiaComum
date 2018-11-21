package com.guia.guiacomumdorecife.view.guia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.guia.guiacomumdorecife.GuiaComumApplication;
import com.guia.guiacomumdorecife.R;
import com.guia.guiacomumdorecife.model.Livro;
import com.guia.guiacomumdorecife.model.Mapa;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LivrosActivity extends AppCompatActivity {

    @BindView(R.id.lista_livros)
    ListView mListaLivros;
    @BindView(R.id.nome_livros)
    TextView mNomeLivros;

    private ArrayList<Livro> livros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livros);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {

        Mapa mapa = GuiaComumApplication.getsBotoesMapa().get(GuiaComumApplication.getsCurrentMap());

        livros = new ArrayList<>();
        Random random = new Random();
        for (Livro livro : mapa.getLivros()) {
            //Menor chance de ter um livro no inicio
            if (random.nextBoolean()) { livros.add(new Livro()); }
            livros.add(livro);
            //Chances iguais de ter um livro depois
            if (random.nextBoolean()) { livros.add(new Livro()); }
        }

        mListaLivros.setAdapter(new LivrosAdapter(this, R.layout.item_livro, livros));

        mNomeLivros.setText(mapa.getNome().toUpperCase());

    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
