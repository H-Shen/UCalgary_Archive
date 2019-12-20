/**
 * The {@code DateOfBirth} class inherits class User and contains methods that gets the date of birth of the user.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class DateOfBirth {

    /**
     * @Fields properties of the object
     */
    private String year  = "";
    private String month = "";
    private String day   = "";

    /**
     * Constructor that splits the String date of birth into year, month and date.
     *
     * @param dateOfBirth a String containing the date of birth.
     * @date 2019/04/11
     */
    public DateOfBirth(String dateOfBirth) {
        String[] temp = dateOfBirth.split("-");
        try {
            setYear(temp[0]);
            setMonth(temp[1]);
            setDay(temp[2]);
        } catch (Exception ignored) {
        }
    }

    /**
     * Constructor that initializes the year, month and day of the birth date.
     *
     * @param year  a String containing the year.
     * @param month a String containing the month.
     * @param day   a String containing the day.
     * @date 2019/04/11
     */
    public DateOfBirth(String year, String month, String day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    /**
     * Copy constructor
     *
     * @param other another DateOfBirth object
     * @date 2019/04/11
     */
    public DateOfBirth(DateOfBirth other) {
        this(other.getYear(), other.getMonth(), other.getDay());
    }

    /**
     * Getter of the year of birth date.
     *
     * @return a String containing the year.
     * @date 2019/04/11
     */
    public String getYear() {
        return year;
    }

    /**
     * Setter of the year of birth date.
     * The year set only if it is valid.
     *
     * @param year a String containing the year.
     * @date 2019/04/11
     */
    public void setYear(String year) {
        if (Validation.isYearValid(year)) {
            this.year = year;
        }
    }

    /**
     * Getter of the month of birth date.
     *
     * @return a String containing the year.
     * @date 2019/04/11
     */
    public String getMonth() {
        return month;
    }

    /**
     * Setter of the month of birth date.
     * The month set only if it is valid.
     *
     * @param month a String containing the month.
     * @date 2019/04/11
     */
    public void setMonth(String month) {
        if (Validation.isMonthValid(month)) {
            this.month = month;
        }
    }

    /**
     * Getter of the day of birth date.
     *
     * @return a String containing the day.
     * @date 2019/04/11
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the day of birth date.
     * The day set only if it is valid.
     *
     * @param day a String containing the day.
     * @date 2019/04/11
     */
    public void setDay(String day) {
        if (Validation.isDayValid(year, month, day)) {
            this.day = day;
        }
    }

    /**
     * The method defines the result of printing a DateOfBirth object directly.
     *
     * @return a string represents the DateOfBirth object
     * @date 2019/04/08
     */
    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }
}
