package PastureSim;

import java.util.ArrayList;

public class Grass extends Entity {


    Grass(int x, int y, Main m){
        super(x,y, m, Main.GRASS_START_HEALTH);

    }

    void doAction(int x, int y) {
    }

    public void advanceTurn() {
        this.adjacent = this.m.getAdjacent(this.x, this.y);
        this.health--;
        if (this.health <=0 ) {
            this.die();
        } else if (this.m.r.nextInt(100) > 70){
            this.spread();
        }
    }

    private void spread(){
        ArrayList<int[]> adjacent = this.adjacentFreeTile();
        if (adjacent.size() != 0) {
            int[] pos = adjacent.get(this.m.getRand(0, adjacent.size()));
            this.m.spawnGrass(pos[0], pos[1]);
        }
    }
}
