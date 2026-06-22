package br.com.fiap.agnellofrontend.data.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "produtos")
public class ProdutoEntity {
    @PrimaryKey
    public int id;
    public String nome;
    public String tipo;
    public String regiao;
    public String uva;
    public Double teorAlcoolico;
    public Integer intensidade;
    public Integer encorpado;
    public Integer acucar;
    public Integer acidez;
    public double valor;
    public int quantidadeEstoque;
    public Double avaliacaoCritica;

    public ProdutoEntity() {
    }

    @Ignore
    public ProdutoEntity(
            int id,
            String nome,
            String tipo,
            String regiao,
            String uva,
            Double teorAlcoolico,
            Integer intensidade,
            Integer encorpado,
            Integer acucar,
            Integer acidez,
            double valor,
            int quantidadeEstoque,
            Double avaliacaoCritica
    ) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.regiao = regiao;
        this.uva = uva;
        this.teorAlcoolico = teorAlcoolico;
        this.intensidade = intensidade;
        this.encorpado = encorpado;
        this.acucar = acucar;
        this.acidez = acidez;
        this.valor = valor;
        this.quantidadeEstoque = quantidadeEstoque;
        this.avaliacaoCritica = avaliacaoCritica;
    }
}
