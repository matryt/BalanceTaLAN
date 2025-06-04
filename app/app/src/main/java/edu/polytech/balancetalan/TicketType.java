package edu.polytech.balancetalan;

public enum TicketType {
    OTHER("Autre", R.drawable.autre),
    NETWORK("Réseau", R.drawable.reseau),
    COMFORT("Confort", R.drawable.confort),
    EQUIPMENT("Equipement", R.drawable.equipement),
    CHEAT("Triche", R.drawable.triche),
    INSTALLATION("Installation", R.drawable.installation);

    private String description;
    private int drawable;

    TicketType(String description, int drawable) {
        this.description = description;
        this.drawable = drawable;
    }

    public static TicketType getFromString(String desc) {
        for (TicketType t: values()) {
            if (t.description.equals(desc)) return t;
        }
        return OTHER;
    }

    public int getDrawable() {
        return drawable;
    }

}
