package sv.app.javaee.bandesal.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Clase utilitaria para dinamizar los Querys y Criterios en JPA.
 *
 * @author Raul Garcia
 */
public class QueryBundle {

    private static final String RAW_TYPES = "rawtypes";
    private static final String UN_CHECKED = "unchecked";
    /**
     * Expresión where.
     */
    @SuppressWarnings(RAW_TYPES)
    private Expression where;

    /**
     * Elemento raiz del criterio.
     */
    @SuppressWarnings(RAW_TYPES)
    private Root root;

    /**
     * Consulta con criterio.
     */
    @SuppressWarnings(RAW_TYPES)
    private CriteriaQuery query;

    /**
     * builder.
     */
    private CriteriaBuilder builder;

    /**
     * Administrador de Entidades de JPA.
     */
    private EntityManager em;

    /**
     * Lista de ordernamientos.
     */
    private List<Order> orders = new LinkedList<Order>();

    /**
     * Constructor por defecto de la clase.
     *
     * @param em Administrador de Entidades de JPA.
     * @param entityClass Clase de la entidad que se esta manejando
     */
    @SuppressWarnings({RAW_TYPES, UN_CHECKED})
    public QueryBundle(final EntityManager em, final Class entityClass) {
        this.em = em;
        builder = em.getCriteriaBuilder();
        query = builder.createQuery();
        root = query.from(entityClass);
    }

    /**
     * Coloca una selección exacta de campos en la consulta.
     *
     * @param path Campo de la consulta
     * @param distinct Especifica si es unica o no la consulta
     * @return QueryBundle
     */
    @SuppressWarnings(UN_CHECKED)
    public final QueryBundle setSelect(final String path, final boolean distinct) {
        query.select(createPath(path));
        query.distinct(distinct);
        return this;
    }

    /**
     * Coloca una selección exacta de campos en la consulta.
     *
     * @param path Campo de la consulta
     * @return query bundle
     */
    public final QueryBundle setSelect(final String path) {
        return setSelect(path, true);
    }

    /**
     * Crea todos las consultas de lectura.
     *
     * @param em Clase de la entidad que se esta manejando
     * @param entityClass Clase de la entidad que se esta manejando
     * @return QueryBundle
     */
    @SuppressWarnings({RAW_TYPES, UN_CHECKED})
    public static QueryBundle createReadAllQuery(final EntityManager em,
            final Class entityClass) {
        QueryBundle q = new QueryBundle(em, entityClass);
        q.query.select(q.root);
        return q;
    }

    /**
     * Create read count query.
     *
     * @param em Clase de la entidad que se esta manejando
     * @param entityClass Clase de la entidad que se esta manejando
     * @return query bundle
     */
    @SuppressWarnings({RAW_TYPES, UN_CHECKED})
    public static QueryBundle createReadCountQuery(final EntityManager em,
            final Class entityClass) {
        QueryBundle q = new QueryBundle(em, entityClass);
        q.query.select(q.builder.count(q.root));
        return q;
    }

    /**
     * Retorna el valor de builder.
     *
     * @return builder
     */
    public final CriteriaBuilder getBuilder() {
        return builder;
    }

    /**
     * Retorna el valor de query.
     *
     * @return query
     */
    @SuppressWarnings(RAW_TYPES)
    public final CriteriaQuery getQuery() {
        return query;
    }

    /**
     * Retorna el valor de root.
     *
     * @return root
     */
    @SuppressWarnings(RAW_TYPES)
    public final Root getRoot() {
        return root;
    }

    /**
     * Retorna el valor de where.
     *
     * @return where
     */
    @SuppressWarnings(RAW_TYPES)
    public final Expression getWhere() {
        return where;
    }

    /**
     * Compila las consultas.
     *
     * @return query
     */
    @SuppressWarnings(UN_CHECKED)
    public final Query compile() {
        if (where != null) {
            query.where(where);
        }
        if (!orders.isEmpty()) {
            query.orderBy(orders);
        }

        return em.createQuery(query);
    }

    /**
     * Coloca los filtros de la consulta.
     *
     * @param filter Filtros de la consulta
     * @return QueryBundle
     */
    @SuppressWarnings(RAW_TYPES)
    public final QueryBundle setFilter(final Map filter) {
        if (filter != null) {
            for (Object key : filter.keySet()) {
                appendFilter(String.valueOf(key), filter.get(key));
            }
        }
        return this;
    }

    /**
     * Agrega el ordenamiento.
     *
     * @param path Campo de la consulta
     * @param desc Orden descendente o ascendente
     * @return QueryBundle
     */
    public final QueryBundle appendOrder(final String path, final boolean desc) {
        if (path == null) {
            return this;
        }
        return appendOrder(createPath(path), desc);
    }

    /**
     * Agrega el orden.
     *
     * @param path Campo de la consulta
     * @param desc Orden descendente o ascendente
     * @return QueryBundle
     */
    @SuppressWarnings(RAW_TYPES)
    public final QueryBundle appendOrder(final Path path, final boolean desc) {
        Order order = null;
        if (desc) {
            order = builder.desc(path);
        } else {
            order = builder.asc(path);
        }

        if (!orders.contains(order)) {
            orders.add(order);
        }
        return this;
    }

    /**
     * Agrega el filtro.
     *
     * @param property La propiedad del filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    public final QueryBundle appendFilter(final String property,
            final Object expr) {
        if (expr instanceof String) {
            return appendLikeFilter(property, (String) expr);
        } else {
            return appendEqFilter(property, expr);
        }
    }

    /**
     * Agrega el filtro igual.
     *
     * @param property La propiedad del filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    public final QueryBundle appendEqFilter(final String property,
            final Object expr) {
        if (property.contains("&&")) {
            appendEqFilter(property.replace("&&", ""), true, true, expr);
        } else if (property.contains("||")) {
            appendEqFilter(property.replace("||", ""), true, false, expr);
        } else {
            appendEqFilter(property, true, true, expr);
        }

        return this;
    }

    /**
     * Agrega el filtro igual.
     *
     * @param property La propiedad del filtro
     * @param include si se incluye o no el filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    public final QueryBundle appendEqFilter(final String property,
            final boolean include, final Object expr) {
        if (property.contains("&&")) {
            appendEqFilter(property.replace("&&", ""), include, true, expr);
        } else if (property.contains("||")) {
            appendEqFilter(property.replace("||", ""), include, false, expr);
        } else {
            appendEqFilter(property, true, true, expr);
        }

        return this;
    }

    /**
     * Agrega el filtro igual.
     *
     * @param property La propiedad del filtro
     * @param include si se incluye o no el filtro
     * @param and agrega las expresión and al filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    @SuppressWarnings({RAW_TYPES, UN_CHECKED})
    public final QueryBundle appendEqFilter(final String property,
            final boolean include, final boolean and, final Object expr) {
        Path path = createPath(property);
        Expression e = createInPredicate(expr, path);
        if (!include) {
            e = builder.not(e);
        }
        if (where != null) {
            if (and) {
                where = builder.and(where, e);
            } else {
                where = builder.or(where, e);
            }
        } else {
            where = e;
        }

        return this;
    }

    /**
     * Agrega el filtro like.
     *
     * @param property La propiedad del filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    public final QueryBundle appendLikeFilter(final String property,
            final String expr) {
        if (property.contains("&&")) {
            appendLikeFilter(property.replace("&&", ""), true, true, expr);
        } else if (property.contains("||")) {
            appendLikeFilter(property.replace("||", ""), true, false, expr);
        } else {
            appendLikeFilter(property, true, true, expr);
        }
        return this;

    }

    /**
     * Agrega el filtro like.
     *
     * @param property La propiedad del filtro
     * @param include si se incluye o no el filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    public final QueryBundle appendLikeFilter(final String property,
            final boolean include, final String expr) {
        if (property.contains("&&")) {
            appendLikeFilter(property.replace("&&", ""), include, true, expr);
        } else if (property.contains("||")) {
            appendLikeFilter(property.replace("||", ""), include, false, expr);
        } else {
            appendLikeFilter(property, include, true, expr);
        }

        return this;
    }

    /**
     * Agrega el filtro like.
     *
     * @param property La propiedad del filtro
     * @param include si se incluye o no el filtro
     * @param and agrega las expresión and al filtro
     * @param expr la expresión del filtro
     * @return QueryBundle
     */
    @SuppressWarnings({RAW_TYPES, UN_CHECKED})
    public final QueryBundle appendLikeFilter(final String property,
            final boolean include, final boolean and, final String expr) {
        String wildCard = "%";
        String value = wildCard + expr + wildCard;
        Path path = createPath(property);
        Expression e = builder.like(builder.upper(path), value);
        if (!include) {
            e = builder.not(e);
        }
        if (where != null) {
            if (and) {
                where = builder.and(where, e);
            } else {
                where = builder.or(where, e);
            }
        } else {
            where = e;
        }
        return this;
    }

    /**
     * Create path.
     *
     * @param path Campo de la consulta
     * @return path Campo de la consulta
     */
    @SuppressWarnings(RAW_TYPES)
    final Path createPath(final String path) {
        Path result = root;
        for (String sub : path.split("\\.")) {
            try {
                result = result.get(sub);
            } catch (IllegalArgumentException e) {
                return result;
            }
        }
        return result;
    }

    /**
     * Crea la consulta dentro de predicate.
     *
     * @param value Valor de la consulta
     * @param path Campo de la consulta
     * @return predicate
     */
    @SuppressWarnings({RAW_TYPES, UN_CHECKED})
    public static Predicate createInPredicate(final Object value,
            final Path path) {
        if (value instanceof Collection) {
            return path.in((Collection) value);
        } else if (value instanceof Object[]) {
            return path.in((Object[]) value);
        } else {
            return path.in(value);
        }
    }
}
