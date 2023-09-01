package sv.app.javaee.bandesal.facade;

import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import sv.app.javaee.bandesal.model.Readers;
import sv.app.javaee.bandesal.util.AbstractJpaDAO;

/**
 * Clase que aplica el patrón DAO para la entidad Readers Facade.
 *
 * @author Raul Garcia
 */
@LocalBean
@Stateless
public class ReadersFacadeImpl extends AbstractJpaDAO<Readers> implements ReadersFacade {

    private static final long serialVersionUID = 1L;

    public ReadersFacadeImpl() {
        super(Readers.class);
    }

    @Override
    public List<Readers> getAllReaders() {
        return super.findAll();
    }

    @Override
    public void addReader(Readers reader) {
        super.create(reader);
    }

    @Override
    public Readers updateReader(Readers reader) {
        return super.update(reader);
    }

    @Override
    public void deleteReader(Readers reader) {
        super.delete(reader);
    }

    @Override
    public Readers getReaderById(Integer id) {
        return super.getById(id);
    }

}
