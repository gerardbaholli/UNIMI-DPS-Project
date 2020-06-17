package nodes;


import java.util.ArrayList;

public class LocalAvgList {

    private ArrayList<Double> localList;
    private int size;

    private static LocalAvgList instance;

    private LocalAvgList() {
        this.localList = new ArrayList<>();
    }

    // singleton
    public synchronized static LocalAvgList getInstance(){
        if(instance==null)
            instance = new LocalAvgList();
        return instance;
    }

    public synchronized void addValue(double value){
        this.localList.add(value);
        this.size++;
    }

    public synchronized double getLastValue(){
        double value = this.localList.remove(0);
        this.size--;
        return value;
    }


    public int getSize() {
        return size;
    }
}
