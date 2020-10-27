

public class NamedPlace extends Place {

    public NamedPlace(String name, Category category, int posX, int posY) {
        super(name, category, posX, posY);
        this.setType("Named");
    }

    public String getName() {
        return this.getName();
    }

    public Category getCategory() {
        return this.getCategory();
    }

    public Position getPosition() {
        return this.getPoint();
    }

    public void setName(String name) {
        this.setName(name);
    }

    public void setCategory(Category category) {
        this.setCategory(category);
    }

    public void setPosition(Position point) {
        this.setPoint(point);
    }



    @Override
    public Place getObj() {
        return new NamedPlace(this.getName(), this.getCategory(), this.getPoint().getPosX(), this.getPoint().getPosY());
    }

    @Override
    public String getDescription() {
        return "";
    }

}
