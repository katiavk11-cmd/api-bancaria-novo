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
        contaOrigem.setTitular("João");
        contaOrigem.setSaldo(new BigDecimal("1000.00"));

        contaDestino = new Conta();
        contaDestino.setTitular("Maria");
        contaDestino.setSaldo(new BigDecimal("500.00"));

        when(contaRepository.findById(1L)).thenReturn(contaOrigem);
        when(contaRepository.findById(2L)).thenReturn(contaDestino);
    }

    @Test
    void deveDepositarComSucesso() {
        // Arrange já feito no setup()

        // Act
        contaService.depositar(1L, new BigDecimal("200.00"));

        // Assert
        assertEquals(0,
                contaOrigem.getSaldo().compareTo(new BigDecimal("1200.00")));

        verify(contaRepository).persist(contaOrigem);
    }

    @Test
    void deveSacarComSucesso() {
        // Act
        contaService.sacar(1L, new BigDecimal("300.00"));

        // Assert
        assertEquals(0,
                contaOrigem.getSaldo().compareTo(new BigDecimal("700.00")));

        verify(contaRepository).persist(contaOrigem);
    }

    @Test
    void deveLancarSaldoInsuficiente() {
        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.sacar(1L, new BigDecimal("5000.00"))
        );

        assertEquals("Saldo insuficiente.", ex.getMessage());

        verify(contaRepository, never()).persist(any(Conta.class));
    }

    @Test
    void deveTransferirComSucesso() {
        // Act
        contaService.transferir(1L, 2L, new BigDecimal("200.00"));

        // Assert
        assertEquals(0,
                contaOrigem.getSaldo().compareTo(new BigDecimal("800.00")));

        assertEquals(0,
                contaDestino.getSaldo().compareTo(new BigDecimal("700.00")));

        verify(contaRepository).persist(contaOrigem);
        verify(contaRepository).persist(contaDestino);
    }

    @Test
    void deveLancarMesmaConta() {
        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> contaService.transferir(1L, 1L, new BigDecimal("100.00"))
        );

        assertEquals(
                "Contas de origem e destino devem ser diferentes.",
                ex.getMessage()
        );

        verify(contaRepository, never()).persist(any(Conta.class));
    }
}