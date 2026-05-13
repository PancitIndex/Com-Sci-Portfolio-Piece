import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Snake game CHANGED
public class GamePanel extends JPanel implements ActionListener{
    
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final int SIZE = 10;
    private int score = 0;
    
    private final int MAX = 900;
    
    private int x[] = new int[MAX];
    private int y[] = new int[MAX];
    
    private int segments;
    private int apple_x;
    private int apple_y;
    
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    
    private boolean inGame = true;
    
    private Timer timer;
    
    public GamePanel(){
        setBackground(new Color(0,0,150)); 
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        initGame();
    }
    
    public void initGame(){
        segments = 3;
        
        for(int z=0; z<segments; z++){
            x[z] = 50 - z*10;
            y[z] = 50;
        }
        
        newApple();
        
        timer = new Timer(140, this);
        timer.start();
    }
    
    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Score: " + score, 10, 15);
        if(inGame){
            g.setColor(Color.red);
            g.fillRect(apple_x, apple_y, SIZE, SIZE);
            
            for(int z=0; z<segments; z++){
                if(z == 0){
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(0, 150, 0));
                }
                
                g.fillRect(x[z], y[z], SIZE, SIZE);
            }
            
            Toolkit.getDefaultToolkit().sync();
            
        } else {
            gameOver(g);
        }
    }
    
    public void gameOver(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Game Over", 100, 140);
    }
    
    private void newApple() {
        boolean onSnake;
        do {
            onSnake = false;
            apple_x = (int) (Math.random()*30) * SIZE;
            apple_y = (int) (Math.random()*30) * SIZE;
            
            for(int i=0; i<segments; i++){
                if(x[i] == apple_x && y[i] == apple_y){
                    onSnake = true;
                    break;
                }
            }
        } while(onSnake);
        apple_x = (int) (Math.random()*30) * SIZE;
        apple_y = (int) (Math.random()*30) * SIZE;
    }
    
    private void move(){
        for(int z=segments; z > 0; z--){
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }
        
        if(left) {
            x[0] -= SIZE;
        }
        
        if(right) {
            x[0] += SIZE;
        }
        
        if(up) {
            y[0] -= SIZE;
        }
        
        if(down) {
            y[0] += SIZE;
        }
    }
    
    private void checkCollision() {
        
        for(int z=segments; z > 0; z--){
            if((z>4) && (x[0] == x[z]) && (y[0] == y[z])){
                inGame = false;
            }
        }
        
        if(y[0] >= HEIGHT){
            inGame = false;
        }
        
        if(y[0] < 0){
            inGame = false;
        }
        
        if(x[0] >= WIDTH){
            inGame = false;
        }
        
        if(x[0] < 0){
            inGame = false;
        }
        
        if(!inGame){
            timer.stop();
        }
    }
    
    public void checkApple(){
        if(x[0] == apple_x && y[0] == apple_y){
            segments++;
            score++;
            newApple();
        
            if(timer.getDelay() > 5){
                timer.setDelay(timer.getDelay() - 5);
            }
            newApple();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        
        repaint();
    }
    
    private class MyKeyAdapter extends KeyAdapter{
        
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            // arrow key controls
            if((key == KeyEvent.VK_LEFT) && (!right)){
                left = true;
                up = false;
                down = false;
            }
            
            if((key == KeyEvent.VK_RIGHT) && (!left)){
                right = true;
                up = false;
                down = false;
            }
            
            if((key == KeyEvent.VK_UP) && (!down)){
                up = true;
                right = false;
                left = false;
            }
            
            if((key == KeyEvent.VK_DOWN) && (!up)){
                down = true;
                right = false;
                left = false;
            }
            
            if(key == KeyEvent.VK_SPACE && !inGame){
                inGame = true;
                score = 0;
                initGame();
            }
            //space to restart when Game Over'd
        }
    }
}
