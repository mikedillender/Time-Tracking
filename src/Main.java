import com.sun.javafx.geom.Vec2f;

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
    private final int WIDTH=2000, HEIGHT=1200;

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

    Color[] projcols;
    String[] prs;
    float[] times;


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
        gfx.setFont(gfx.getFont().deriveFont(20f));
        boolean stack=true;

        int x=50;
        int w=6;
        int sep=3;
        int endx=x+((w+sep)*(days.size()+1));
        for (int y=0;y<24;y++){
            int y1=(HEIGHT/24)*y;
            gfx.setColor(Color.GRAY);
            gfx.drawLine(0,y1,endx,y1);
            gfx.setColor(Color.BLACK);
            if(!stack) {
                gfx.drawString(y % 12 + "", 10, y1 - 30);
            }else {
                if (y%2==1){continue;}
                gfx.drawString(12-y/2 + "", 10, y1 - 30);
            }
        }
        int x1=x;
        gfx.setFont(gfx.getFont().deriveFont(12f));
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
        gfx.setFont(gfx.getFont().deriveFont(20f));
        for (Day d:days){
            //gfx.setColor(dayc[d1%7]);
            x=x+w+sep;
            if (!stack) {
                for (Task t : d.tasks) {
                    int y = (int) ((HEIGHT / 24f) * t.start);
                    int h = (int) ((HEIGHT / 24f) * t.dur);
                    gfx.setColor(getColor(t.proj));
                    gfx.fillRect(x, y, w, h);
                }
            }else {
                int y1=(HEIGHT/24);
                int i=0;
                for (float t : d.projs){
                    int h = (int) ((HEIGHT / 12f) * t);
                    gfx.setColor(projcols[i]);
                    gfx.fillRect(x, (HEIGHT/24)*24-y1-h, w, h);
                    y1=y1+h;
                    i++;
                }
            }
        }
        int y=50;
        gfx.setFont(gfx.getFont().deriveFont(18f));
        for (int i=0;i<projcols.length;i++){
            gfx.setColor(projcols[i]);
            gfx.drawString(prs[i]+" : "+((int)(times[i]*10))/10.0+" Hrs",WIDTH-500,y+(30*i));
        }
        gfx.setFont(gfx.getFont().deriveFont(20f));
        //drawTimePlot(gfx,WIDTH-500,500);

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void drawTimePlot(Graphics g,int x, int y){
        int wid=400;
        int hei=400;
        ArrayList<Vec2f> points=new ArrayList<>();
        for (Day d:days){
            points.add(d.getDayTime());
        }
        gfx.setColor(Color.gray);
        gfx.setFont(gfx.getFont().deriveFont(14f));
        for (int i=0; i<12; i++){
            gfx.drawLine(x,y+(i*hei/12),x+wid,y+(i*hei/12));
            gfx.drawLine(x+(i*wid/12),y,x+(i*wid/12),y+hei);
            gfx.drawString(""+(i*2),(x+(i*wid/12)),y+hei+15);
        }
        gfx.setColor(Color.BLACK);
        g.drawRect(x,y,wid,hei);
        gfx.setFont(gfx.getFont().deriveFont(16f));

        g.drawString("Time Started Working - >",x+wid/5,y+hei+30);
        g.drawString("Work Done - ^",x-100,y+hei/2);
        gfx.setColor(Color.blue);
        for (Vec2f v: points){
            g.fillOval(x+(int)(v.x*wid/24f)-4,y+hei-(int)(v.y*hei/12f)-4,8,8);
        }
    }

    public void importData(){
        ArrayList<ArrayList<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\toggl16.csv"))) {
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
            if (i==1||t.size()<7){continue;}
            if (t.get(7).equals(cds)){
                cd.addTask(t);
            }else {
                days.add(cd);
                cd=new Day(days.size());
                cd.addTask(t);
                cds=t.get(7);
            }
        }
        days.add(cd);
        ArrayList<String> projs=new ArrayList<>();
        boolean first=true;
        for (ArrayList<String> t:records){
            if (first){first=false;continue;}
            String proj=t.get(3);
            boolean repeat=false;
            for (String s:projs){
                if (s.equals(proj)){
                    repeat=true;
                }
            }
            if (!repeat){
                projs.add(proj);
            }
        }
        projcols=new Color[projs.size()];
        prs=new String[projs.size()];
        for (int p=0; p<projcols.length; p++){
            prs[p]=projs.get(p);
            float h=(float) ((projcols.length-1-p)*(360f/(projcols.length-1)));
            float s=(float)(65+35*((Math.random()<.5)?-1:1)*Math.pow(Math.random(),1.5));;
            float l=(float)(45+25*((Math.random()<.5)?-1:1)*Math.pow(Math.random(),1.5));
            System.out.println(h+", "+s+","+l);
            HSLColor col=new HSLColor(h,s,l,1);
            //HSLColor col=new HSLColor(h,100,50,1);
            projcols[p]=col.getRGB();
        }
        resetcols();
        for (Day d: days) {
            d.setProjs(prs);
        }
        times=new float[projs.size()];
        for (Day d:days){
            i=0;
            for (float t : d.projs){
                times[i]+=t;
                i++;
            }
        }
    }

    public int bound(int i,int max){
        if(i<0){return 0;}
        return (i<max)?i:max;
    }

    public void resetcols(){
        int[] relay=new int[projcols.length];
        for (int i=0;i<relay.length;i++){relay[i]=i;}
        relay[3]=8;relay[8]=3;
        int col=1,rows=1;
        int[] dhues={0,25,35,50,70,110,150,170,185,200,230,267,280,290,300,320};//distinct hues
        System.out.println(relay.length+" colors, dhues = "+dhues.length);
        for (int p=0; p<projcols.length; p++){
            float h=(float) (dhues[relay[p]%16]);
            //float s=(float)(70+30*((Math.random()<.5)?-1:1)*Math.pow(Math.random(),1.5));;
            float s=(float)(100-50*(Math.pow(Math.random(),2)))-(relay[p]>=16?(20*(float)Math.random()):0);
            float l=(float)(50+25*((Math.random()<.5)?-.4:1)*Math.pow(Math.random(),1.3));
            System.out.println(h+", "+s+","+l);
            HSLColor colr=new HSLColor(h,s,l,1);
            //HSLColor col=new HSLColor(h,100,50,1);
            projcols[p]=colr.getRGB();
        }
        projcols[5]=new Color(26, 30, 48);
    }

    public Color getColor(String p){
        int i=0;
        for (String s: prs){ if (p.equals(s)){break;}i++; }
        return projcols[i];
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
        if (e.getKeyCode()==KeyEvent.VK_P){
            exportImg();
        }
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            resetcols();
        }
    }
    public void keyReleased(KeyEvent e) {  }
    public void keyTyped(KeyEvent e) { }

    public void exportImg(){
        //String export="C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\t.png";
        //String export="C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\tall.png";
        String export="C:\\Users\\Mike\\Documents\\GitHub\\Time-Tracking\\src\\t14.png";

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