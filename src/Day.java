import java.util.ArrayList;

public class Day {
    public ArrayList<ArrayList<String>> tasksl;
    ArrayList<Task> tasks;
    int index;
    public Day(int i){
        tasksl=new ArrayList<>();
        tasks=new ArrayList<>();
        index=i;
    }
    public void addTask(ArrayList<String> t){
        tasksl.add(t);
        tasks.add(new Task(t));
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

}
