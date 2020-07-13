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

            synchronized (LocalAvgList.getInstance()){
                LocalAvgList.getInstance().addValue(avg);
                LocalAvgList.getInstance().notifyAll();
                System.out.println("The average is: " + avg);
            }

        }

    }



}
