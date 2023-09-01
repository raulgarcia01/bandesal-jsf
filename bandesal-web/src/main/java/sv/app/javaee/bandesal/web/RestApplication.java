package sv.app.javaee.bandesal.web;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configura que todos los endpoints de REST inicien con Path API.
 *
 * @author Raul Garcia
 */
@ApplicationPath("/api")
public class RestApplication extends Application {

}
