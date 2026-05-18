package br.com.katia.api_bancaria.testutil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TestDatabaseCleaner {

    @Inject
    EntityManager em;

    @Transactional
    public void limpar() {
        em.createNativeQuery("DELETE FROM transacao").executeUpdate();
        em.createNativeQuery("DELETE FROM conta").executeUpdate();
        em.createNativeQuery("DELETE FROM cliente").executeUpdate();
    }
}
