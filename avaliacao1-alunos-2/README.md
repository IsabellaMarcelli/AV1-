# Cadastro de Alunos — projeto de referência (JPA)

Projeto de **estudo** feito no mesmo padrão das aulas de PRW3 (Entidade + DAO +
JPAUtil + Main), usando **Hibernate + H2 em memória** (o obrigatório dos trabalhos).
Serve para você entender como as peças se encaixam e comparar com a sua versão.

## Como rodar

Precisa de **JDK 17+** e **Maven** instalados.

```bash
mvn compile        # baixa Hibernate/H2 e compila
mvn exec:java -Dexec.mainClass=br.edu.ifsp.carlao2005.testes.CadastroDeAlunos
```

Ou, mais simples, abra a pasta no **IntelliJ** (ele reconhece o `pom.xml`),
espere o Maven baixar as dependências e rode a classe `CadastroDeAlunos`
(botão ▶ ao lado do `main`).

> Obs.: o banco é **em memória** (`jdbc:h2:mem:loja`). Os dados existem só
> enquanto o programa está rodando — a cada execução ele começa vazio. Isso é o
> comportamento esperado do H2 em memória usado nas aulas.

## Estrutura e o papel de cada arquivo

```
src/main/
├── java/br/edu/ifsp/carlao2005/
│   ├── modelo/Aluno.java        -> a "entidade": vira a tabela "alunos"
│   ├── dao/AlunoDao.java        -> acesso ao banco (cadastrar, alterar, excluir, buscar, listar)
│   ├── util/JPAUtil.java        -> cria o EntityManager (a "porta" do JPA)
│   └── testes/CadastroDeAlunos.java -> o menu (main)
└── resources/META-INF/persistence.xml -> configuração da conexão (Hibernate + H2)
```

### 1. `Aluno` (a entidade)
É uma classe Java comum com anotações JPA. `@Entity` diz "isso vira tabela";
`@Id` + `@GeneratedValue` marcam a chave primária auto-incremento. Os dois
métodos importantes da regra da avaliação ficam aqui:

- `getMedia()` = (nota1 + nota2 + nota3) / 3
- `getSituacao()`:
  - média **abaixo de 4** → `Reprovado`
  - média **de 4 até (mas não incluindo) 6** → `Recuperacao`
  - média **6 ou mais** → `Aprovado`

### 2. `AlunoDao` (o acesso ao banco)
Concentra todo o JPA num lugar só. Recebe um `EntityManager` no construtor (igual
o `ProdutoDao` das aulas) e oferece: `cadastrar` (persist), `alterar` (merge),
`excluir` (remove), `buscarPorNome` (JPQL) e `listarTodos` (JPQL).

### 3. `JPAUtil`
Cria a `EntityManagerFactory` uma vez (o `"loja"` tem que ser igual ao nome da
`persistence-unit` no `persistence.xml`) e entrega um `EntityManager` novo por
operação.

### 4. `CadastroDeAlunos` (o menu)
O `main` com as 6 opções. Operações que **mudam** o banco (1, 2, 3) abrem uma
transação (`begin` … `commit`); consultas (4, 5) não precisam.

## Pontos de atenção que costumam derrubar o projeto

- `persistence.xml` **tem que** ficar em `src/main/resources/META-INF/` — pasta
  exata `META-INF` (com hífen).
- O nome em `createEntityManagerFactory("loja")` tem que ser **idêntico** ao
  `name` da `persistence-unit`.
- Toda operação de escrita precisa de `begin()`/`commit()`; sem isso nada é salvo.
- Cuidado com o `Scanner`: depois de `nextInt()`/`nextDouble()` sobra uma quebra
  de linha — por isso o código chama `nextLine()` para "limpar".

## O que foi conferido

- A regra de média/situação foi testada contra os exemplos do slide
  (Lupita 4.33 → Recuperação, Asdrubal 8.00 → Aprovado, Zoroastro → Reprovado)
  e nas fronteiras (4 → Recuperação, 6 → Aprovado). Tudo bateu.
- Todas as classes compilam sem erro.
