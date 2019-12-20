public class CreditHistory {
    private Queue<Integer> ratings = new LinkedList<Integer>();

    public void addRating(int rating) {
        if (rating > -5 && rating < 5) {
            ratings.add(rating);
        }
    }

    public ArrayList<Integer> getRatings() {
        ArrayList<Integer> asList = new ArrayList<Integer>();
        return asList;
    }

    public void trimRatings() {
        while (ratings.size() > 10) {
            ratings.poll();
        }
    }

    public int numOfRatings() {
        return ratings.size();
    }
}
