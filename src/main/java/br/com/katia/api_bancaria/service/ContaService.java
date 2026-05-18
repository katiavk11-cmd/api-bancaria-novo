package br.com.katia.api_bancaria.service;

import br.com.katia.api_bancaria.entity.Conta;
import br.com.katia.api_bancaria.repository.ContaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@ApplicationScoped
public class ContaService {

    @Inject
    ContaRepository contaRepository;

    // =========================
    // DEPÓSITO
    // =========================
    @Transactional
    public void depositar(Long id, BigDecimal valor) {

        Conta conta = contaRepository.findById(id);

        validarConta(conta);
        validarValor(valor);

        conta.setSaldo(conta.getSaldo().add(valor));

        contaRepository.persist(conta);
    }

    // =========================
    // SAQUE
    // =========================
    @Transactional
    public void sacar(Long id, BigDecimal valor) {

        Conta conta = contaRepository.findById(id);

        validarConta(conta);
        validarValor(valor);
        validarSaldo(conta, valor);

        conta.setSaldo(conta.getSaldo().subtract(valor));

        contaRepository.persist(conta);
    }

    // =========================
    // TRANSFERÊNCIA
    // =========================
    @Transactional
    public void transferir(Long origemId, Long destinoId, BigDecimal valor) {

        if (origemId.equals(destinoId)) {
            throw new RuntimeException(
                    "Contas de origem e destino devem ser diferentes."
            );
        }

        Conta origem = contaRepository.findById(origemId);
        Conta destino = contaRepository.findById(destinoId);

        validarConta(origem);
        validarConta(destino);
        validarValor(valor);
        validarSaldo(origem, valor);

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        contaRepository.persist(origem);
        contaRepository.persist(destino);
    }

    // =========================
    // CONSULTA DE SALDO
    // =========================
    public BigDecimal calcularSaldo(Long id) {

        Conta conta = contaRepository.findById(id);

        validarConta(conta);

        return conta.getSaldo();
    }

    // =========================
    // VALIDAÇÕES
    // =========================
    private void validarConta(Conta conta) {
        if (conta == null) {
            throw new RuntimeException("Conta não encontrada.");
        }
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor inválido.");
        }
    }

    private void validarSaldo(Conta conta, BigDecimal valor) {
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente.");
        }
    }
}