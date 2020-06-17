package nodes;

public class ThreadAverage extends Thread {

    Queue queue;


    public ThreadAverage(Queue q){
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

            LocalAvgList.getInstance().addValue(avg);
            System.out.println("The average is: " + avg);
        }

    }



}
