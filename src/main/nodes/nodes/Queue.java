package nodes;

import simulator.Buffer;
import simulator.Measurement;

import java.util.ArrayList;

public class Queue implements Buffer {

    public ArrayList<Measurement> buffer = new ArrayList<>();

    @Override
    public void addMeasurement(Measurement m) {
        buffer.add(m);
        System.out.println(
                m.getId() + " " +
                        m.getType() + " " +
                        m.getValue() + " " +
                        m.getTimestamp());
    }

    public synchronized Measurement take() {
        Measurement m = null;

        while(buffer.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(buffer.size()>0){
            m = buffer.get(0);
            buffer.remove(0);
        }

        return m;
    }

}