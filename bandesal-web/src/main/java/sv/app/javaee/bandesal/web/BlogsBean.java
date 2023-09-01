package sv.app.javaee.bandesal.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import sv.app.javaee.bandesal.facade.BlogsFacade;
import sv.app.javaee.bandesal.model.Blogs;

/**
 * CRUD Bean para el manejo de Blogs.
 *
 * @author Raul Garcia
 */
@Named(value = "blogsBean")
@ViewScoped
public class BlogsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Blogs> blogs;

    private Blogs selectedBlog;

    @Inject
    private BlogsFacade blogService;

    @PostConstruct
    public void init() {
        this.blogs = this.blogService.getAllBlogs();
        this.selectedBlog = new Blogs();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String id = request.getParameter("id");
        System.out.println("ID: " + id);
        if (id != null) {
            this.selectedBlog = this.blogService.getBlogById(Integer.parseInt(id));
        }
    }

    public void openNew() {
        this.selectedBlog = new Blogs();
    }

    public void saveBlog() throws IOException {
        System.out.println("Entra");
        if (this.selectedBlog.getId() == null) {
            this.blogService.addBlog(selectedBlog);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blogs Added"));
        } else {
            this.blogService.updateBlog(selectedBlog);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blogs Updated"));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("blogs.xhtml");
    }

    public void deleteBlog(Blogs itemSelected) {
        this.blogs.remove(itemSelected);
        this.blogService.deleteBlog(itemSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Blogs Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-blogs");
    }

    public void updateBlog(Blogs itemSelected) throws IOException {
        this.selectedBlog = itemSelected;
        FacesContext.getCurrentInstance().getExternalContext().redirect("editBlog.xhtml?id=" + itemSelected.getId());
    }

    public List<Blogs> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blogs> blogs) {
        this.blogs = blogs;
    }

    public Blogs getSelectedBlog() {
        return selectedBlog;
    }

    public void setSelectedBlog(Blogs selectedBlog) {
        this.selectedBlog = selectedBlog;
        System.out.println("SET de Blogs " + this.selectedBlog);
    }

}