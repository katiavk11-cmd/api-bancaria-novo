package br.com.katia.api_bancaria.repository;

import br.com.katia.api_bancaria.entity.Conta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ContaRepository implements PanacheRepository<Conta> {

    public Optional<Conta> findByIdOptional(Long id) {
        return findByIdOptional(id);
    }
}