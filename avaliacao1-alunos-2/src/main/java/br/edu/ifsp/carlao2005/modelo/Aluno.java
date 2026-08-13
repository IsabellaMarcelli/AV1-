package br.edu.ifsp.carlao2005.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
 * Entidade Aluno: cada objeto desta classe vira uma linha na tabela "alunos".
 * As anotacoes JPA (@Entity, @Id, etc.) e que fazem o mapeamento objeto -> tabela.
 */
@Entity
@Table(name = "alunos")
public class Aluno {

    // Chave primaria, gerada automaticamente pelo banco (auto-incremento).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String ra;

    private Double nota1;
    private Double nota2;
    private Double nota3;

    // O JPA exige um construtor vazio.
    public Aluno() {
    }

    public Aluno(String nome, String email, String ra, Double nota1, Double nota2, Double nota3) {
        this.nome = nome;
        this.email = email;
        this.ra = ra;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
    }

    // Media aritmetica das tres notas.
    public double getMedia() {
        return (nota1 + nota2 + nota3) / 3.0;
    }

    // Regra da avaliacao:
    //   abaixo de 4        -> Reprovado
    //   de 4 (inclusive) a 6 -> Recuperacao
    //   6 ou acima         -> Aprovado
    public String getSituacao() {
        double media = getMedia();
        if (media < 4) {
            return "Reprovado";
        } else if (media < 6) {
            return "Recuperacao";
        } else {
            return "Aprovado";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public Double getNota3() {
        return nota3;
    }

    public void setNota3(Double nota3) {
        this.nota3 = nota3;
    }
}
