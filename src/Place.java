
import javafx.scene.shape.Polygon;

public abstract class Place extends Polygon {

    private String name;
    private Category category;
    private Position point;
    private boolean hiden;
    private boolean selected;
    private String type;


    public Place(String name, Category category, int posX, int posY) {
        super();
        super.getPoints().addAll(new Double[]{
                Double.valueOf(posX), Double.valueOf(posY),
                Double.valueOf(posX) - 15.00 , Double.valueOf(posY) -30.00,
                Double.valueOf(posX) + 15.00 , Double.valueOf(posY) - 30.00
        });
        this.name = name;
        this.category = category;
        this.point = new Position(posX, posY);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPoint() {
        return point;
    }

    public void setPoint(Position point) {
        this.point = point;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void print(){
        setFill(category.getColorMiddle());
        setStroke(category.getColorBorder());
    }

    public String getDescription(){
        return "";
    }

    public void setSelect(boolean selected){
        this.selected = selected;
    }

    public void setHiden(boolean hiden){
        this.hiden = hiden;
    }

    public boolean isHiden(){
        return this.hiden;
    }

    public boolean isSelected(){
        return this.selected;
    }

    public abstract Place getObj();

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeObj(){
        return this.type;
    }

}
