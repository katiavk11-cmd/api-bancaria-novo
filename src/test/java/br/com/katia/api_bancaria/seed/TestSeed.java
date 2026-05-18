package br.com.katia.api_bancaria.seed;

import br.com.katia.api_bancaria.entity.Cliente;
import br.com.katia.api_bancaria.entity.Conta;
import br.com.katia.api_bancaria.repository.ClienteRepository;
import br.com.katia.api_bancaria.repository.ContaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@ApplicationScoped
public class TestSeed {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    ContaRepository contaRepository;

    @Transactional
    public void inserirDados() {

        clienteRepository.deleteAll();
        contaRepository.deleteAll();

        Cliente c1 = new Cliente();
        c1.setNome("Ana");
        c1.setCpf("111");
        c1.setEmail("ana@test.com");
        clienteRepository.persist(c1);

        Cliente c2 = new Cliente();
        c2.setNome("Bruno");
        c2.setCpf("222");
        c2.setEmail("bruno@test.com");
        clienteRepository.persist(c2);

        Conta conta1 = new Conta();
        conta1.setTitular("Ana");
        conta1.setSaldo(new BigDecimal("1000.00"));
        conta1.setCliente(c1);
        contaRepository.persist(conta1);

        Conta conta2 = new Conta();
        conta2.setTitular("Bruno");
        conta2.setSaldo(new BigDecimal("1000.00"));
        conta2.setCliente(c2);
        contaRepository.persist(conta2);
    }
}