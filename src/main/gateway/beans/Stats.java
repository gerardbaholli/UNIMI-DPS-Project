package beans;

import java.util.ArrayList;
import java.util.List;


public class Stats {

    private List<Double> statsList;

    private static Stats instance;

    private Stats() {
        this.statsList = new ArrayList<>();
    }

    //singleton
    public synchronized static Stats getInstance() {
        if (instance == null)
            instance = new Stats();
        return instance;
    }

    public synchronized void addStats(double stats) {
        statsList.add(stats);
    }


    public synchronized List<Double> getLastNStats(int n) {

        // create a copy of the list to iterate on it
        List<Double> tempList = new ArrayList<>(statsList);

        // create a clean array to add the last N stats
        List<Double> response = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            response.add(tempList.get(i));
        }

        return response;
    }


    public synchronized List<Double> getStanDevAvg(int n) {

        // create a copy of the list to iterate on it
        List<Double> tempList = new ArrayList<>(statsList);

        // create a clean array to add the last N stats
        List<Double> lastNStats = new ArrayList<>();

        // create the response array to add the calculate
        List<Double> response = new ArrayList<>(2);

        for (int i = 0; i < n; i++) {
            lastNStats.add(tempList.get(i));
        }

        response.add(calculateSD(lastNStats));
        response.add(calculateAVG(lastNStats));

        return response;
    }

    public double calculateAVG(List<Double> list) {
        double sum = 0.0;
        int count = list.size();

        for (double value : list) {
            sum += value;
        }

        return sum / count;

    }

    public double calculateSD(List<Double> list) {
        double sum = 0.0;
        double standardDeviation = 0.0;

        int count = list.size();

        for (double value : list) {
            sum += value;
        }

        double mean = sum / count;

        for (double value : list) {
            standardDeviation += Math.pow(value - mean, 2);
        }

        return Math.sqrt(standardDeviation / count);
    }


}
