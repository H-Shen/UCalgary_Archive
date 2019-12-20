public abstract class Aircraft {

    private String name;
    private String origin;
    private int    speed;

    public Aircraft(String aName, String aOrigin, int aSpeed) {
        setName(aName);
        setOrigin(aOrigin);
        setSpeed(aSpeed);
    }

    public Aircraft(Aircraft toCopy) {
        this(toCopy.getName(), toCopy.getOrigin(), toCopy.getSpeed());
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName.toLowerCase();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String aOrigin) {
        origin = aOrigin.toUpperCase();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int aSpeed) {
        if (aSpeed >= 0) {
            speed = aSpeed;
        }
    }

    protected abstract String getCategory();

    public String getAircraftCategoryBySpeed() {
        String result = getCategory() + " with ";
        int    temp   = getSpeed();
        if (temp <= 550) {
            result += "A";
        } else if (temp >= 551 && temp < 751) {
            result += "B";
        } else if (temp >= 751) {
            result += "C";
        }
        return result;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + " Category: " + getAircraftCategoryBySpeed();
    }
}
