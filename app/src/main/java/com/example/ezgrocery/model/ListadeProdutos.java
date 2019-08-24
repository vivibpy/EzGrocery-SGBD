package com.example.ezgrocery.model;

import java.util.ArrayList;

public class ListadeProdutos {

    public ListadeProdutos(){

    }

    public ListadeProdutos(String nome, ArrayList<Produto> produtos){
        this.nome = nome;

        if(produtos == null || produtos.isEmpty()) {
            this.item = new ArrayList<>();
        }else{
            this.item = produtos;
        }
    }
    public ListadeProdutos(int id, String nome, ArrayList<Produto> produtos){

        this.id = id;
        this.nome = nome;

        if(produtos == null || produtos.isEmpty()) {
            this.item = new ArrayList<>();
        }else{
            this.item = produtos;
        }
    }

    public int id;
    public String nome;
    public boolean comprada;
    public ArrayList<Produto> item;
}
