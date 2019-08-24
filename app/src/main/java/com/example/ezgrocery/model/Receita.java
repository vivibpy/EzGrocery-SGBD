package com.example.ezgrocery.model;

public class Receita {
    public Receita(){}

    public Receita(String nome, String desc){
        Nome = nome;
        Descricao = desc;
    }

    public Receita(int id, String nome, String desc){
        Nome = nome;
        Descricao = desc;
        Id = id;
    }

    public int Id;
    public String Nome;
    public String Descricao;
    public ListadeProdutos listadeProdutos;
}
