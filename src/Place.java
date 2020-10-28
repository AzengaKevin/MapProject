
import javafx.scene.shape.Polygon;

public class Place extends Polygon {

    private static final String DEFAULT_TYPE = "Named";

    protected String name;
    protected Category category;
    protected Position position;
    protected boolean hidden;
    protected boolean selected;
    private String type = DEFAULT_TYPE;

    public Place(String name, Category category, double posX, double posY) {
        super(posX, posY, (posX - 15.00), (posY - 30.00), (posX + 15.00), (posY - 30.00));
        this.name = name;
        this.category = category;
        this.position = new Position(posX, posY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void print() {
        setFill(category.getColorMiddle());
        setStroke(category.getColorBorder());
    }

    public void setSelect(boolean selected) {
        this.selected = selected;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%.2f,%.2f,%s", "Named", category.getName(), position.getPosX(), position.getPosY(), name);
    }

    public static Place fromString(String placeStr) {

        String[] parts = placeStr.split(",");

        try {
            return new Place(parts[4], new Category(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        } catch (NumberFormatException e) {
            System.err.println("Format Exception: " + e.getLocalizedMessage());
            return null;
        }

    }
}
