package br.com.katia.api_bancaria.testutil;

import br.com.katia.api_bancaria.entity.Conta;

import java.math.BigDecimal;

public class ContaFactory {

    public static Conta criar(String titular, BigDecimal saldo) {

        Conta c = new Conta();
        c.setTitular(titular);
        c.setSaldo(saldo);

        return c;
    }
}