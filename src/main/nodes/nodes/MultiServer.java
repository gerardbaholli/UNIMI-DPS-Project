package nodes;

import simulator.Measurement;
import simulator.PM10Simulator;

import java.io.IOException;

public class MultiServer {

    public static void main(String[] args) throws IOException {

        Queue buffer = new Queue();
        double avg = 0;

        // TODO: send POST for insert node in the system

        System.out.println("Node running");


        // create thread simulator
        PM10Simulator pm10Simulator = new PM10Simulator(buffer);
        // start thread simulator
        pm10Simulator.start();


        ThreadAverage threadAverage = new ThreadAverage(buffer);
        threadAverage.start();

        //avg = buffer.computeAvg();
        //System.out.println("The average of first 12 is: " + avg);


        // TODO: stop node
        // should call this method and should be fine like this
        System.out.println("Hit return to stop...");
        System.in.read();
        pm10Simulator.stopMeGently();
        System.out.println("Node stopped");


        // print and remove last measurement
        Measurement lastM = buffer.read();
        System.out.println("Last measurement:\n" +
                lastM.getId() + " " +
                lastM.getType() + " " +
                lastM.getValue() + " " +
                lastM.getTimestamp());


    }


}
