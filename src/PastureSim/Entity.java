package PastureSim;

import java.util.ArrayList;

public abstract class Entity {
    protected int x, y;
    Main m;
    protected int health;
    public boolean hasMoved;
    protected Entity[][] adjacent;


    Entity(int x, int y, Main main, int health) {
        this.setX(x);
        this.setY(y);
        this.m = main;
        this.health = health;
    }

    public int getX() {
        return x;
    }

    abstract void doAction(int x, int y);

    public void setX(int x) {
        this.x = x;
    }

    void advanceTurn() {
        this.adjacent = this.m.getAdjacent(this.x, this.y);
        if (!this.hasMoved) {
            if (this.health == 0) {
                this.die();
            } else {
                int x = 0, y = 0;
                do {
                    int nextMove = m.getRand(0, 8);
                    switch (nextMove) {
                        case 0:
                            x = -1;
                            y = -1;
                            break;
                        case 1:
                            x = 0;
                            y = -1;
                            break;
                        case 2:
                            x = 1;
                            y = -1;
                            break;
                        case 3:
                            x = -1;
                            y = 0;
                            break;
                        case 4:
                            x = 1;
                            y = 0;
                            break;
                        case 5:
                            x = -1;
                            y = 1;
                            break;
                        case 6:
                            x = 0;
                            y = 1;
                            break;
                        case 7:
                            x = 0;
                            y = 1;
                            break;
                        case 8:
                            x = 1;
                            y = 1;
                            break;
                    }
                } while (!this.m.validPos(this.x + x, this.y + y));
                this.health -=2;
                if (this instanceof Sheep){
                    ((Sheep) this).health -= 2;
                }
                this.doAction(x, y);
            }
        }
        this.hasMoved = true;
    }

    protected ArrayList<int[]> adjacentFreeTile() {
        ArrayList<int[]> pos = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            if (this.x + i > -1 && this.x + i < Main.SIZE) {
                for (int j = -1; j < 2; j++) {
                    if (this.y + j > -1 && this.y + j < Main.SIZE) {
                        if (this.adjacent[j+1][i+1] == null) {
                            int[] coord = {this.x + i,this.y + j};
                            pos.add(coord);
                        }
                    }
                }
            }
        }
        return pos;
    }


    protected void mate(Entity s, boolean type) {
        ArrayList<int[]> adjacent = adjacentFreeTile();
        if (adjacent.size() != 0) {
            int[] pos = adjacent.get(this.m.getRand(0, adjacent.size()));
            int health = this.health /2;
            this.m.spawnEntity(pos[0], pos[1], type, health);
            s.hasMoved = true;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    protected boolean checkPopulation(){
     boolean overpopulated = false;
     int count = 0;
     for(int i = 0; i < this.adjacent.length; i++){
         for (int j = 0; j < this.adjacent[0].length; j++){
             //TODO: find how many adjacent tiles are of the same species. Make sure to check for map boundaries.
         }
     }
     return overpopulated;
    }

    protected void die() {
        this.m.removeEntity(this.x, this.y, this);
    }

    public int getHealth() {
        return this.health;
    }


    public void setHealth(int health) {
        this.health = health;
    }
}
