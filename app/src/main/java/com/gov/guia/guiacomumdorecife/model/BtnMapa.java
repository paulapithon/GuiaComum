package com.gov.guia.guiacomumdorecife.model;

import java.util.ArrayList;

/**
 * Created by Paula Pithon on 10/11/2017.
 */

public class BtnMapa {

    private String nome;
    private String icone;
    private double x;
    private double y;
    private ArrayList<Livro> livros;

    public BtnMapa () {
        super();
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getIcone() {
        return icone;
    }

    public String getNome() {
        return nome;
    }
}
