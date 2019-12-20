public class DateOfBirth {

    // fields
    private int year;
    private int month;
    private int day;

    public DateOfBirth(String dateOfBirth) {
        String[] temp = dateOfBirth.split("-");
        setYear(Integer.parseInt(temp[0]));
        setMonth(Integer.parseInt(temp[1]));
        setDay(Integer.parseInt(temp[2]));
    }

    public DateOfBirth(int year, int month, int day) {
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    public DateOfBirth(DateOfBirth other) {
        this(other.getYear(), other.getMonth(), other.getDay());
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (Validation.isYearLegal(year)) {
            this.year = year;
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (Validation.isMonthLegal(month)) {
            this.month = month;
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (Validation.isDayLegal(year, month, day)) {
            this.day = day;
        } else {
            System.out.println(day);
        }
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        result.append(getYear());
        result.append("-");

        if (getMonth() < 10) {
            result.append("0");
        }
        result.append(getMonth());
        result.append("-");

        if (getDay() < 10) {
            result.append("0");
        }
        result.append(getDay());
        return result.toString();
    }

}
