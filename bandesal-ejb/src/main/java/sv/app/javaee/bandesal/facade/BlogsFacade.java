package sv.app.javaee.bandesal.facade;

import java.util.List;
import javax.ejb.Local;
import sv.app.javaee.bandesal.model.Blogs;

/**
 *
 * @author Raul Garcia
 */
@Local
public interface BlogsFacade {

    public List<Blogs> getAllBlogs();

    public void addBlog(Blogs blog);

    public Blogs updateBlog(Blogs blog);

    public void deleteBlog(Blogs blog);

    public Blogs getBlogById(Integer id);

}
