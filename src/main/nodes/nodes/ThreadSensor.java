package nodes;


public class ThreadSensor extends Thread {

    public ThreadSensor(){

    }

    public void run() {

        while (true) {
            System.out.println("ThreadSensor");
        }

    }


}
