package sv.app.javaee.bandesal.web;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sv.app.javaee.bandesal.facade.ReadersFacadeImpl;
import sv.app.javaee.bandesal.model.Readers;

/**
 * Obtiene un listado de Readers utilizando GetMapping.
 *
 * @author Raul Garcia
 */
@Path("/readers")
@Stateless(mappedName = "readersAPI")
public class ReadersSecureAPI {

    /**
     * Variable que se encarga de manejar los logs en la aplicacion.
     */
    private static final Logger log = LogManager.getLogger(ReadersSecureAPI.class);

    private static final long serialVersionUID = 1L;

    @EJB
    ReadersFacadeImpl readersService;

    @GET
    @RolesAllowed("securedAPI")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReaders() {
        try {

            List<Readers> readersList = readersService.getAllReaders();

            return Response.ok().entity(readersList).build();

        } catch (Exception e) {
            log.info("Error al ejecutar GET Mapping: ", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(":( ").type(MediaType.APPLICATION_JSON).build();

        }
    }

}
