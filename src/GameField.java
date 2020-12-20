import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private ArrayList<Dot> snakeArray = new ArrayList<Dot>();
    private int mapSizeX = 20;
    private int mapSizeY = 20;
    private int dotSize = 16;
    private Timer timer;
    private int delayTime = 100;
    private Dot apple;
    private boolean isPlaying = true;
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = true;
    private boolean pressed = false;

    private boolean equalsDots(Dot first, Dot second) {
        return first.x == second.x && first.y == second.y;
    }

    private boolean isContain(Dot dot) {
        for(int i = 0; i < snakeArray.size(); i++) {
            if(equalsDots(snakeArray.get(i), dot)) return true;
        }
        return false;
    }

    public GameField() {
        initGame();
        createApple();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {
        setBackground(Color.black);

        snakeArray.add(new Dot(2, mapSizeY/2-1));
        snakeArray.add(new Dot(3, mapSizeY/2-1));
        snakeArray.add(new Dot(4, mapSizeY/2-1));

        timer = new Timer(delayTime, this);
        timer.start();
    }

    public void createApple() {
        do {
            apple = new Dot(new Random().nextInt(mapSizeX), new Random().nextInt(mapSizeY));
        }
        while(isContain(apple));
    }

    public void move() {
        Dot head = new Dot(snakeArray.get(snakeArray.size()-1).x, snakeArray.get(snakeArray.size()-1).y);

        if(up) head.y--;
        if(down) head.y++;
        if(left) head.x--;
        if(right) head.x++;

        if(isContain(head) || head.x >= mapSizeX || head.x < 0 || head.y >= mapSizeY || head.y < 0) isPlaying = false;

        snakeArray.add(head);

        if(!equalsDots(head, apple)) snakeArray.remove(0);
        else createApple();

        pressed = false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.green);
        for(int i = 0; i < snakeArray.size(); i++) {
            Dot dot = snakeArray.get(i);
            g.fillRect(dot.x*dotSize+1,dot.y*dotSize+1,dotSize,dotSize);
        }

        g.setColor(Color.red);
        g.fillRect(apple.x*dotSize+1,apple.y*dotSize+1,dotSize,dotSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isPlaying) {
            move();
            repaint();
        }
    }

    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(!pressed) {
                pressed = true;
                if (key == KeyEvent.VK_LEFT && !right) {
                    up = false;
                    down = false;
                    left = true;
                    right = false;
                } else if (key == KeyEvent.VK_RIGHT && !left) {
                    up = false;
                    down = false;
                    left = false;
                    right = true;
                } else if (key == KeyEvent.VK_UP && !down) {
                    up = true;
                    down = false;
                    left = false;
                    right = false;
                } else if (key == KeyEvent.VK_DOWN && !up) {
                    up = false;
                    down = true;
                    left = false;
                    right = false;
                }
            }
        }
    }
}
