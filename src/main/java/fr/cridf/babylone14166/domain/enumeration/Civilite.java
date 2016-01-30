package fr.cridf.babylone14166.domain.enumeration;

/**
 * The Civilite enumeration.
 */
public enum Civilite {
    MONSIEUR("M."), MADAME("Mme.");

    private String label;

    Civilite(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
