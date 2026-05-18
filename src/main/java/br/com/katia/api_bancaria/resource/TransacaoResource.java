package br.com.katia.api_bancaria.resource;

import br.com.katia.api_bancaria.dto.DepositoRequest;
import br.com.katia.api_bancaria.dto.SaqueRequest;
import br.com.katia.api_bancaria.dto.TransferenciaRequest;
import br.com.katia.api_bancaria.service.TransacaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/transacoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransacaoResource {

    @Inject
    TransacaoService service;

    // =========================
    // DEPÓSITO
    // =========================
    @POST
    @Path("/deposito/{id}")
    public String depositar(@PathParam("id") Long id,
                            DepositoRequest req) {

        service.depositar(id, req.getValor());
        return "Depósito realizado com sucesso";
    }

    // =========================
    // SAQUE
    // =========================
    @POST
    @Path("/saque/{id}")
    public String sacar(@PathParam("id") Long id,
                        SaqueRequest req) {

        service.sacar(id, req.getValor());
        return "Saque realizado com sucesso";
    }

    // =========================
    // TRANSFERÊNCIA
    // =========================
    @POST
    @Path("/transferencia")
    public String transferir(TransferenciaRequest req) {

        service.transferir(
                req.getContaOrigemId(),
                req.getContaDestinoId(),
                req.getValor()
        );

        return "Transferência realizada com sucesso";
    }
}