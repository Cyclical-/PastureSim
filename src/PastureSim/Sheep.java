package PastureSim;

public class Sheep extends Entity {
    private String name;
    private boolean gender;

    Sheep(int x, int y, String name, Main m, boolean g, int health) {
        super(x, y, m, health);
        this.name = name;
        this.gender = g;
    }


    public String getName() {
        return name;
    }


    void doAction(int x, int y) {
        Entity e = this.m.getEntityAtPos(this.x + x, this.y + y);
        if (e == null) {
            this.m.move(this.x + x, this.y + y, this);
        } else if (e instanceof Sheep) {
            if (((Sheep) e).gender != this.gender && !e.hasMoved) {
                this.mate(e, true);
            }
        } else if (e instanceof Grass) {
            this.eatGrass((Grass) e);
        }

    }



    private void eatGrass(Grass grass) {
        int x = grass.getX();
        int y = grass.getY();
        grass.die();
        this.health += grass.health * 0.5;
        this.m.move(x, y, this);
        System.out.println("Sheep " + this.name + " ate a grass!");
    }


}
