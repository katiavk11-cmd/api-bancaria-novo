package br.com.katia.api_bancaria.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
public class Transacao extends PanacheEntity {

    @Column(nullable = false)
    public String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id")
    public Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    public Conta contaDestino;

    @Column(nullable = false, precision = 15, scale = 2)
    public BigDecimal valor;

    @Column(nullable = false)
    public LocalDateTime dataHora;

    // -------------------------------------------------
    // FACTORY METHOD (CORRETO)
    // -------------------------------------------------
    public static Transacao criar(
            String tipo,
            Conta origem,
            Conta destino,
            BigDecimal valor
    ) {
        Transacao t = new Transacao();
        t.tipo = tipo;
        t.contaOrigem = origem;
        t.contaDestino = destino;
        t.valor = valor;
        t.dataHora = LocalDateTime.now();
        return t;
    }
}