package br.com.katia.api_bancaria.integracao;

import br.com.katia.api_bancaria.entity.Conta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@ApplicationScoped
public class ContaTestSeed {

    public Conta origem;
    public Conta destino;

    @Transactional
    public void inserirDados() {

        origem = new Conta();
        origem.setTitular("João");
        origem.setSaldo(new BigDecimal("1000.00"));

        destino = new Conta();
        destino.setTitular("Maria");
        destino.setSaldo(new BigDecimal("500.00"));

        Conta.persist(origem);
        Conta.persist(destino);
    }
}