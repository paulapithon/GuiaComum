package com.gov.guia.guiacomumdorecife.view.participe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gov.guia.guiacomumdorecife.R;
import com.gov.guia.guiacomumdorecife.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TermosActivity extends AppCompatActivity {

    @BindView(R.id.descricao_termos)
    TextView mDescricao;
    @BindView(R.id.titulo_termos)
    TextView mTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);
        ButterKnife.bind(this);

        setUI();
    }

    private void setUI () {

        String texto = getIntent().getStringExtra(Constants.DATABASE_LIVRO_TEXTO);
        String titulo = getIntent().getStringExtra(Constants.DATABASE_LIVRO_TITULO);

        if(texto != null && titulo != null) {
            //Adicionar parágrafos aos textos
            String[] descricao = texto.split("   ");
            StringBuilder descricaoFinal = new StringBuilder();
            for (int i = 0; i < descricao.length; i++) {
                descricaoFinal.append(descricao[i]);
                descricaoFinal.append("\n\n");
            }
            mDescricao.setText(descricaoFinal);
            mTitulo.setText(titulo.toUpperCase());
        } else {
            mDescricao.setText(getResources().getString(R.string.termos_conteudo));
            mTitulo.setText(getResources().getString(R.string.termos_titulo));
        }

    }

    @OnClick(R.id.btn_voltar)
    public void onBack () {
        onBackPressed();
    }

}
