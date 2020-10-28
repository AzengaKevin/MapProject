public class Position {

    private double posX;
    private double posY;

    public Position(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public void setPosX(double x) {
        this.posX = x;
    }

    public void setPosY(double y) {
        this.posY = y;
    }
}
