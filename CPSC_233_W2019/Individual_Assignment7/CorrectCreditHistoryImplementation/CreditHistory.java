public class CreditHistory {
    private Queue<Integer> ratings = new LinkedList<Integer>();

    public void addRating(int rating) {
        if (rating > -6 && rating < 6) {
            ratings.add(rating);
        }
    }

    public ArrayList<Integer> getRatings() {
        ArrayList<Integer> asList = new ArrayList<Integer>();
        asList.addAll(ratings);
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
