package nodes;

import com.example.token.NodeServiceOuterClass.TokenData.Ready;
import com.example.token.NodeServiceOuterClass.TokenData.Waiting;

public class Token {

    private Ready readyList;
    private Waiting waiting;

    private Token() {}

    private static Token instance;

    // singleton
    public synchronized static Token getInstance(){
        if(instance==null)
            instance = new Token();
        return instance;
    }


    public Ready getReadyList() {
        return readyList;
    }

    public void setReadyList(Ready readyList) {
        this.readyList = readyList;
    }

    public Waiting getWaiting() {
        return waiting;
    }

    public void setWaiting(Waiting waiting) {
        this.waiting = waiting;
    }
}
