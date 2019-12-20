/**
 * The {@code DateOfBirth} class inherits class User and contains methods that gets the date of birth of the user.
 */
public class DateOfBirth {

    private String year  = "";
    private String month = "";
    private String day   = "";

    /**
     * Constructor that splits the String date of birth into year, month and date.
     *
     * @param dateOfBirth a String containing the date of birth.
     */
    public DateOfBirth(String dateOfBirth) {
        String[] temp = dateOfBirth.split("-");
        try {
            setYear(temp[0]);
            setMonth(temp[1]);
            setDay(temp[2]);
        } catch (Exception e) {
        }
    }

    /**
     * Constructor that initializes the year, month and day of the birth date.
     *
     * @param year  a String containing the year.
     * @param month a String containing the month.
     * @param day   a String containing the day.
     */
    public DateOfBirth(String year, String month, String day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    /**
     * @param other
     */
    public DateOfBirth(DateOfBirth other) {
        this(other.getYear(), other.getMonth(), other.getDay());
    }

    /**
     * Gets the year of birth date.
     *
     * @return a String containing the year.
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of birth date.
     * The year set only if it is valid.
     *
     * @param year a String containing the year.
     */
    public void setYear(String year) {
        if (Validation.isYearValid(year)) {
            this.year = year;
        }
    }

    /**
     * Gets the month of birth date.
     *
     * @return a String containing the year.
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month of birth date.
     * The month set only if it is valid.
     *
     * @param month a String containing the month.
     */
    public void setMonth(String month) {
        if (Validation.isMonthValid(month)) {
            this.month = month;
        }
    }

    /**
     * Gets the day of birth date.
     *
     * @return a String containing the day.
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the day of birth date.
     * The day set only if it is valid.
     *
     * @param day a String containing the day.
     */
    public void setDay(String day) {
        if (Validation.isDayValid(year, month, day)) {
            this.day = day;
        }
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }
}
