public abstract class StreamingMedia {

    private String title;
    private int    length = 10;

    public StreamingMedia(String aTitle, int aLength) {
        setTitle(aTitle);
        setLength(aLength);
    }

    public StreamingMedia(StreamingMedia toCopy) {
        this(toCopy.getTitle(), toCopy.getLength());
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String aTitle) {
        title = aTitle.toUpperCase();
    }

    public int getLength() {
        return length;
    }

    protected void setLength(int aLength) {
        if (aLength > 0) {
            length = aLength;
        }
    }

    public abstract int getRating();

    public char getCategory() {
        switch (getRating()) {
            case 10:
            case 9:
                return 'A';
            case 8:
            case 7:
                return 'B';
            case 6:
            case 5:
                return 'C';
            case 4:
            case 3:
                return 'D';
            default:
                return 'F';
        }
    }

    @Override
    public String toString() {
        return "Title: " + getTitle() + " Length: " + getLength();
    }

}
