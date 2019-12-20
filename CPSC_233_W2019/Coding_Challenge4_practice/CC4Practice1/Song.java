public class Song extends StreamingMedia {

    private int numOfLikes;

    public Song(String aTitle, int aLength) {
        super(aTitle, aLength);
    }

    public Song(Song toCopy) {
        super(toCopy);
        addLikes(toCopy.getNumOfLikes());
    }

    public void addLikes(int amount) {
        if (amount > 0) {
            numOfLikes += amount;
        }
    }

    public int getNumOfLikes() {
        return numOfLikes;
    }

    @Override
    public int getRating() {
        int temp = getNumOfLikes();
        if (temp >= 5000) {
            return 9;
        }
        if (temp >= 500) {
            return 7;
        }
        if (temp >= 50) {
            return 5;
        }
        if (temp >= 10) {
            return 3;
        }
        return 1;
    }

    @Override
    public String toString() {
        return super.toString() + " Likes: " + getNumOfLikes();
    }
}
