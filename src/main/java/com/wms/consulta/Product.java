package com.wms.consulta;

import javafx.beans.property.*;

public class Product {

    private final StringProperty ean;
    private final StringProperty codigo;
    private final StringProperty nome;
    private final IntegerProperty quantidade;
    private final StringProperty local;

    public Product(String ean, String codigo, String nome, int quantidade, String local) {
        this.ean = new SimpleStringProperty(ean);
        this.codigo = new SimpleStringProperty(codigo);
        this.nome = new SimpleStringProperty(nome);
        this.quantidade = new SimpleIntegerProperty(quantidade);
        this.local = new SimpleStringProperty(local);
    }

    public StringProperty eanProperty() { return ean; }
    public StringProperty codigoProperty() { return codigo; }
    public StringProperty nomeProperty() { return nome; }
    public IntegerProperty quantidadeProperty() { return quantidade; }
    public StringProperty localProperty() { return local; }
}
