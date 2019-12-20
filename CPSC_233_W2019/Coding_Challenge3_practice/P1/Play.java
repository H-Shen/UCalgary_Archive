import java.util.Calendar;
import java.util.Date;

public class Play extends Theatre {

    private String writer;
    private int    yearWritten;

    public Play(String aTitle, int aRating, String aWriter, int yearWritten) {
        super(aTitle, aRating);
        setWriter(aWriter);
        setYearWritten(yearWritten);
    }

    public Play(Play toCopy) {
        this(toCopy.getTitle(), toCopy.getRating(), toCopy.getWriter(), toCopy.getYearWritten());
    }

    public int getYearWritten() {
        return yearWritten;
    }

    public void setYearWritten(int year) {
        if (year < 2019) {
            yearWritten = year;
        } else {
            yearWritten = 2018;
        }
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String aWriter) {
        writer = aWriter;
    }

    @Override
    public String getCategory() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentYear = calendar.get(Calendar.YEAR);
        int gap         = currentYear - getYearWritten();
        if (gap > 200) {
            return "Classic";
        }
        if (gap > 50) {
            return "Contemporary";
        }
        return "Modern";
    }
}
