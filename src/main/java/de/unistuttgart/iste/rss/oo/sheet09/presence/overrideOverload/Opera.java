package de.unistuttgart.iste.rss.oo.sheet09.presence.overrideOverload;

/**
 * A class for an opera
 *
 */
public class Opera extends Building {

    private static final String OPERA_LITERAL_NLS = "Opera";

    /**
     * Constructor for the class
     * @param name
     */
    public Opera(final String name) {
        super(name);
    }

    @Override
    protected String getBuildingType() {
        return OPERA_LITERAL_NLS;
    }

}
