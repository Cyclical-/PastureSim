package PastureSim;

public class Wolf extends Entity {
    private String name;
    private boolean gender; //true: male; false: female

    Wolf(int x, int y, String name, Main m, boolean g, int health) {
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
        } else if (e instanceof Grass){
            e.die();
            this.m.move(this.x + x, this.y + y, this);
        }
        else if (e instanceof Wolf) {
            if (((Wolf) e).gender != this.gender && !e.hasMoved) {
                this.mate(e, false);
            } else {
                if (this.m.r.nextBoolean()) {
                    this.fight((Wolf) e);
                }
            }
        } else if (e instanceof Sheep) {
            this.eatSheep((Sheep)e);
        }
    }


    public boolean getGender() {
        return gender;
    }

    private void eatSheep(Sheep s){
        int x = s.getX();
        int y = s.getY();
        s.die();
        this.health += s.health * 2 /3;
        this.m.move(x,y,this);
        System.out.println("Wolf " + this.name + " ate Sheep " + s.getName() + "!");
    }

    private void fight(Wolf w) {
        if (w.health < this.health) {
            if (w.getHealth() - this.attack(w) <= 0) {
                w.die();
            } else {
                w.setHealth(this.attack(w));
            }
        } else {
            if (this.health - w.attack(this) <= 0) {
                this.die();
            } else {
                this.setHealth(w.attack(this));
            }
        }
    }


    private int attack(Wolf w) {
        int opponentHealth = w.getHealth();
        return (int) ((this.health - opponentHealth) * Main.WOLF_DAMAGE_MULTIPLIER);
    }
}
