package com.example.ezgrocery.model;

import com.example.ezgrocery.enumerator.Categoria;

public class Produto {
    public Produto(){

    }

    public Produto(int id, String nome, String codRef, Categoria cat, float desconto, int qtdEstoque, String codBarras){
        this.id = id;
        this.nome = nome;
        this.cod_ref = codRef;
        this.categoria = cat;
        this.desconto = desconto;
        this.qtd_estoque = qtdEstoque;
        this.cod_barras = codBarras;
    }

    public int id;
    public String nome;
    public String cod_ref;
    public Categoria categoria;
    public float desconto;
    public int qtd_estoque;
    public int preco;
    public String cod_barras;
}
