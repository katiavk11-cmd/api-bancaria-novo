package br.com.katia.api_bancaria.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "conta")
@SequenceGenerator(
        name = "conta_seq",
        sequenceName = "conta_seq",
        allocationSize = 1
)
public class Conta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_seq")
    private Long id;

    private String titular;
    private BigDecimal saldo;
    private boolean ativa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Long getId() {
        return id;
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}