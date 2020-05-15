package StereoControl;

public class Zone {
    private boolean power;
    private int vol;
    private int bass;
    private int treb;
    private int bal;
    private boolean mute;

    public Zone(String input) {
        if(Integer.parseInt(input.substring(6, 7)) == 1) { this.power = true; }
        else { this.power = false; }

        this.vol = Integer.parseInt(input.substring(11, 13));
        this.bass = Integer.parseInt(input.substring(15, 17));
        this.treb = Integer.parseInt(input.substring(13, 15));
        this.bal = Integer.parseInt(input.substring(17, 19));

        if(Integer.parseInt(input.substring(8, 9)) == 1) { this.mute = true; }
        else { this.mute = false; }
    }

    public Zone() {
        this.power = true;
        this.vol = 30;
        this.bass = 5;
        this.treb = 5;
        this.bal = 10;
        this.mute = false;
    }

    public void setPower(boolean pwr) { this.power = pwr; }
    public boolean getPower() { return this.power; }

    public void setVol(int vol) { this.vol = vol; }
    public int getVol() { return this.vol; }

    public void setBass (int bs) { this.bass = bs;}
    public int getBass() { return this.bass; }

    public void setTreb(int tr) { this.treb = tr;}
    public int getTreb() { return this.treb; }

    public void setBal(int bl) { this.bal = bl;}
    public int getBal() { return this.bal; }

    public void setMute(boolean mt) { this.mute = mt; }
    public boolean getMute() { return this.mute; }
}
