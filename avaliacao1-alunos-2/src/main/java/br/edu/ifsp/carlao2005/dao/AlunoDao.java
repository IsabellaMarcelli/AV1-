package br.edu.ifsp.carlao2005.dao;

import java.util.List;

import br.edu.ifsp.carlao2005.modelo.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;


public class AlunoDao {

    private final EntityManager em;

    public AlunoDao(EntityManager em) {
        this.em = em;
    }

    // INSERT: coloca um aluno novo no banco.
    public void cadastrar(Aluno aluno) {
        this.em.persist(aluno);
    }

    // UPDATE: atualiza um aluno ja existente (que tem id).
    public void alterar(Aluno aluno) {
        this.em.merge(aluno);
    }

    // DELETE: remove o aluno do banco.
    public void excluir(Aluno aluno) {
        this.em.remove(aluno);
    }

    // SELECT por nome. Retorna null se nao encontrar nenhum.
    public Aluno buscarPorNome(String nome) {
        try {
            return this.em
                    .createQuery("SELECT a FROM Aluno a WHERE a.nome = :nome", Aluno.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    // SELECT 
    public List<Aluno> listarTodos() {
        return this.em
                .createQuery("SELECT a FROM Aluno a", Aluno.class)
                .getResultList();
    }
}
