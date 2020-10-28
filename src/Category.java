
import javafx.scene.paint.Color;

public enum Category {

    Bus(Color.RED, Color.BLACK),
    Underground(Color.BLUE, Color.BLACK),
    Train(Color.GREEN, Color.BLACK),
    None(Color.GRAY, Color.BLACK);


    private Color fill;
    private Color stroke;

    Category(Color fill, Color stroke) {

        this.fill = fill;
        this.stroke = stroke;

    }

    public Color getFill() {
        return fill;
    }

    public void setFill(Color fill) {
        this.fill = fill;
    }

    public Color getStroke() {
        return stroke;
    }

    public void setStroke(Color stroke) {
        this.stroke = stroke;
    }

    public static Category fromString(String s) {
        switch (s) {
            case "Bus":
                return Bus;
            case "Underground":
                return Underground;
            case "Train":
                return Train;
            default:
                return None;
        }
    }

    public String toName() {
        return name();
    }
}
