package br.edu.ifsp.carlao2005.testes;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import br.edu.ifsp.carlao2005.dao.AlunoDao;
import br.edu.ifsp.carlao2005.modelo.Aluno;
import br.edu.ifsp.carlao2005.util.JPAUtil;
import jakarta.persistence.EntityManager;

/*
 * Classe principal: mostra o menu e chama as operacoes do CRUD.
 * Cada operacao que muda o banco (cadastrar, alterar, excluir) abre uma transacao,
 * faz o trabalho e da commit; as consultas (buscar, listar) nao precisam de transacao.
 */
public class CadastroDeAlunos {

    // Um unico Scanner para o programa todo.
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Digite a opcao desejada: ");
            System.out.println();

            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> excluirAluno();
                case 3 -> alterarAluno();
                case 4 -> buscarAluno();
                case 5 -> listarAlunos();
                case 6 -> System.out.println("Encerrando...");
                default -> System.out.println("Opcao invalida!");
            }

            System.out.println();
        } while (opcao != 6);

        // Fecha a fabrica do JPA ao sair.
        JPAUtil.close();
    }

    private static void exibirMenu() {
        System.out.println("** CADASTRO DE ALUNOS **");
        System.out.println();
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Excluir aluno");
        System.out.println("3 - Alterar aluno");
        System.out.println("4 - Buscar aluno pelo nome");
        System.out.println("5 - Listar alunos (com status aprovacao)");
        System.out.println("6 - FIM");
        System.out.println();
    }

    // Opcao 1 -----------------------------------------------------------------
    private static void cadastrarAluno() {
        System.out.println("CADASTRAR ALUNO:");
        String nome = lerTexto("Digite o nome: ");
        String ra = lerTexto("Digite o RA: ");
        String email = lerTexto("Digite o email: ");
        double nota1 = lerDouble("Digite a nota 1: ");
        double nota2 = lerDouble("Digite a nota 2: ");
        double nota3 = lerDouble("Digite a nota 3: ");

        Aluno aluno = new Aluno(nome, email, ra, nota1, nota2, nota3);

        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao dao = new AlunoDao(em);

        em.getTransaction().begin();
        dao.cadastrar(aluno);
        em.getTransaction().commit();
        em.close();

        System.out.println();
        System.out.println("Aluno cadastrado com sucesso!");
    }

    // Opcao 2 -----------------------------------------------------------------
    private static void excluirAluno() {
        System.out.println("EXCLUIR ALUNO:");
        String nome = lerTexto("Digite o nome: ");

        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao dao = new AlunoDao(em);

        Aluno aluno = dao.buscarPorNome(nome);
        System.out.println();

        if (aluno == null) {
            System.out.println("Aluno nao encontrado!");
            em.close();
            return;
        }

        em.getTransaction().begin();
        dao.excluir(aluno);
        em.getTransaction().commit();
        em.close();

        System.out.println("Aluno excluido com sucesso!");
    }

    // Opcao 3 -----------------------------------------------------------------
    private static void alterarAluno() {
        System.out.println("ALTERAR ALUNO:");
        String nome = lerTexto("Digite o nome: ");

        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao dao = new AlunoDao(em);

        Aluno aluno = dao.buscarPorNome(nome);
        System.out.println();

        if (aluno == null) {
            System.out.println("Aluno nao encontrado!");
            em.close();
            return;
        }

        System.out.println("Dados do aluno:");
        imprimirDadosBasicos(aluno);
        System.out.println();

        System.out.println("NOVOS DADOS:");
        aluno.setNome(lerTexto("Digite o nome: "));
        aluno.setRa(lerTexto("Digite o RA: "));
        aluno.setEmail(lerTexto("Digite o email: "));
        aluno.setNota1(lerDouble("Digite a nota 1: "));
        aluno.setNota2(lerDouble("Digite a nota 2: "));
        aluno.setNota3(lerDouble("Digite a nota 3: "));

        em.getTransaction().begin();
        dao.alterar(aluno);
        em.getTransaction().commit();
        em.close();

        System.out.println();
        System.out.println("Aluno alterado com sucesso!");
    }

    // Opcao 4 -----------------------------------------------------------------
    private static void buscarAluno() {
        System.out.println("CONSULTAR ALUNO:");
        String nome = lerTexto("Digite o nome: ");

        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao dao = new AlunoDao(em);

        Aluno aluno = dao.buscarPorNome(nome);
        em.close();
        System.out.println();

        if (aluno == null) {
            System.out.println("Aluno nao encontrado!");
            return;
        }

        System.out.println("Dados do aluno:");
        imprimirDadosBasicos(aluno);
    }

    // Opcao 5 -----------------------------------------------------------------
    private static void listarAlunos() {
        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao dao = new AlunoDao(em);

        List<Aluno> alunos = dao.listarTodos();
        em.close();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado!");
            return;
        }

        System.out.println("Exibindo todos os alunos:");
        for (Aluno aluno : alunos) {
            System.out.println();
            imprimirDadosBasicos(aluno);
            System.out.println("Media: " + formatar(aluno.getMedia()));
            System.out.println("Situacao: " + aluno.getSituacao());
        }
    }

    // Auxiliares --------------------------------------------------------------
    private static void imprimirDadosBasicos(Aluno aluno) {
        System.out.println("Nome: " + aluno.getNome());
        System.out.println("Email: " + aluno.getEmail());
        System.out.println("RA: " + aluno.getRa());
        System.out.println("Notas: " + formatar(aluno.getNota1()) + " - "
                + formatar(aluno.getNota2()) + " - " + formatar(aluno.getNota3()));
    }

    private static String formatar(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static String lerTexto(String rotulo) {
        System.out.print(rotulo);
        return entrada.nextLine();
    }

    private static int lerInteiro(String rotulo) {
        System.out.print(rotulo);
        while (!entrada.hasNextInt()) {
            entrada.nextLine();
            System.out.print(rotulo);
        }
        int valor = entrada.nextInt();
        entrada.nextLine(); // consome a quebra de linha que sobra
        return valor;
    }

    private static double lerDouble(String rotulo) {
        System.out.print(rotulo);
        while (!entrada.hasNextDouble()) {
            entrada.nextLine();
            System.out.print(rotulo);
        }
        double valor = entrada.nextDouble();
        entrada.nextLine();
        return valor;
    }
}
