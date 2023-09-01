package sv.app.javaee.bandesal.facade;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import sv.app.javaee.bandesal.model.Blogs;
import sv.app.javaee.bandesal.util.AbstractJpaDAO;

/**
 * Clase que aplica el patrón DAO para la entidad Blogs Facade.
 *
 * @author Raul Garcia
 */
@LocalBean
@Stateless
public class BlogsFacadeImpl extends AbstractJpaDAO<Blogs> implements BlogsFacade {

    private static final long serialVersionUID = 1L;

    public BlogsFacadeImpl() {
        super(Blogs.class);
    }

    @Override
    public List<Blogs> getAllBlogs() {
        return super.findAll();
    }

    @Override
    public void addBlog(Blogs blog) {
        super.create(blog);
    }

    @Override
    public Blogs updateBlog(Blogs blog) {
        return super.update(blog);
    }

    @Override
    public void deleteBlog(Blogs blog) {
        super.delete(blog);
    }

    @Override
    public Blogs getBlogById(Integer id) {
        return super.getById(id);
    }

}
