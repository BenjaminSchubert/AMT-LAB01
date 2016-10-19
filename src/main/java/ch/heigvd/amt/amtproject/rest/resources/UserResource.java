package ch.heigvd.amt.amtproject.rest.resources;

import ch.heigvd.amt.amtproject.model.User;
import ch.heigvd.amt.amtproject.rest.dto.PasswordUserDTO;
import ch.heigvd.amt.amtproject.rest.dto.UserDTO;
import ch.heigvd.amt.amtproject.services.UserDAOLocal;
import ch.heigvd.amt.amtproject.util.Errors;
import ch.heigvd.amt.amtproject.util.FieldLength;
import ch.heigvd.amt.amtproject.util.PATCH;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Path("/users")
public class UserResource {
    @EJB
    UserDAOLocal userDAO;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam(value="username") String username) {
        User u;
        try {
            u = userDAO.get(username);
            if (u == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }
        } catch (SQLException e) {
            return Response
                    .serverError()
                    .build();
        }
        return Response
                .ok(new UserDTO(u.getId(), u.getUsername(), u.getQuote()))
                .build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<UserDTO> res = new ArrayList<>();
        try {
            userDAO.getAll()
                    .forEach(u -> res.add(
                            new UserDTO(u.getId(), u.getUsername(), u.getQuote()))
                    );
        } catch (SQLException e) {
            return Response
                    .serverError()
                    .build();
        }
        return Response
                .ok(res)
                .build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(PasswordUserDTO user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (username == null || username.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Errors.USERNAME_MISSING)
                    .build();
        }

        if ( password == null || password.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Errors.PASSWORD_MISSING)
                    .build();
        }

        if (username.length() > FieldLength.USERNAME_MAX_LENGTH ) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Errors.USERNAME_TOO_LONG)
                    .build();
        }

        if (password.length() > FieldLength.PASSWORD_MAX_LENGTH) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Errors.PASSWORD_TOO_LONG)
                    .build();
        }

        try {
            if (userDAO.get(username) != null) {
                return Response
                        .status(Response.Status.CONFLICT)
                        .build();
            }
            userDAO.create(username, password);
        } catch (SQLException e) {
            return Response
                    .serverError()
                    .build();
        }

        URI href = uriInfo
                .getBaseUriBuilder()
                .path(UserResource.class)
                .path(UserResource.class, "getUser")
                .build(username);

        return Response.created(href).build();
    }

    @PATCH
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("username") String username, PasswordUserDTO user) {
        try {
            User u = userDAO.get(username);
            if (u == null) {
                return Response
                        .status(Response.Status.NOT_FOUND)
                        .build();
            }

            if (user.getPassword() != null) {
                if (user.getPassword().length() > FieldLength.PASSWORD_MAX_LENGTH) {
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(Errors.PASSWORD_TOO_LONG)
                            .build();
                }

                u.setPassword(user.getPassword());
            }

            if (user.getQuote() != null) {
                if (user.getQuote().length() > FieldLength.QUOTE_MAX_LENGTH) {
                    return Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(Errors.QUOTE_TOO_LONG)
                            .build();
                }

                u.setQuote(user.getQuote());
            }

            userDAO.update(u);
        } catch (SQLException e) {
            return Response
                    .serverError()
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @DELETE
    @Path("/{username}")
    public Response delete(@PathParam(value="username") String username) {
        try {
            userDAO.delete(username);
        } catch (SQLException e) {
            return Response
                    .serverError()
                    .build();
        }
        return Response
                .noContent()
                .build();
    }
}
