package sv.app.javaee.bandesal.web;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import sv.app.javaee.bandesal.facade.ReadersFacade;
import sv.app.javaee.bandesal.model.Readers;

/**
 * CRUD Bean para el manejo de Readers.
 *
 * @author Raul Garcia
 */
@ManagedBean
@SessionScoped
public class ReadersBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Readers> readers;

    private Readers selectedReader;

    @Inject
    private ReadersFacade readerService;

    @PostConstruct
    public void init() {
        this.readers = this.readerService.getAllReaders();
    }

    public void openNew() {
        this.selectedReader = new Readers();
    }

    public void saveReader() {
        System.out.println("Entra");
        System.out.println(selectedReader);
        if (this.selectedReader.getId() == null) {
            this.readerService.addReader(selectedReader);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Readers Added"));
        } else {
            this.readerService.updateReader(selectedReader);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Readers Updated"));
        }

        PrimeFaces.current().executeScript("PF('manageReadersDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-readers");
    }

    public void deleteReader(Readers itemSelected) {
        this.readers.remove(itemSelected);
        this.readerService.deleteReader(itemSelected);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Readers Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dt-readers");
    }

    public void updateReader(Readers itemSelected) {
        this.selectedReader = itemSelected;
        System.out.println("this.selectedReader: " + this.selectedReader);
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
