public class FighterAircraft extends Aircraft {

    private char bvrTechnology;

    public FighterAircraft(String name, String aOrigin, int aSpeed, char aBVRTechnology) {
        super(name, aOrigin, aSpeed);
        setBVRTechnology(aBVRTechnology);
    }

    public FighterAircraft(FighterAircraft toCopy) {
        super(toCopy);
        setBVRTechnology(toCopy.getBVRTechnology());
    }

    public char getBVRTechnology() {
        return bvrTechnology;
    }

    public void setBVRTechnology(char abvrTechnology) {
        if (abvrTechnology == 'Y' || abvrTechnology == 'N') {
            bvrTechnology = abvrTechnology;
        }
    }

    @Override
    public String getCategory() {
        if (getBVRTechnology() == 'Y') {
            return "EXCELLENT";
        }
        return "NORMAL";
    }
}
