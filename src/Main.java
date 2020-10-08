import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=1280, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 150,150);

    float[][] vecs;
    int px=0,py=0;

    public void createVecs(int num){
        vecs=new float[num][6];
        for (int i=0; i<num*4; i++){
            vecs[i/4][i%4]=(float)(Math.random()*6-3);
        }
        for (int i=0; i<num;i++){vecs[i][4]=(float)(Math.sqrt(Math.random()));vecs[i][5]=(float)(.1f*(Math.random()-.5));}

    }

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        createVecs(5);
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        paintCoordGrid(gfx);

        //RENDER FOREGROUND
        int ts=30;
        for (int x=0; x<WIDTH/ts; x++){
            for (int y=0; y<HEIGHT/ts; y++) {
                float pvy=(float)(Math.sqrt((((float)y/(HEIGHT/ts)))));
                int v=getTile(x+px,y+py,pvy);
                v=(int)(v/50)*50;
                //v=v>125?250:0;
                //v=(v>130)?0:250;
                //gfx.setColor(new Color((v/(255*255)),(v/255)%255,v%255));
                gfx.setColor(new Color(v,v,v));
                gfx.fillRect(ts*x,ts*y,ts,ts);
            }
        }

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

        //UPDATES


        repaint();//UPDATES FRAME
        try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP) {
            for (int i=0;i<vecs.length;i++) {
                vecs[i][1] += vecs[i][4];
                //vecs[i][3] += vecs[i][5];
            }
        }
        /*
        if (e.getKeyCode()==KeyEvent.VK_UP){
            py++;
        }if (e.getKeyCode()==KeyEvent.VK_DOWN){
            py--;
        }if (e.getKeyCode()==KeyEvent.VK_LEFT){
            px--;
        }if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            px++;
        }*/
        if (e.getKeyCode()==KeyEvent.VK_1){
            createVecs(1);
        }if (e.getKeyCode()==KeyEvent.VK_2){
            createVecs(2);
        }if (e.getKeyCode()==KeyEvent.VK_3){
            createVecs(3);
        }if (e.getKeyCode()==KeyEvent.VK_4){
            createVecs(4);
        }if (e.getKeyCode()==KeyEvent.VK_8){
            createVecs(8);
        }
    }

    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    int getTile(int x, int y,float py){
        float sum=0;
        for (float[] v:vecs){
            sum+=(float)(Math.sin(v[0]*(x-v[1])+v[2]*(y-v[3]))+1);
        }
        sum/=vecs.length;
        sum=(sum+py)/(2);

        //return (int)(sum/2f*255*255*255);
        return (int)(sum/2f*255);
    }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID
    public void paintCoordGrid(Graphics gfx){
        gfx.setColor(gridColor);
        for (int x=100; x<WIDTH; x+=100){
            gfx.drawString(""+x, x, 20);
            gfx.drawRect(x, 20, 1, HEIGHT);
        }
        for (int y=100; y<HEIGHT; y+=100){
            gfx.drawString(""+y, 20, y);
            gfx.drawRect(20, y, WIDTH, 1);
        }
    }
}