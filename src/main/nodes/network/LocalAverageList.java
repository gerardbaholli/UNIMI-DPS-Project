package network;

import java.util.ArrayList;

public class LocalAverageList {

    private ArrayList<Double> localList;
    private int size;

    private static LocalAverageList instance;

    private LocalAverageList() {
        this.localList = new ArrayList<>();
    }

    // singleton
    public synchronized static LocalAverageList getInstance(){
        if(instance==null)
            instance = new LocalAverageList();
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
