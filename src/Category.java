
import javafx.scene.paint.Color;

public class Category {
    private String catename;
    private Color colorMiddle;
    private Color colorBorder;

    Category(String catename){
        this.catename = catename;
        switch (catename) {
            case "Bus":
                this.colorMiddle = Color.RED;
                this.colorBorder = Color.BLACK;
                break;

            case "Underground":
                this.colorMiddle = Color.BLUE;
                this.colorBorder = Color.BLACK;
                break;

            case "Train":
                this.colorMiddle = Color.GREEN;
                this.colorBorder = Color.BLACK;
                break;

            default :
                this.colorMiddle = Color.BLACK;
                this.colorBorder = Color.GRAY;
                break;
        }

    }

    public String getCatename() {
        return catename;
    }

    public void setCatename(String catename) {
        this.catename = catename;
    }

    public Color getColorMiddle() {
        return colorMiddle;
    }

    public void setColorMiddle(Color colorMiddle) {
        this.colorMiddle = colorMiddle;
    }

    public Color getColorBorder() {
        return colorBorder;
    }

    public void setColorBorder(Color colorBorder) {
        this.colorBorder = colorBorder;
    }

}
