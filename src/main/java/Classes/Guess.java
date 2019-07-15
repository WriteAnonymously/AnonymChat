package Classes;

public class Guess {

    private String word;
    private long chatId;

    public Guess(String word, long chatId){
        this.word = word;
        this.chatId = chatId;
    }

    public String getWord() {
        return word;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
