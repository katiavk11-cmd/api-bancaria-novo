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
import static org.mockito.Mockito.*;

@QuarkusTest
class TransacaoServiceTest {

    @Inject
    TransacaoService transacaoService;

    @InjectMock
    ContaRepository contaRepository;

    private Conta origem;
    private Conta destino;

    @BeforeEach
    void setup() {
        origem = new Conta();
        origem.setTitular("Ana");
        origem.setSaldo(new BigDecimal("1000.00"));
        origem.setAtiva(true);

        destino = new Conta();
        destino.setTitular("Bruno");
        destino.setSaldo(new BigDecimal("500.00"));
        destino.setAtiva(true);

        when(contaRepository.findById(1L)).thenReturn(origem);
        when(contaRepository.findById(2L)).thenReturn(destino);
        when(contaRepository.findById(99L)).thenReturn(null);
    }

    @Test
    void deveDepositarComSucesso() {
        // ARRANGE
        BigDecimal valor = new BigDecimal("200.00");

        // ACT
        transacaoService.depositar(1L, valor);

        // ASSERT
        assertEquals(0, origem.getSaldo().compareTo(new BigDecimal("1200.00")));
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).persist((Conta) any(Conta.class));
    }

    @Test
    void deveSacarComSucesso() {
        // ARRANGE
        BigDecimal valor = new BigDecimal("300.00");

        // ACT
        transacaoService.sacar(1L, valor);

        // ASSERT
        assertEquals(0, origem.getSaldo().compareTo(new BigDecimal("700.00")));
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).persist((Conta) any(Conta.class));
    }

    @Test
    void deveTransferirComSucesso() {
        // ARRANGE
        BigDecimal valor = new BigDecimal("200.00");

        // ACT
        transacaoService.transferir(1L, 2L, valor);

        // ASSERT
        assertEquals(0, origem.getSaldo().compareTo(new BigDecimal("800.00")));
        assertEquals(0, destino.getSaldo().compareTo(new BigDecimal("700.00")));

        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).findById(2L);
        verify(contaRepository, times(2)).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaNaoExisteNoDeposito() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.depositar(99L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository, times(1)).findById(99L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaNaoExisteNoSaque() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.sacar(99L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository, times(1)).findById(99L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoContaOrigemNaoExisteNaTransferencia() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.transferir(99L, 2L, new BigDecimal("100.00"))
        );

        // ASSERT
        assertEquals("Conta não encontrada.", ex.getMessage());
        verify(contaRepository, times(1)).findById(99L);
        verify(contaRepository, times(1)).findById(2L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDoDepositoForZero() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.depositar(1L, BigDecimal.ZERO)
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDoSaqueForNegativo() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.sacar(1L, new BigDecimal("-10.00"))
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroQuandoValorDaTransferenciaForZero() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.transferir(1L, 2L, BigDecimal.ZERO)
        );

        // ASSERT
        assertEquals("Valor inválido.", ex.getMessage());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).findById(2L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroSaldoInsuficienteNoSaque() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.sacar(1L, new BigDecimal("5000.00"))
        );

        // ASSERT
        assertEquals("Saldo insuficiente.", ex.getMessage());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }

    @Test
    void deveLancarErroSaldoInsuficienteNaTransferencia() {
        // ARRANGE

        // ACT
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> transacaoService.transferir(1L, 2L, new BigDecimal("5000.00"))
        );

        // ASSERT
        assertEquals("Saldo insuficiente.", ex.getMessage());
        verify(contaRepository, times(1)).findById(1L);
        verify(contaRepository, times(1)).findById(2L);
        verify(contaRepository, never()).persist((Conta) any(Conta.class));
    }
}
