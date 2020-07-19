package network;


public class LocalAverage extends Thread {

    Queue queue;

    public LocalAverage(Queue q){
        this.queue = q;
    }

    @Override
    public void run() {

        double avg = 0;

        while (true){

            try {
                avg = queue.slidingWindow();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (LocalAverageList.getInstance()){
                LocalAverageList.getInstance().addValue(avg);
                LocalAverageList.getInstance().notify();
                System.out.println("The average is: " + avg);
            }

        }

    }



}
