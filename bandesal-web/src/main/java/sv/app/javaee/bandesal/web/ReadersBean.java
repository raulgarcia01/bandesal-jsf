package sv.app.javaee.bandesal.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import sv.app.javaee.bandesal.facade.ReadersFacade;
import sv.app.javaee.bandesal.model.Readers;

/**
 * CRUD Bean para el manejo de Readers.
 *
 * @author Raul Garcia
 */
@Named(value = "readersBean")
@ViewScoped
public class ReadersBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Readers> readers;

    private Readers selectedReader;

    @Inject
    private ReadersFacade readerService;

    @PostConstruct
    public void init() {
        this.readers = this.readerService.getAllReaders();
        this.selectedReader = new Readers();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        String id = request.getParameter("id");
        
    }

    public void openNew() {
        this.selectedReader = new Readers();
    }

    public void saveReader() throws IOException {
        System.out.println("Entra");
        if (this.selectedReader.getId() == null) {
            this.readerService.addReader(selectedReader);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Readers Added"));
        } else {
            this.readerService.updateReader(selectedReader);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Readers Updated"));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("readers.xhtml");
    }

    public void deleteReader(Readers itemSelected) {
        this.readers.remove(itemSelected);
        this.readerService.deleteReader(itemSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Readers Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-readers");
    }

    public void updateReader(Readers itemSelected) throws IOException {
        this.selectedReader = itemSelected;
        FacesContext.getCurrentInstance().getExternalContext().redirect("editReader.xhtml?id=" + itemSelected.getId());
    }

    public List<Readers> getReaders() {
        return readers;
    }

    public void setReaders(List<Readers> readers) {
        this.readers = readers;
    }

    public Readers getSelectedReader() {
        return selectedReader;
    }

    public void setSelectedReader(Readers selectedReader) {
        this.selectedReader = selectedReader;
        System.out.println("SET de Readers " + this.selectedReader);
    }

}
