import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Game extends JFrame implements KeyListener{

    //window vars
    private final int MAX_FPS; //maximum refresh rate
    private final int WIDTH; //window width
    private final int HEIGHT; //window height

    //double buffer strategy
    private BufferStrategy strategy;
    public ArrayList<Integer> keys = new ArrayList<>();

    //loop variables
    private boolean isRunning = true; //is the window running
    private long rest = 0; //how long to sleep the main thread

    //timing variables
    private float dt; //delta time
    private long lastFrame; //time since last frame
    private long startFrame; //time since start of frame
    private int fps; //current fps

    private Vector p;
    private Vector v;
    private Vector a;

    Rectangle myRectangle;

    public Game(int width, int height, int fps){
        super("My Game");
        this.MAX_FPS = fps;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    /*
     * init()
     * initializes all variables needed before the window opens and refreshes
     */
    void init(){
        //initializes window size
        setBounds(0, 0, WIDTH, HEIGHT);
        setResizable(false);

        //set jframe visible
        setVisible(true);

        //set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create double buffer strategy
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        addKeyListener(this);
        setFocusable(true);

        //set initial lastFrame var
        lastFrame = System.currentTimeMillis();

        //set background window color
        setBackground(Color.BLACK);

        p = new Vector(200, 200);
        v = new Vector(0, 0);
        a = new Vector(0, 0);

        myRectangle = new Rectangle(new Vector(20, 20), new Vector(10, 10), new Vector(5, 5), Color.green);
    }

    /*
     * update()
     * updates all relevant game variables before the frame draws
     */
    private void update(){
        //update current fps
        fps = (int)(1f/dt);

        handleKeys();

        myRectangle.update(dt);
        myRectangle.v.add(new Vector(1, 1));

        v.add(Vector.mult(a, dt));
        p.add(Vector.mult(v, dt));

        //handle game logic here
        if(p.x < 0 || p.x +250 > WIDTH){
            v.setX(-v.x);
            p.add(Vector.mult(v, dt));
        }
        if(p.y < 20 || p.y + 300 > HEIGHT){
            v.setY(-v.y);
            p.add(Vector.mult(v, dt));
        }

        //if(p.y < 20) p.setY(p.y + 10);
        //if(p.y + 300 > HEIGHT) p.setY(p.y - 10);
    }

    /*
     * draw()
     * gets the canvas (Graphics2D) and draws all elements
     * disposes canvas and then flips the buffer
     */
    private void draw(){
        //get canvas
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

        //clear screen
        g.clearRect(0,0,WIDTH, HEIGHT);

        g.setColor(Color.red);
        ///         x,      y,     width,  height
        g.fillRect(p.ix, p.iy, 250, 300);
        //g.drawOval();

        myRectangle.draw(g);

        //draw fps
        g.setColor(Color.GREEN);
        g.drawString(Long.toString(fps), 10, 40);

        //release resources, show the buffer
        g.dispose();
        strategy.show();
    }

    private void handleKeys(){
        for(int i = 0; i < keys.size(); i++){
            switch(keys.get(i)){
                case KeyEvent.VK_RIGHT:
                    a = new Vector(1000, 0);
                    break;
                case KeyEvent.VK_LEFT:
                    a = new Vector(-1000, 0);
                    break;
                case KeyEvent.VK_UP:
                    a = new Vector(0, -1000);
                    break;
                case KeyEvent.VK_DOWN:
                    a = new Vector(0, 1000);
                    break;
                case KeyEvent.VK_SPACE:
                    a = new Vector(0, 0);
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(!keys.contains(keyEvent.getKeyCode()))
            keys.add(keyEvent.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        for(int i = keys.size() - 1; i >= 0; i--)
            if(keys.get(i) == keyEvent.getKeyCode())
                keys.remove(i);
    }

    /*
         * run()
         * calls init() to initialize variables
         * loops using isRunning
            * updates all timing variables and then calls update() and draw()
            * dynamically sleeps the main thread to maintain a framerate close to target fps
         */
    public void run(){
        init();

        while(isRunning){

            //new loop, clock the start
            startFrame = System.currentTimeMillis();

            //calculate delta time
            dt = (float)(startFrame - lastFrame)/1000;

            //update lastFrame for next dt
            lastFrame = startFrame;

            //call update and draw methods
            update();
            draw();

            //dynamic thread sleep, only sleep the time we need to cap the framerate
            //rest = (max fps sleep time) - (time it took to execute this frame)
            rest = (1000/MAX_FPS) - (System.currentTimeMillis() - startFrame);
            if(rest > 0){ //if we stayed within frame "budget", sleep away the rest of it
                try{ Thread.sleep(rest); }
                catch (InterruptedException e){ e.printStackTrace(); }
            }
        }

    }

    //entry point for application
    public static void main(String[] args){
        Game game = new Game(800, 600, 60);
        game.run();
    }

}
