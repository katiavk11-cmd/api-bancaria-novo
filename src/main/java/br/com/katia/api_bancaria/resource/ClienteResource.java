package br.com.katia.api_bancaria.resource;

import br.com.katia.api_bancaria.entity.Cliente;
import br.com.katia.api_bancaria.repository.ClienteRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClienteResource {

    @Inject
    ClienteRepository clienteRepository;

    @POST
    @Transactional
    public Response criar(Cliente cliente) {
        clienteRepository.persist(cliente);
        return Response.ok(cliente).build();
    }

    @GET
    public List<Cliente> listar() {
        return clienteRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Cliente buscar(@PathParam("id") Long id) {
        Cliente cliente = clienteRepository.findById(id);

        if (cliente == null) {
            throw new NotFoundException("Cliente não encontrado");
        }

        return cliente;
    }
}