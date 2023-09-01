package sv.app.javaee.bandesal.facade;

import javax.ejb.Stateless;
import sv.app.javaee.bandesal.model.Blogs;
import sv.app.javaee.bandesal.util.AbstractJpaDAO;

/**
 * Clase que aplica el patrón DAO para la entidad Blogs Facade.
 *
 * @author Raul Garcia
 */
@Stateless
public class BlogsFacadeImpl extends AbstractJpaDAO<Blogs>  {

    private static final long serialVersionUID = 1L;

    public BlogsFacadeImpl() {
        super(Blogs.class);
    }

}
