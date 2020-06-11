package beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Words {

    @XmlElement(name="word")
    private List<Word> wordList;

    private static Words instance;

    private Words(){
        this.wordList = new ArrayList<Word>();
    }

    //singleton
    public synchronized static Words getInstance(){
        if (instance==null){
            instance = new Words();
        }
        return instance;
    }


    public synchronized List<Word> getWordList() {
        return new ArrayList<>(wordList);
    }

    public synchronized void setWordList(List<Word> wordList) {
        this.wordList = wordList;
    }

    public synchronized void add(Word word){
        this.wordList.add(word);
    }

    public String getDefinition(String word){
        List<Word> wordsCopy = getWordList();

        for (Word w : wordsCopy)
            if (w.getWord().toLowerCase().equals(word.toLowerCase()))
                return w.getDefinition();
        return null;
    }

    public synchronized String changeDefinition(Word word){
        for (Word w : wordList) {
            if (w.getWord().toLowerCase().equals(word.getWord().toLowerCase())){
                w.setDefinition(word.getDefinition());
                return "Success";
            }
        }
        return "notFound";
    }

    public synchronized String deleteWord(Word word){
        for (Word w : wordList) {
            if (w.getWord().toLowerCase().equals(word.getWord().toLowerCase()))
                this.wordList.remove(w);
                return "Success";
        }
        return "notFound";
    }


}
