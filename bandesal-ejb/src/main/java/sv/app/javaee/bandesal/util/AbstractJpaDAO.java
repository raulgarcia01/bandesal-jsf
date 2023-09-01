package sv.app.javaee.bandesal.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase abstracta que contiene la lógica para el manejo del JPA.
 *
 * @author Raul Garcia
 */
public abstract class AbstractJpaDAO<T> implements Serializable {

    /**
     * Variable que se encarga de manejar los logs en la aplicacion.
     */
    private static final Logger log = LogManager.getLogger(AbstractJpaDAO.class);

    /**
     * Serial de la clase.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante que indica el nombre de la clase.
     */
    private static final String CLASS_NAME = "AbstractJpaDAO";

    /**
     * Constante que indica el nombre del método público getById.
     */
    public static final String GET_BY_ID = "getById";

    /**
     * Constante que indica el nombre del método público getAll.
     */
    public static final String GET_ALL = "getAll";

    /**
     * Constante que indica el nombre del método público create.
     */
    public static final String CREATE = "create";

    /**
     * Constante que indica el nombre del método público update.
     */
    public static final String UPDATE = "update";

    /**
     * Constante que indica el nombre del método público save.
     */
    public static final String SAVE = "save";

    /**
     * Constante que indica el nombre del método público delete.
     */
    public static final String DELETE = "delete";

    /**
     * Constante que indica el nombre del método público findOneResult.
     */
    public static final String FIND_ONE_RESULT = "findOneResult";

    /**
     * Constante que indica el nombre del método público findOneResultbySelect.
     */
    public static final String FIND_ONE_RESULT_BY_SELECT = "findOneResultbySelect";

    /**
     * Constante que indica el nombre del método público findStringResult.
     */
    public static final String FIND_STRING_RESULT = "findStringResult";

    /**
     * Constante que indica el nombre del método público findStringResult.
     */
    public static final String FIND_RESULT_LIST = "findResultList";

    /**
     * Representa clase de un Entity.
     */
    private Class<T> entityClass;

    /**
     * Administrador de la persistencia de JPA.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor por defecto de la clase.
     *
     * @param entityClass La clase de la entidad a manejar
     */
    public AbstractJpaDAO(final Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Retorna la entidad por medio de su id Integer.
     *
     * @param id Id de la entidad
     * @return Retorna la entidad
     */
    public final T getById(final Integer id) {
        T result = null;
        try {
            result = getEntityManager().find(entityClass, id);
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en obtener ID de BD: ", e.getMessage(), GET_BY_ID));
        }
        return result;
    }

    /**
     * Retorna la entidad por medio de su id Long.
     *
     * @param id Id de la entidad
     * @return Retorna la entidad
     */
    public final T getById(final Long id) {
        T result = null;
        try {
            result = getEntityManager().find(entityClass, id);
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en obtener ID de BD: ", e.getMessage(), GET_BY_ID));
        }
        return result;
    }

    /**
     * Retorna todos los registros encontrados de una entidad.
     *
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public final List<T> getAll() {
        List<T> results = null;
        try {
            results = getEntityManager().createQuery(
                    "from " + entityClass.getName()).getResultList();
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en obtener registros en BD: ", e.getMessage(), GET_ALL));
        }
        return results;
    }

    /**
     * Crea la entidad.
     *
     * @param entity Entidad a crear
     */
    public final void create(final T entity) {
        try {
            getEntityManager().persist(entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(this.createLogMessage("Error en crear registros en BD: ", e.getMessage(), CREATE));
        }
    }

    /**
     * Actualiza la entidad.
     *
     * @param entity Entidad a actualizar
     * @return Retorna la entidad actualizada
     */
    public final T update(final T entity) {
        T updated = null;
        try {
            updated = (T) getEntityManager().merge(entity);
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en actualizar registros en BD: ", e.getMessage(), UPDATE));
        }
        return updated;
    }

    /**
     * Actualiza la entidad.
     *
     * @param entity Entidad a actualizar
     * @return Retorna la entidad actualizada
     */
    public final T save(final T entity) {
        T updated = null;
        try {
            updated = (T) getEntityManager().merge(entity);
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en almacenar registros en BD: ", e.getMessage(), SAVE));
        }
        return updated;
    }

    /**
     * Borra la entidad.
     *
     * @param entity Entidad a borrar
     */
    public final void delete(final T entity) {
        try {
            getEntityManager().remove(getEntityManager().merge(entity));
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en eliminar registros en BD: ", e.getMessage(), DELETE));
        }
    }

    /**
     * Borra la entidad por medio de su id Integer.
     *
     * @param entityId Id de la entidad a borrar
     */
    public final void deleteById(final Integer entityId) {
        final T entity = getById(entityId);
        delete(entity);
    }

    /**
     * Borra la entidad por medio de su id Integer.
     *
     * @param entityId Id de la entidad a borrar
     */
    public final void deleteById(final Long entityId) {
        final T entity = getById(entityId);
        delete(entity);
    }

    /**
     * Encuentra una entidad dependiendo de un nameQuery y sus parametros.
     *
     * @param namedQuery NameQuery para encontrar la entidad
     * @param parameters Parametros del NameQuery
     * @return Retorna la entidad dependiendo del nameQuery
     */
    public final T findOneResult(final String namedQuery,
            final Map<String, Object> parameters) {
        T result = null;

        try {
            Query query = getEntityManager().createNamedQuery(namedQuery);

            // Method that will populate parameters if they are passed not null
            // and empty
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (T) query.getSingleResult();

        } catch (NoResultException e) {
            return result;
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en encontrar un registro en BD: ", e.getMessage(), FIND_ONE_RESULT));
        }

        return result;
    }

    /**
     * Encuentra una entidad dependiendo de una consulta normal y sus
     * parametros.
     *
     * @param selectQuery Consulta normal para encontrar la entidad
     * @param parameters Parametros de la consulta
     * @return Retorna la entidad dependiendo de la consulta
     */
    @SuppressWarnings("unchecked")
    public final T findOneResultbySelect(final String selectQuery,
            final Map<String, Object> parameters) {
        T result = null;

        try {
            Query query = getEntityManager().createQuery(selectQuery);

            // Method that will populate parameters if they are passed not null
            // and empty
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (T) query.getSingleResult();

        } catch (NoResultException e) {
            return result;
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en encontrar registros en BD: ", e.getMessage(), FIND_ONE_RESULT_BY_SELECT));
        }

        return result;
    }

    /**
     * Encuentra un string dependiendo de un nameQuery y sus parametros.
     *
     * @param namedQuery NameQuery para encontrar el string
     * @param parameters Parametros del NameQuery
     * @return Retorna un string dependiendo del nameQuery
     */
    public final String findStringResult(final String namedQuery,
            final Map<String, Object> parameters) {
        String result = null;

        try {
            Query query = getEntityManager().createNamedQuery(namedQuery);

            // Method that will populate parameters if they are passed not null
            // and empty
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (String) query.getSingleResult();

        } catch (NoResultException e) {
            return result;
        } catch (Exception e) {
            log.error(this.createLogMessage("Error en encontrar registros en BD: ", e.getMessage(), FIND_STRING_RESULT));
        }

        return result;
    }

    /**
     * Método encargado de llenar los parametros de un Query.
     *
     * @param query Query al que se le llenaran los parametros
     * @param parameters Parametros a llenar el en Query
     */
    private void populateQueryParameters(final Query query,
            final Map<String, Object> parameters) {

        for (Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Encuentra una lista de entidades dependiendo de un nameQuery y sus
     * parametros.
     *
     * @param namedQuery NameQuery para encontrar la entidad
     * @param parameters Parametros del NameQuery
     * @return Retorna la lista de entidades entidades dependiendo del nameQuery
     */
    
    @SuppressWarnings("unchecked")
    public final List<T> findResultList(final String namedQuery,
            final Map<String, Object> parameters) {
        List<T> result = null;

        try {
            Query query = getEntityManager().createNamedQuery(namedQuery);

            // Method that will populate parameters if they are passed not null
            // and empty
            if (parameters != null && !parameters.isEmpty()) {
                populateQueryParameters(query, parameters);
            }

            result = (List<T>) query.getResultList();

        } catch (Exception e) {
            log.error(this.createLogMessage("Error en encontrar registros en BD: ", e.getMessage(), FIND_RESULT_LIST));
            
        }

        return result;
    }

    /**
     * Crea el query bundle.
     *
     * @return QueryBundle
     */
    protected QueryBundle createQueryBundle() {
        return new QueryBundle(getEntityManager(), entityClass);
    }

    /**
     * Crea la lectura de todos los query bundle.
     *
     * @return QueryBundle
     */
    protected QueryBundle createReadAllQueryBundle() {
        return QueryBundle.createReadAllQuery(getEntityManager(), entityClass);
    }

    /**
     * Crea la lectura de todos los query.
     *
     * @return query
     */
    protected Query createReadAllQuery() {
        return createReadAllQueryBundle().compile();
    }

    /**
     * Crea la lectura del conteo de los CountQueryBundle.
     *
     * @return QueryBundle
     */
    protected QueryBundle createReadCountQueryBundle() {
        return QueryBundle
                .createReadCountQuery(getEntityManager(), entityClass);
    }

    /**
     * Create la lectura del conteo del query.
     *
     * @return query
     */
    protected Query createReadCountQuery() {
        return createReadCountQueryBundle().compile();
    }

    /**
     * Retorna todos los registros encontrados de una entidad.
     *
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public final List<T> findAll() {
        return createReadAllQueryBundle().compile().getResultList();
    }

    /**
     * Find range.
     *
     * @param range the range
     * @return list
     */
    @SuppressWarnings("unchecked")
    public final List<T> findRange(final int[] range) {
        Query q = createReadAllQuery();
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    /**
     * Retorna la cantidad de registros encontrados para la tabla.
     *
     * @return int Cantidad de Registros.
     */
    public final int count() {
        return ((Long) createReadCountQuery().getSingleResult()).intValue();
    }

    /**
     * Retorna la cantidad de registros encontrados para la tabla.
     *
     * @param filters Los Filtros especificados para la tabla.
     * @return int Cantidad de Registros.
     */
    public final int count(final Map<String, Object> filters) {

        QueryBundle qb = null;
        qb = createReadCountQueryBundle().setFilter(filters);
        javax.persistence.Query q = qb.compile();
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Método para la carga lazy de los datos de una tabla.
     *
     * @param first El numero de pagina seleccionado de la tabla.
     * @param pageSize La cantidad de paginas de la tabla.
     * @param sortField El campo por el que se esta ordenando la tabla.
     * @param sortOrder El tipo de ordenamiento pedido (Ascendente o
     * Descendente)
     * @param filters Los Filtros especificados para la tabla.
     * @return list Retorna la cantidad de los registros encontrados para esa
     * pagina seleccionada.
     */
    @SuppressWarnings("unchecked")
    public final List<T> load(final int first, final int pageSize,
            final String sortField, final SortOrder sortOrder,
            final Map<String, Object> filters) {

        QueryBundle qb = null;

        if (sortField != null && !sortOrder.equals(SortOrder.UNSORTED)) {
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                qb = createReadAllQueryBundle().appendOrder(sortField, false)
                        .setFilter(filters);
            } else {
                qb = createReadAllQueryBundle().appendOrder(sortField, true)
                        .setFilter(filters);
            }
        } else {
            qb = createReadAllQueryBundle().setFilter(filters);

        }
        javax.persistence.Query q = qb.compile().setMaxResults(pageSize)
                .setFirstResult(first);
        return q.getResultList();
    }

    /**
     * Método encargado de inicialiar los parametros pasados con estrategia Lazy
     * El método no necesita hacer nada el simple llamdo de la propiedad hace
     * que se inicialice la clase.
     *
     * @param entity Lista de entidades a inicializar
     */
    public void initialize(final Object entity) {
    }

    /**
     * Método encargado de inicialiar la lista de entidades pasadas con
     * estrategia lazy.
     *
     * @param entitys Lista de entidades a inicializar
     */
    public final void initialize(final Collection<?> entitys) {
        entitys.size();
    }
    /**
     * Retorna el administrador de persistencia de JPA.
     *
     * @return Administrador de persistencia JPA
     */
    public final EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Coloca el administrador de persistencia de JPA.
     *
     * @param entityManager Administrador de persistencia JPA
     */
    public final void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private String createLogMessage(String description, String message, String method) {
        StringBuilder sb = new StringBuilder();
        sb.append(description);
        sb.append(message);
        sb.append("Clase: ");
        sb.append(CLASS_NAME);
        sb.append("Metodo: ");
        sb.append(method);
        return sb.toString();
    }

}
