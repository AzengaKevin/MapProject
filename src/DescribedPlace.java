public class DescribedPlace extends Place {

    private String description;

    public DescribedPlace(String name, Category category, double posX, double posY, String description) {
        super(name, category, posX, posY);
        this.description = description;

        setType("Described");
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%.2f,%.2f,%s,%s", "Described", category.toName(), position.getPosX(), position.getPosY(), name, description);
    }

    public static Place fromString(String placeStr) {

        String[] parts = placeStr.split(",");

        try {
            return new DescribedPlace(parts[4], Category.fromString(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[5]);
        } catch (NumberFormatException e) {
            System.err.println("Format Exception: " + e.getLocalizedMessage());
            return null;
        }

    }

}
