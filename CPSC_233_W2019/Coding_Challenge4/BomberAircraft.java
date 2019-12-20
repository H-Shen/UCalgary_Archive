public class BomberAircraft extends Aircraft {

    private int payload;

    public BomberAircraft(String name, String aOrigin, int aSpeed, int aPayload) {
        super(name, aOrigin, aSpeed);
        setPayload(aPayload);
    }

    public BomberAircraft(BomberAircraft toCopy) {
        super(toCopy);
        setPayload(toCopy.getPayload());
    }

    public int getPayload() {
        return payload;
    }

    public void setPayload(int aPayload) {
        payload = aPayload;
    }

    @Override
    public String getCategory() {
        int temp = getPayload();
        if (temp <= 1) {
            return "LIGHTER";
        }
        if (temp > 1 && temp <= 2) {
            return "MEDIUM";
        }
        return "HEAVIER";
    }


}
