package br.edu.ifsp.carlao2005.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class JPAUtil {

    // A fabrica (Factory) e criada UMA vez e reaproveitada (e cara de criar).
    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("loja");

    // Cada operacao pega um EntityManager novo a partir da fabrica.
    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    // Fecha a fabrica ao encerrar o programa.
    public static void close() {
        FACTORY.close();
    }
}
