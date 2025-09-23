package com.projectbarber.projectbarber.dto;


public class ServicoDTO {
    private Integer id;
    private String nome;
    private double preco;
    private Integer tempoDuracao;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }
    public Integer getTempoDUracao() {
        return tempoDuracao;
    }
    public void setTempoDUracao(Integer tempoDuracao) {
        this.tempoDuracao = tempoDuracao;
    }

}