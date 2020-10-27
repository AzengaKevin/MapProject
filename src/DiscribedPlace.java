
public class DiscribedPlace extends Place {

    private String discribed;

    public DiscribedPlace(String name, String described, Category category, int posX, int posY) {
        super(name, category, posX, posY);
        this.discribed = described;
        this.setType("Described");
    }

    public String getName() {
        return this.getName();
    }

    public String getDescribed() {
        return this.discribed;
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

    public void setDescribed(String described) {
        this.discribed = described;
    }

    public void setCategory(Category category) {
        this.setCategory(category);
    }

    public void setPosition(Position point) {
        this.setPoint(point);
    }

    @Override
    public Place getObj() {
        return new DiscribedPlace(this.getName(), this.discribed, this.getCategory(), this.getPoint().getPosX(), this.getPoint().getPosY());
    }

    @Override
    public String getDescription() {
        return this.discribed;
    }

}
