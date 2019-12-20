public class AudioBook extends Book {

    private int    sizeInKB;
    private double playbackRatio = 1.0;

    public AudioBook(String title, int numOfWords, int sizeInKB) {
        super(title, numOfWords, 1);
        setSizeInKB(sizeInKB);
    }

    public AudioBook(AudioBook toCopy) {

        super(toCopy);
        setSizeInKB(toCopy.getSizeInKB());
        setPlaybackRatio(toCopy.getPlaybackRatio());
    }

    public int getSizeInKB() {
        return sizeInKB;
    }

    public void setSizeInKB(int sizeInKB) {
        if (sizeInKB >= 0) {
            this.sizeInKB = sizeInKB;
        }
    }

    public double getPlaybackRatio() {
        return playbackRatio;
    }

    public void setPlaybackRatio(double playbackRatio) {
        if (playbackRatio > 0) {
            this.playbackRatio = playbackRatio;
        }
    }

    @Override
    public int minutesToConsume() {
        return (int) (getSizeInKB() / 1920.0 / getPlaybackRatio());
    }
}
