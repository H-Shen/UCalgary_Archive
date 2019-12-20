import java.math.BigDecimal;

public class Book {

    private String title;
    private int    numberOfWords = 0;
    private int    numPages      = 1;

    public Book(String title, int numberOfWords, int numberOfPages) {

        this.title = title;
        setNumberOfWords(numberOfWords);
        setNumPages(numberOfPages);
    }

    public Book(Book toCopy) {
        this(toCopy.getTitle(), toCopy.getNumberOfWords(), toCopy.getNumPages());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int wordsInBook) {
        if (wordsInBook >= 0 && wordsInBook <= 500000) {
            numberOfWords = wordsInBook;
        }
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numberOfPages) {
        if (numberOfPages > 0) {
            numPages = numberOfPages;
        } else {
            numPages = 1;
        }
    }

    public int minutesToConsume() {
        BigDecimal temp = new BigDecimal(getNumberOfWords() / getNumPages());
        if (temp.compareTo(new BigDecimal("20")) < 0) {
            return (int) (getNumPages() * 0.5);
        }
        if (temp.compareTo(new BigDecimal("20")) >= 0 && temp.compareTo(new BigDecimal("100")) < 0) {
            return getNumPages();
        }
        if (temp.compareTo(new BigDecimal("100")) >= 0 && temp.compareTo(new BigDecimal("250")) < 0) {
            return getNumPages() * 2;
        }
        return getNumPages() * 4;
    }

    public String difficulty() {
        int temp = minutesToConsume();
        if (temp < 30) {
            return "Easy";
        }
        if (temp < 120) {
            return "Moderate";
        }
        if (temp < 240) {
            return "Hard";
        }
        return "Extra Challenge";
    }

    @Override
    public String toString() {
        return "Title: " + getTitle() + " Difficulty: " + difficulty() + " Pages: " + getNumPages();
    }
}
