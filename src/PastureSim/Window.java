package PastureSim;

import java.awt.*;
import javax.swing.*;

public class Window extends Canvas {
    Main m;
    int squareSize = 10;

    public Window(int size, Main m){
        this.m = m;
        JFrame frame = new JFrame();
        frame.setSize(size*squareSize, size*squareSize + 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setVisible(true);
    }

    public void paint(Graphics g){
    for (int i = 0; i < Main.SIZE; i++){
        for (int j = 0; j < Main.SIZE; j++){
            Entity currentEntity = this.m.getMap()[i][j];
            int type = m.entityType(currentEntity);
            Color color;
            color = Color.WHITE;
            if (type == 1){ //wolf
                color = Color.GRAY;
            } else if (type == 0){ //sheep
                color = Color.BLUE;
            } else if (type == 2){ //grass
                color = Color.GREEN;
            }
            g.setColor(color);
            g.fillRect(i*this.squareSize,j*this.squareSize,this.squareSize,this.squareSize);
        }
    }
    }
}
