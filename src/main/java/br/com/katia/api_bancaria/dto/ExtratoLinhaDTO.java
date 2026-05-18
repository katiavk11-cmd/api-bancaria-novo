package br.com.katia.api_bancaria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExtratoLinhaDTO {

    public Long transacaoId;
    public String tipo;

    public BigDecimal valor;

    public Long contaOrigemId;
    public Long contaDestinoId;

    public LocalDateTime data;

    public BigDecimal saldoAposOperacao;

    // 👇 IMPORTANTE: para "vermelho" na UI
    public boolean saldoNegativo;
}