import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

import java.util.ArrayList;

public class Day {
    public ArrayList<ArrayList<String>> tasksl;
    ArrayList<Task> tasks;
    int index;
    float[] projs;
    public Day(int i){
        tasksl=new ArrayList<>();
        tasks=new ArrayList<>();
        index=i;
    }
    public void addTask(ArrayList<String> t){
        tasksl.add(t);
        tasks.add(new Task(t));
        if (tasks.get(tasks.size()-1).dur==-1||tasks.get(tasks.size()-1).start==-1){
            tasks.remove(tasks.size()-1);
            tasksl.remove(tasksl.size()-1);
        }
    }
    private float time2hours(String time){
        int hrs=Integer.parseInt(time.substring(0,2));
        int min=Integer.parseInt(time.substring(3,4));
        int sec=Integer.parseInt(time.substring(6,8));
        return hrs+(min/60f)+(sec/3600f);
    }
    public void print(){
        System.out.println("Day "+index+" has "+tasksl.size()+" tasksl recorded");
        for (Task t: tasks){
            System.out.println(t.toString());
        }
    }

    public Vec2f getDayTime(){
        Vec2f times=new Vec2f();
        for (Task t: tasks){
            if (t.start>4){
                times.y+=t.dur;
                if (times.x==0){
                    times.x=t.start;
                }
            }
        }
        return times;
    }

    public void setProjs(String[] ps) {
        projs=new float[ps.length];
        for (Task t : tasks) {
            for (int i=0; i<ps.length;i++){
                if (t.proj.contains(ps[i])){
                    projs[i]+=t.dur;
                }
            }
        }
    }

}
