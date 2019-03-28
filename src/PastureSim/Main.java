package PastureSim;

import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

//Element 1 of entity array: Sheep
//Element 2 of entity array:  Wolves

public class Main {

    static int SIZE = 70;
    static String MALEFILENAME = "src/PastureSim/malenames.txt";
    static String FEMALEFILENAME = "src/PastureSim/femalenames.txt";
    private int numSheep;
    private int numWolves;
    private int initialSheep;
    private int initialWolves;
    private Entity[][] map;
    Random r;
    File names;
    public ArrayList<String> maleNameList;
    public ArrayList<String> femaleNameList;
    static int GRASS_START_HEALTH = 5;
    static int SHEEP_START_HEALTH = 5;
    static int WOLF_START_HEALTH = 15;
    static double WOLF_DAMAGE_MULTIPLIER = 0.5;
    Scanner in;



    public static void main(String[] args) throws InterruptedException {
        Main inst = new Main();
        inst.run();
    }

    void run() throws InterruptedException {
        this.in = new Scanner(System.in);
        this.getStartVars();
        this.r = new Random();
        System.out.println("Generating names...");
        this.generateNames();
        System.out.println("Generating map...");
        this.newMap();
        Window window = new Window(SIZE, this);
        Scanner in = new Scanner(System.in);
        String next;
        do {
            for (int i = 0; i < 10; i++) {
                this.resetMoveStatus();
                this.advance();
            }
            this.printMap();
            window.repaint();
            //System.out.print("Would you like to continue?");
            //next = in.nextLine().toLowerCase();
            TimeUnit.SECONDS.sleep(3);
        } while (true);
        //System.out.println("Shutting Down!");
        //in.close();
    }

    void printMap(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                char curr = ' ';
                Entity currentEntity = this.map[i][j];
                int type = entityType(currentEntity);
                if (type == 1){
                    curr = 'W';
                } else if (type == 0){
                    curr = 'S';
                } else if (type == 2){
                    curr = 'G';
                }
                System.out.print(curr);
            }
            System.out.println();
        }
    }

    void advance(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (this.map[i][j] != null) {
                    this.map[i][j].advanceTurn();
                }
            }
        }
    }

    void generateNames() {
        this.names = new File(MALEFILENAME);
        this.maleNameList = new ArrayList<>();
        addNamesToArray(true);
        this.femaleNameList = new ArrayList<>();
        this.names = new File(FEMALEFILENAME);
        addNamesToArray(false);

    }

    String getName(boolean gender) {
        return gender ? this.maleNameList.get(r.nextInt(this.maleNameList.size())) : this.femaleNameList.get(r.nextInt(this.femaleNameList.size()));
    }

    void addNamesToArray(boolean g) {
        try {
            Scanner inFile = new Scanner(names);
            int i = 0;
            while (inFile.hasNextLine()) {
                if (g) {
                    this.maleNameList.add(inFile.nextLine());
                } else {
                    this.femaleNameList.add(inFile.nextLine());
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File error!");
        }
    }

    private void getStartVars() {
        do {
            System.out.print("Please enter the number of sheep to start the simulation: ");
            this.initialSheep = Integer.parseInt(in.nextLine());
            System.out.print("Please enter the number of wolves to start the simulation: ");
            this.initialWolves = Integer.parseInt(in.nextLine());
            if (this.initialSheep + this.initialWolves > Math.pow((double) SIZE, 2) /2) {
                System.out.println("That is too many animals yo! Try being less of a goddamn fool next time.");
            }
        } while (this.initialSheep + this.initialWolves > Math.pow((double) SIZE, 2) / 2);
    }


    void newMap() {
        this.setMap(new Entity[SIZE][SIZE]);
        System.out.println("Spawning Sheep..");
        this.spawnInitialSheep();
        System.out.println("Spawning Wolves..");
        this.spawnWolves();
        System.out.println("Spawning Grass..");
        this.spawnInitialGrass();
    }

    private void spawnInitialGrass(){
        int x, y;
        for (int i = 0; i < (Math.pow((double)SIZE, 2)/2) - (this.numSheep + numWolves); i++ ){
            if (this.r.nextInt(100) > 60) {
                do {
                    x = r.nextInt(SIZE);
                    y = r.nextInt(SIZE);
                } while (!spawnGrass(x, y));
            }
        }
    }

    private void spawnInitialSheep() {
        int x, y;
        for (int i = 0; i < this.initialSheep; i++) {
            do {
                x = r.nextInt(SIZE);
                y = r.nextInt(SIZE);
            } while (!spawnEntity(x, y, true, SHEEP_START_HEALTH));
        }
        System.out.println("Sheep spawned!");
    }

    public boolean spawnGrass(int x, int y){
        return this.addEntity(new Grass(x, y, this));
    }

    public boolean spawnEntity(int x, int y, boolean type, int health) {
        boolean gender = r.nextBoolean();
        String name = this.getName(gender);
        return type ? this.addEntity(new Sheep(x, y, name, this, gender, health)) : this.addEntity(new Wolf(x, y, name, this, gender, health));
    }

    private void spawnWolves() {
        int x, y;
        for (int i = 0; i < this.initialWolves; i++) {
            do {
                x = r.nextInt(SIZE);
                y = r.nextInt(SIZE);
            } while (!spawnEntity(x, y, false, WOLF_START_HEALTH));
        }
        System.out.println("Wolves spawned!");
    }

    public Entity[][] getMap() {
        return this.map;
    }

    public void setMap(Entity[][] map) {
        this.map = map;
    }

    public void move(int xPos, int yPos, Entity e) {
        this.map[e.getY()][e.getX()] = null;
        this.map[yPos][xPos] = e;
        e.setX(xPos);
        e.setY(yPos);
        //System.out.println("Moving Entity to x = " + xPos + ", y = " + yPos);
    }

    public boolean validPos(int xPos, int yPos){
        return !((xPos >= SIZE || yPos >= SIZE) || (xPos < 0 || yPos < 0));

        }

    public Entity getEntityAtPos(int xPos, int yPos) {

        return this.getMap()[yPos][xPos];
    }

    public int getRand(int lBound, int uBound) {
        return r.nextInt(uBound - lBound) + lBound;
    }


    //Returns true if addition of entity was a success, false if failure
    public boolean addEntity(Entity entity) {
        if (this.getMap()[entity.getY()][entity.getX()] != null) {
            return false;
        } else {
            this.map[entity.getY()][entity.getX()] = entity;
            int type = entityType(entity);
            if (type == 1) {
                this.numWolves++;
            } else if (type == 0) {
                this.numSheep++;
            }
            return true;
        }
    }

    public void removeEntity(int xPos, int yPos, Entity entity) {
        this.map[yPos][xPos] = null;
        int type = entityType(entity);
        if (type == 1) {
            this.numWolves--;
        } else if (type == 0) {
            this.numSheep--;
        }
    }
    
    public int entityType(Entity e){
        if (e instanceof Wolf) {
            return 1;
        } else if (e instanceof Sheep) {
            return 0;
        } else if (e instanceof Grass){
            return 2;
        }
        return -1;
    }

    private void resetMoveStatus(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (this.map[j][i] != null){
                    this.map[j][i].hasMoved = false;
                }
            }
        }
    }

    Entity[][] getAdjacent(int xPos, int yPos) {
        Entity[][] adjacent = new Entity[3][3];
        for (int x = -1; x < 2; x++) {
            if (xPos + x > -1 && xPos + x < SIZE) {
                for (int y = -1; y < 2; y++) {
                    if (yPos + y > -1 && yPos + y < SIZE) {
                        adjacent[y + 1][x + 1] = this.map[yPos + y][xPos + x];
                    } else {
                        adjacent[y + 1][x + 1] = null;
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    adjacent[i][x + 1] = null;
                }
            }
        }
        return adjacent;
    }


}
