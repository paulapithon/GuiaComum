package com.gov.guia.guiacomumdorecife.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Paula Pithon on 10/11/2017.
 */

public class Livro implements Serializable{

    String nome;
    ArrayList<String> imagens;

    public Livro () {
        super();
    }
}
