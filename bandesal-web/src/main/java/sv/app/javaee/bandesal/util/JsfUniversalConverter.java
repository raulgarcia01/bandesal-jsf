package sv.app.javaee.bandesal.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Raul Garcia
 */
public class JsfUniversalConverter implements Converter {

    /**
     * Variable que se encarga de manejar los logs en la aplicacion.
     */
    private static final Logger log = LogManager.getLogger(JsfUniversalConverter.class);

    /**
     * Cache del objeto convertido.
     */
    private static final String OBJECT_CACHE_KEY = "JSF_UNIVERSAL_CONVERTER_OBJECT_CACHE";

    /*
	 * (non-Javadoc)
	 * @see
	 * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public final Object getAsObject(final FacesContext fc,
            final UIComponent uic, final String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        Object returnObject = getObjectCache(fc).get(string);
        log.debug("User Selected " + string
                + " and we're returning this object from the cache: "
                + returnObject);
        return returnObject;
    }

    /*
	 * (non-Javadoc)
	 * @see
	 * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
     */
    @Override
    public final String getAsString(final FacesContext fc,
            final UIComponent uic, final Object o) {
        if (o == null) {
            return "";
        }
        String returnString = null;
        Map<String, Object> objectCache = getObjectCache(fc);
        // search cacheMap to see if this has already been converted.
        for (Map.Entry<String, Object> cacheEntry : objectCache.entrySet()) {
            Object cachedObject = cacheEntry.getValue();
            if (cachedObject == null) {
                continue;
            }
            if (o.equals(cachedObject)) {
                returnString = cacheEntry.getKey();
            }
        }
        if (returnString == null) {
            returnString = UUID.randomUUID().toString();
            objectCache.put(returnString, o);
        }
        log.debug("We're converting " + o + " into this String: "
                + returnString);
        return returnString;
    }

    /**
     * Método que saca el objeto del cache.
     *
     * @param fc Contexto de faces
     * @return Retorn el objeto del cache o lo crea
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getObjectCache(final FacesContext fc) {
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(
                true);
        Object object = session.getAttribute(OBJECT_CACHE_KEY);
        if (object instanceof Map) {
            return (Map<String, Object>) object;
        } else {
            Map<String, Object> objectCache = new HashMap<>();
            session.setAttribute(OBJECT_CACHE_KEY, objectCache);
            return objectCache;
        }
    }

}
