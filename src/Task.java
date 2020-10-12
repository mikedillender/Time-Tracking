import java.util.ArrayList;

public class Task {
    float start,dur;
    String task, proj;
    private float time2hours(String time){
        int hrs=Integer.parseInt(time.substring(0,2));
        int min=Integer.parseInt(time.substring(3,5));
        int sec=Integer.parseInt(time.substring(6,8));
        return hrs+(min/60f)+(sec/3600f);
    }
    public Task(ArrayList<String> ts){
        proj=ts.get(3);
        task=ts.get(5);
        start=time2hours(ts.get(8));
        dur=time2hours(ts.get(11));
        System.out.println("Duration = "+ts.get(11)+" = "+dur);
    }
    public String toString(){
        return task+" ( "+proj+" ) Start : "+start+" | Lasted : "+dur;
    }

}
