package br.com.katia.api_bancaria.service;

import br.com.katia.api_bancaria.entity.Cliente;
import br.com.katia.api_bancaria.repository.ClienteRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ClienteServiceTest {

    @Inject
    ClienteService clienteService;

    @InjectMock
    ClienteRepository clienteRepository;

    @Test
    void deveListarTodosClientes() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setNome("Ana");
        cliente.setCpf("111");
        cliente.setEmail("ana@test.com");

        when(clienteRepository.listAll()).thenReturn(List.of(cliente));

        // ACT
        List<Cliente> clientes = clienteService.listarTodos();

        // ASSERT
        assertEquals(1, clientes.size());
        assertEquals("Ana", clientes.get(0).getNome());
        verify(clienteRepository, times(1)).listAll();
    }

    @Test
    void deveSalvarClienteComSucesso() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setNome("Bruno");
        cliente.setCpf("222");
        cliente.setEmail("bruno@test.com");

        // ACT
        Cliente salvo = clienteService.salvar(cliente);

        // ASSERT
        assertNotNull(salvo);
        assertEquals("Bruno", salvo.getNome());
        verify(clienteRepository, times(1)).persist((Cliente) any(Cliente.class));
    }

    @Test
    void deveBuscarClientePorIdComSucesso() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos");
        cliente.setCpf("333");
        cliente.setEmail("carlos@test.com");

        when(clienteRepository.findById(1L)).thenReturn(cliente);

        // ACT
        Cliente encontrado = clienteService.buscarPorId(1L);

        // ASSERT
        assertNotNull(encontrado);
        assertEquals("Carlos", encontrado.getNome());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveRetornarNullQuandoClienteNaoExiste() {
        // ARRANGE
        when(clienteRepository.findById(99L)).thenReturn(null);

        // ACT
        Cliente encontrado = clienteService.buscarPorId(99L);

        // ASSERT
        assertNull(encontrado);
        verify(clienteRepository, times(1)).findById(99L);
    }
}