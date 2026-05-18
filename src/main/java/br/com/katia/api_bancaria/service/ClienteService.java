package br.com.katia.api_bancaria.service;

import br.com.katia.api_bancaria.entity.Cliente;
import br.com.katia.api_bancaria.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.listAll();
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        repository.persist(cliente);
        return cliente;
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id);
    }
}