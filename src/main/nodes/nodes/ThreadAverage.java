package nodes;

public class ThreadAverage extends Thread {

    Queue q;

    public ThreadAverage(Queue q){
        this.q = q;
    }

    @Override
    public void run() {

        double avg = 0;

        while (true){

            try {
                avg = q.slidingWindow();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("The average is: " + avg);
        }

    }



}
