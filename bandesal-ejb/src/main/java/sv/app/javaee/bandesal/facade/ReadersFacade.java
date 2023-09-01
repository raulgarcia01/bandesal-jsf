/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package sv.app.javaee.bandesal.facade;

import java.util.List;
import javax.ejb.Local;
import sv.app.javaee.bandesal.model.Readers;

/**
 *
 * @author Raul Garcia
 */
@Local
public interface ReadersFacade  {
    
    public List<Readers> getAllReaders();
    
    public void addReader(Readers reader);
    
    public Readers updateReader(Readers reader);
    
    public void deleteReader(Readers reader);
    
    public Readers getReaderById(Integer id);
    
}
