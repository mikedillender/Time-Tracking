import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=2000, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 150,150);

    ArrayList<Day> days;
    ArrayList<Color> cols;
    Color[] dayc={
            new Color(59, 103, 250),
            new Color(150, 23, 13),
            new Color(3, 156, 5),
            new Color(184, 59, 171),
            new Color(250, 116, 35),
            new Color(250, 246, 98),
            new Color(0,0,0),
    };

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        days=new ArrayList<>();
        cols=new ArrayList<>();
        importData();
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        //RENDER FOREGROUND
        int x=50;
        int w=7;
        int sep=0;
        for (int y=0;y<24;y++){
            int y1=(HEIGHT/24)*y;
            gfx.setColor(Color.GRAY);
            gfx.drawLine(0,y1,WIDTH,y1);
            gfx.setColor(Color.BLACK);
            gfx.drawString(y+"",10,y1);
        }
        int x1=x;
        for (int i=0;i<days.size();i++){
            x=x+w+sep;
            if (i%7==0){
                gfx.setColor(Color.GRAY);
                gfx.drawLine(x-(sep/2),0,x-(sep/2),HEIGHT);
                gfx.setColor(Color.BLACK);
                gfx.drawString(days.get(i).tasksl.get(0).get(7)+"",x,30);
            }
        }
        x=x1;
        int i=0;
        for (Day d:days){
            gfx.setColor(dayc[i%7]);
            x=x+w+sep;
            for (Task t:d.tasks){
                int y=(int)((HEIGHT/24f)*t.start);
                int h=(int)((HEIGHT/24f)*t.dur);
                gfx.fillRect(x,y,w,h);
            }
            i++;
        }

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void importData(){
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\togglyear.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                ArrayList<String> lv=new ArrayList<>();
                for (String s:values){
                    lv.add(s);
                }
                records.add(lv);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int x=0; x<records.size(); x++){
            System.out.println("");
            for (int y=0; y<records.get(x).size();y++){
                System.out.print(records.get(x).get(y)+" | ");
            }
        }

        Day cd=new Day(days.size());
        String cds=records.get(1).get(7);
        int i=0;
        for (ArrayList<String> t:records){
            i++;
            if (i==1){continue;}
            if (t.get(7).equals(cds)){
                cd.addTask(t);
            }else {
                days.add(cd);
                cd=new Day(days.size());
                cd.addTask(t);
                cds=t.get(7);
            }
        }
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
    public void keyPressed(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { exportImg(); }
    public void keyTyped(KeyEvent e) { }

    public void exportImg(){
        //String export="B:\\Libraries\\Programming\\Calender\\Calendar-Generator\\calendarImgs\\t.png";
        //String export="C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\t.png";
        String export="C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\t1.png";
        //String export="C:\\Users\\dillemic000\\Documents\\GitHub\\Calendar-Generator\\t.png";

        RenderedImage rendImage = toBufferedImage(img);
        File file = new File(export);
        try {
            ImageIO.write(rendImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    public BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) { return (BufferedImage) img; }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

}