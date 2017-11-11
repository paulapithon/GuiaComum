package com.gov.guia.guiacomumdorecife.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Paula Pithon on 10/11/2017.
 */

public class Livro implements Serializable{

    private String nome;
    private ArrayList<String> imagens;
    private String resumo;
    private String local;

    public Livro () {
        super();
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<String> getImagens() {
        return imagens;
    }

    public String getResumo() {
        return resumo;
    }

    public String getLocal() {
        return local;
    }
}
