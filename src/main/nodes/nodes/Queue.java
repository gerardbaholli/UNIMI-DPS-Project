package nodes;

import simulator.Buffer;
import simulator.Measurement;

import java.util.ArrayList;

public class Queue implements Buffer {

    public ArrayList<Measurement> buffer = new ArrayList<>();

    @Override
    public synchronized void addMeasurement(Measurement m) {
        buffer.add(m);
        notify();
        System.out.println(
                m.getId() + " " +
                        m.getType() + " " +
                        m.getValue() + " " +
                        m.getTimestamp());
    }

    public synchronized Measurement take() {
        Measurement m = null;

        while (buffer.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (buffer.size() > 0) {
            m = buffer.get(0);
            buffer.remove(0);
        }

        return m;
    }

    public synchronized double slidingWindow() throws InterruptedException {
        double avg = 0;
        int count = 0;

        while (buffer.size() < 12) {
            wait();
        }

        while (count < 12) {

            if (buffer.size() > 0) {
                avg += buffer.get(count).getValue();
                count++;
            }

        }

        avg = avg / 12;

        for (int i = 0; i < 6; i++) {
            buffer.remove(0);
        }

        return avg;
    }

    public synchronized Measurement read() {
        Measurement m = null;

        while (buffer.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (buffer.size() > 0) {
            m = buffer.get(0);
        }

        return m;
    }


}