package sv.app.javaee.bandesal.faces;

/**
 * Items class constans definition,
 *
 * @author Raul Garcia
 */
public class ItemsConstans {

    // Prevents instanciation of myself and my subclasses
    private ItemsConstans() {
    }

    /**
     * NamedQuery by Status and Name.
     */
    public static final String FIND_BY_STATUS_AND_NAME = "findByStatusAndName";
    /**
     * Param Status for NamedQuery .
     */
    public static final String PARAM_STATUS = "paramStatus";

    /**
     * Param Name for NamedQuery.
     */
    public static final String PARAM_NAME = "paramName";
}
