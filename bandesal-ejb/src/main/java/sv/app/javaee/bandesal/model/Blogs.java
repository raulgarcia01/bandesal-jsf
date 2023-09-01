package sv.app.javaee.bandesal.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que almacena informacion de los Blogs.
 * 
 * @author Raul Garcia
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blogs", schema = "public")
public class Blogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "blogs_id_seq", sequenceName = "blogs_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogs_id_seq")
    @Column(name = "id", updatable = false)
    private Integer id;

    @Size(min = 5, max = 50, message = "{message.sizeTextTo50}")
    @NotNull(message = "{message.notNull}")
    @Column(name = "title")
    private String title;
    
    @Size(min = 5, max = 4000, message = "{message.sizeTextTo4000}")
    @NotNull(message = "{message.notNull}")
    @Column(name = "description")
    private String description;
    

}
