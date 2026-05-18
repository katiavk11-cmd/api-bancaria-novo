package br.com.katia.api_bancaria.resource;

import br.com.katia.api_bancaria.entity.Cliente;
import br.com.katia.api_bancaria.entity.Conta;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;

@Path("/contas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContaResource {

    // =========================
    // CRIAR CONTA
    // =========================
    @POST
    @Transactional
    public Response criarConta(ContaRequest request) {

        Cliente cliente = Cliente.findById(request.clienteId);

        if (cliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente não encontrado")
                    .build();
        }

        Conta conta = new Conta();
        conta.setCliente(cliente);
        conta.setSaldo(BigDecimal.ZERO);

        conta.persist();

        return Response.ok(conta).build();
    }

    // =========================
    // DEPÓSITO
    // =========================
    @POST
    @Path("/{id}/deposito")
    @Transactional
    public Response depositar(@PathParam("id") Long id, MovimentoRequest request) {

        Conta conta = Conta.findById(id);

        if (conta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        conta.setSaldo(conta.getSaldo().add(request.valor));
        return Response.ok(conta).build();
    }

    // =========================
    // SAQUE
    // =========================
    @POST
    @Path("/{id}/saque")
    @Transactional
    public Response sacar(@PathParam("id") Long id, MovimentoRequest request) {

        Conta conta = Conta.findById(id);

        if (conta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (conta.getSaldo().compareTo(request.valor) < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Saldo insuficiente")
                    .build();
        }

        conta.setSaldo(conta.getSaldo().subtract(request.valor));
        return Response.ok(conta).build();
    }

    // =========================
    // SALDO
    // =========================
    @GET
    @Path("/{id}/saldo")
    public Response saldo(@PathParam("id") Long id) {

        Conta conta = Conta.findById(id);

        if (conta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(conta.getSaldo()).build();
    }

    // =========================
    // TRANSFERÊNCIA
    // =========================
    @POST
    @Path("/transferencia")
    @Transactional
    public Response transferir(TransferenciaRequest request) {

        Conta origem = Conta.findById(request.contaOrigemId);
        Conta destino = Conta.findById(request.contaDestinoId);

        if (origem == null || destino == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Conta não encontrada")
                    .build();
        }

        if (origem.getSaldo().compareTo(request.valor) < 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Saldo insuficiente")
                    .build();
        }

        origem.setSaldo(origem.getSaldo().subtract(request.valor));
        destino.setSaldo(destino.getSaldo().add(request.valor));

        return Response.ok("Transferência realizada").build();
    }

    // =========================
    // DTOs INTERNOS
    // =========================

    public static class ContaRequest {
        public Long clienteId;
    }

    public static class MovimentoRequest {
        public BigDecimal valor;
    }

    public static class TransferenciaRequest {
        public Long contaOrigemId;
        public Long contaDestinoId;
        public BigDecimal valor;
    }
}
