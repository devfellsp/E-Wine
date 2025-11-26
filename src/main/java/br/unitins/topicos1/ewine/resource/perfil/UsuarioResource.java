package br.unitins.topicos1.ewine.resource.perfil;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

 
import br.unitins.topicos1.ewine.dto.perfil.UsuarioDTO;
import br.unitins.topicos1.ewine.dto.perfil.UsuarioDTOResponse;
import br.unitins.topicos1.ewine.model.perfil.Usuario;
import br.unitins.topicos1.ewine.service.perfil.UsuarioService;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService service;

    @GET
    public List<UsuarioDTOResponse> listar() {
        return service.findAll().stream()
                .map(UsuarioDTOResponse::valueOf)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Usuario u = service.findById(id);
        if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(UsuarioDTOResponse.valueOf(u)).build();
    }

    @POST
    @Transactional
    public Response criar(UsuarioDTO dto) {
        Usuario criado = service.create(dto);
        UsuarioDTOResponse resp = UsuarioDTOResponse.valueOf(criado);
        return Response.created(URI.create("/usuarios/" + criado.getId())).entity(resp).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response alterar(@PathParam("id") Long id, UsuarioDTO dto) {
        service.update(id, dto);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response apagar(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}