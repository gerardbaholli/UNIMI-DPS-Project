package stats;

import stats.Stat;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    private List<Stat> statsList;

    public Stats() {
        this.statsList = new ArrayList<>();
    }


    public List<Stat> getStatsList() {
        return statsList;
    }

    public void setStatsList(List<Stat> statsList) {
        this.statsList = statsList;
    }

    public String printStatsList(){
        String response = "";
        for (Stat value : statsList) {
            response += value.getValue() + " " + value.getTimestamp() + "\n";
        }

        return response;
    }

}
