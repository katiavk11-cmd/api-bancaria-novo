package br.com.katia.api_bancaria.service;

import br.com.katia.api_bancaria.entity.Conta;
import br.com.katia.api_bancaria.repository.ContaRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class ContaServiceTest {

    @Inject
    ContaService contaService;

    @InjectMock
    ContaRepository contaRepository;

    private Conta contaOrigem;
    private Conta contaDestino;

    @BeforeEach
    void setup() {
        contaOrigem = new Conta();
        contaOrigem.setTitular("Ana");
        contaOrigem.setSaldo(new BigDecimal("1000.00"));
        contaOrigem.setAtiva(true);

        contaDestino = new Conta();
        contaDestino.setTitular("Bruno");
        contaDestino.setSaldo(new BigDecimal("500.00"));
        contaDestino.setAtiva(true);

        when(contaRepository.findById(1L)).thenReturn(contaOrigem);
        when(contaRepository.findById(2L)).thenReturn(contaDestino);
        when(contaRepository.findById(99L)).thenReturn(null);
    }

    @Test
    void deveDepositarComSucesso() {
        // ARRANGE
        BigDecimal valor = new BigDecimal("200.00");

        // ACT
        contaService.depositar(1L, valor);

        // ASSERT
        assertEquals(0, contaOrigem.getSaldo().compareTo(new BigDecimal("1200.00")));
        verify(contaRepository).findById(1L);
        verify(contaRepository).persist((Conta) any(Conta.class));
    }

    @Test
    void deveSacarComSucesso() {
        // ARRANGE
        BigDecimal valor = new BigDecimal("300.00");

        // ACT
        contaService.sacar(1L, valor);

        // ASSERT
        assertEquals(0, contaOrigem.getSaldo().compareTo(new BigDecimal("700.00")));
        verify(contaRepository).findById(1L);
        verify(contaRepository).persist((Conta) any(Conta.class));
    }

    @Test
    void deveTransferirComSucesso() {
        // ARRANGE
        BigDecimal valor = new BigDecimal("200.00");

        // ACT
        contaService.transferir(1L, 2L, valor);

        // ASSERT
        assertEquals(0, contaOrigem.getSaldo().compareTo(new BigDecimal("800.00")));
        assertEquals(0, contaDestino.getSaldo().compareTo(new BigDecimal("700.00")));

        verify(contaRepository).findById(1L);
        verify(contaRepository).findById(2L);
        verify(contaRepository, times(2)).persist((Conta) any(Conta.class));
    }

    @Test
    void deveCalcularSaldoComSucesso() {
        // ARRANGE

        // ACT
        BigDecimal saldo = contaService.calcularSaldo(1L);

        // ASSERT
        assertEquals(0, saldo.compareTo(new BigDecimal("1000.00")));
        verify(contaRepository).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaNaoExisteNoDeposito() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.depositar(99L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository).findById(99L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaNaoExisteNoSaque() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.sacar(99L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository).findById(99L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaNaoExisteNaTransferencia() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.transferir(99L, 2L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository).findById(99L);
        verify(contaRepository).findById(2L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaNaoExisteAoCalcularSaldo() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.calcularSaldo(99L)
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository).findById(99L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDoDepositoForZero() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.depositar(1L, BigDecimal.ZERO)
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDoDepositoForNegativo() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.depositar(1L, new BigDecimal("-10.00"))
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDoSaqueForZero() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.sacar(1L, BigDecimal.ZERO)
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDaTransferenciaForZero() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.transferir(1L, 2L, BigDecimal.ZERO)
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository).findById(1L);
        verify(contaRepository).findById(2L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroSaldoInsuficienteNoSaque() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.sacar(1L, new BigDecimal("5000.00"))
        );

        // ASSERT
        assertEquals("Saldo insuficiente.", ex.getMessage());
        verify(contaRepository).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroSaldoInsuficienteNaTransferencia() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.transferir(1L, 2L, new BigDecimal("5000.00"))
        );

        // ASSERT
        assertEquals("Saldo insuficiente.", ex.getMessage());
        verify(contaRepository).findById(1L);
        verify(contaRepository).findById(2L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroAoTransferirParaMesmaConta() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.transferir(1L, 1L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Contas de origem e destino devem ser diferentes.", ex.getMessage());
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }
}