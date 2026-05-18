package br.com.katia.api_bancaria.dto;

import java.math.BigDecimal;

public class DepositoRequest {

    private Long contaId;
    private BigDecimal valor;

    public DepositoRequest() {
    }

    public Long getContaId() {
        return contaId;
    }

    public void setContaId(Long contaId) {
        this.contaId = contaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}