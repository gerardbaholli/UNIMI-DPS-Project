package beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Stats {

    @XmlElement(name="stats")
    private List<Stat> statsList;

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

    public synchronized List<Stat> getStatsList() {
        return new ArrayList<>(statsList);
    }


    public synchronized String addStats(Stat s) {
        statsList.add(s);
        return "Success";
    }


    public List<Stat> getLastNStats(int n) {

        // create a copy of the list to iterate on it
        List<Stat> tempList = new ArrayList<>(getStatsList());

        // create a clean array to add the last N stats
        List<Stat> response = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            response.add(tempList.get((tempList.size()-1)-i));
        }

        return response;
    }


    public List<Double> getStanDevAvg(int n) {

        // create a copy of the list to iterate on it
        List<Stat> tempList = new ArrayList<>(getStatsList());

        // create a clean array to add the last N stats
        List<Stat> lastNStats = new ArrayList<>();

        // create the response array to add the calculate
        List<Double> response = new ArrayList<>(2);

        for (int i = 0; i < n; i++) {
            lastNStats.add(tempList.get((tempList.size()-1)-i));
        }

        response.add(calculateSD(lastNStats));
        response.add(calculateAVG(lastNStats));

        return response;
    }

    public double calculateAVG(List<Stat> list) {
        double sum = 0.0;
        int count = list.size();

        for (Stat value : list) {
            sum += value.getValue();
        }

        return sum / count;

    }

    public double calculateSD(List<Stat> list) {
        double sum = 0.0;
        double standardDeviation = 0.0;

        int count = list.size();

        for (Stat value : list) {
            sum += value.getValue();
        }

        double mean = sum / count;

        for (Stat value : list) {
            standardDeviation += Math.pow(value.getValue() - mean, 2);
        }

        return Math.sqrt(standardDeviation / count);
    }

    public void setStatsList(List<Stat> list){
        this.statsList = list;
    }

    public void printStatsList(){
        System.out.println("Lista statistiche: ");

        for (Stat value : statsList) {
            System.out.println(value.getValue() + " " + value.getTimestamp());
        }
    }


}
