package StereoControl;
import com.fazecast.jSerialComm.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    private SerialPort comm;
    private Zone[] zones = new Zone[6];

    // BEGIN FXML VARIABLES
    @FXML private ToggleButton z1power;
    @FXML private ToggleButton z2power;
    @FXML private ToggleButton z3power;
    @FXML private ToggleButton z4power;
    @FXML private ToggleButton z5power;
    @FXML private ToggleButton z6power;
    @FXML private ToggleButton[] zonePower;

    @FXML private Slider z1vol;
    @FXML private Slider z2vol;
    @FXML private Slider z3vol;
    @FXML private Slider z4vol;
    @FXML private Slider z5vol;
    @FXML private Slider z6vol;
    @FXML private Slider[] zoneVolume;

    @FXML private Slider z1bass;
    @FXML private Slider z2bass;
    @FXML private Slider z3bass;
    @FXML private Slider z4bass;
    @FXML private Slider z5bass;
    @FXML private Slider z6bass;
    @FXML private Slider[] zoneBass;

    @FXML private Slider z1treb;
    @FXML private Slider z2treb;
    @FXML private Slider z3treb;
    @FXML private Slider z4treb;
    @FXML private Slider z5treb;
    @FXML private Slider z6treb;
    @FXML private Slider[] zoneTreb;

    @FXML private Slider z1bal;
    @FXML private Slider z2bal;
    @FXML private Slider z3bal;
    @FXML private Slider z4bal;
    @FXML private Slider z5bal;
    @FXML private Slider z6bal;
    @FXML private Slider[] zoneBalance;

    @FXML private CheckBox z1mute;
    @FXML private CheckBox z2mute;
    @FXML private CheckBox z3mute;
    @FXML private CheckBox z4mute;
    @FXML private CheckBox z5mute;
    @FXML private CheckBox z6mute;
    @FXML private CheckBox[] zoneMute;

    @FXML private Label z1vollbl;
    @FXML private Label z2vollbl;
    @FXML private Label z3vollbl;
    @FXML private Label z4vollbl;
    @FXML private Label z5vollbl;
    @FXML private Label z6vollbl;
    @FXML private Label[] zoneVolumeLabel;

    @FXML private Label z1basslbl;
    @FXML private Label z2basslbl;
    @FXML private Label z3basslbl;
    @FXML private Label z4basslbl;
    @FXML private Label z5basslbl;
    @FXML private Label z6basslbl;
    @FXML private Label[] zoneBassLabel;

    @FXML private Label z1treblbl;
    @FXML private Label z2treblbl;
    @FXML private Label z3treblbl;
    @FXML private Label z4treblbl;
    @FXML private Label z5treblbl;
    @FXML private Label z6treblbl;
    @FXML private Label[] zoneTrebLabel;

    @FXML private Label z1ballbl;
    @FXML private Label z2ballbl;
    @FXML private Label z3ballbl;
    @FXML private Label z4ballbl;
    @FXML private Label z5ballbl;
    @FXML private Label z6ballbl;
    @FXML private Label[] zoneBalanceLabel;

    @FXML private IntegerProperty z1volint;
    @FXML private IntegerProperty z2volint;
    @FXML private IntegerProperty z3volint;
    @FXML private IntegerProperty z4volint;
    @FXML private IntegerProperty z5volint;
    @FXML private IntegerProperty z6volint;
    @FXML private IntegerProperty[] zoneVolumeInt;

    @FXML private IntegerProperty z1bassint;
    @FXML private IntegerProperty z2bassint;
    @FXML private IntegerProperty z3bassint;
    @FXML private IntegerProperty z4bassint;
    @FXML private IntegerProperty z5bassint;
    @FXML private IntegerProperty z6bassint;
    @FXML private IntegerProperty[] zoneBassInt;

    @FXML private IntegerProperty z1trebint;
    @FXML private IntegerProperty z2trebint;
    @FXML private IntegerProperty z3trebint;
    @FXML private IntegerProperty z4trebint;
    @FXML private IntegerProperty z5trebint;
    @FXML private IntegerProperty z6trebint;
    @FXML private IntegerProperty[] zoneTrebInt;

    @FXML private IntegerProperty z1balint;
    @FXML private IntegerProperty z2balint;
    @FXML private IntegerProperty z3balint;
    @FXML private IntegerProperty z4balint;
    @FXML private IntegerProperty z5balint;
    @FXML private IntegerProperty z6balint;
    @FXML private IntegerProperty[] zoneBalanceInt;

    // END FXML VARIABLES

    @FXML public void initZones() {
        for (int z = 0; z < 6; z++) {
            // send command ("?1" + (z+1) + "\r");
            // >xxaabbccddeeffgghhiijj'CR'
            // xx - zone
            // aa - PA control status
            // bb - power control status
            // cc - mute control status
            // dd - DT control status
            // ee - volume control status
            // ff - treble control status
            // gg - bass control status
            // hh - balance control status
            // ii - source control status
            // jj - keypad connecting status
            zones[z] = new Zone(">1" + (z+1) + "aa0100dd10050510iijj\r"); // temp example string of zone info

            // initialize sliders/checkbox and set values
            checkPowerState(z);
            zoneVolume[z].setValue(zones[z].getVol());
            zoneBass[z].setValue(zones[z].getBass());
            zoneTreb[z].setValue(zones[z].getTreb());
            zoneBalance[z].setValue(zones[z].getBal());
            if(zones[z].getMute()) { zoneMute[z].setSelected(true); }
            zoneVolumeLabel[z].textProperty().bind(zoneVolumeInt[z].asString());
            zoneBassLabel[z].textProperty().bind(zoneBassInt[z].asString());
            zoneTrebLabel[z].textProperty().bind(zoneTrebInt[z].asString());
            zoneBalanceLabel[z].textProperty().bind(zoneBalanceInt[z].asString());
            //System.out.println("Zone " + (z+1) + " initialized.");
        }
    }

    @FXML void powerToggle(int z) {
        zones[z].setPower(!zones[z].getPower());

        checkPowerState(z);
        System.out.println("Zone " + (z+1) + " power toggled.");
    }

    @FXML void checkPowerState(int z) {
        // enable sliders/checkbox
        if (zones[z].getPower()) {
            zonePower[z].setSelected(true);
            zoneVolume[z].setDisable(false);
            zoneBass[z].setDisable(false);
            zoneTreb[z].setDisable(false);
            zoneBalance[z].setDisable(false);
            zoneMute[z].setDisable(false);
            zoneVolumeLabel[z].setDisable(false);
            zoneBassLabel[z].setDisable(false);
            zoneTrebLabel[z].setDisable(false);
            zoneBalanceLabel[z].setDisable(false);
        }
        // disable sliders/checkbox
        else {
            zonePower[z].setSelected(false);
            zoneVolume[z].setDisable(true);
            zoneBass[z].setDisable(true);
            zoneTreb[z].setDisable(true);
            zoneBalance[z].setDisable(true);
            zoneMute[z].setDisable(true);
            zoneVolumeLabel[z].setDisable(true);
            zoneBassLabel[z].setDisable(true);
            zoneTrebLabel[z].setDisable(true);
            zoneBalanceLabel[z].setDisable(true);
        }
    }

    @FXML public void setVolume(int z) {
        zones[z].setVol(zoneVolumeInt[z].get());
        if (zoneVolumeInt[z].get() < 10) {
            // send command ("<1" + (z+1) + "VO0" + vol + "\r")
        }
        else {
            // send command ("<1" + (z+1) + "VO" + vol + "\r")
        }
        System.out.println("Zone " + (z+1) + " volume set to " + zoneVolumeInt[z].get());
    }

    @FXML public void setBass(int z) {
        zones[z].setBass(zoneBassInt[z].get());
        if (zoneBassInt[z].get() < 10) {
            // send command ("<1" + (z+1) + "BS0" + vol + "\r")
        }
        else {
            // send command ("<1" + (z+1) + "BS" + vol + "\r")
        }
        System.out.println("Zone " + (z+1) + " bass set to " + zoneBassInt[z].get());
    }

    @FXML public void setTreble(int z) {
        zones[z].setTreb(zoneTrebInt[z].get());
        if (zoneTrebInt[z].get() < 10) {
            // send command ("<1" + (z+1) + "TR0" + vol + "\r")
        }
        else {
            // send command ("<1" + (z+1) + "TR" + vol + "\r")
        }
        System.out.println("Zone " + (z+1) + " treble set to " + zoneTrebInt[z].get());
    }

    @FXML public void setBalance(int z) {
        zones[z].setBal(zoneBalanceInt[z].get());
        if (zoneBalanceInt[z].get() < 10) {
            // send command ("<1" + (z+1) + "BL0" + vol + "\r")
        }
        else {
            // send command ("<1" + (z+1) + "BL" + vol + "\r")
        }
        System.out.println("Zone " + (z+1) + " balance set to " + zoneBalanceInt[z].get());
    }

    @FXML public void setMute(int z) {
        zones[z].setMute(!zones[z].getMute());
        if (zones[z].getMute()) {
            // send command ("<1" + (z+1) + "MU01\r")
        }
        else {
            // send command ("<1" + (z+1) + "MU00\r")
        }
        System.out.println("Zone " + (z+1) + " mute toggled.");
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        // initialize arrays of buttons/sliders/checkboxes/labels
        zonePower = new ToggleButton[6];
        zonePower[0] = z1power;
        zonePower[1] = z2power;
        zonePower[2] = z3power;
        zonePower[3] = z4power;
        zonePower[4] = z5power;
        zonePower[5] = z6power;

        zoneVolume = new Slider[6];
        zoneVolume[0] = z1vol;
        zoneVolume[1] = z2vol;
        zoneVolume[2] = z3vol;
        zoneVolume[3] = z4vol;
        zoneVolume[4] = z5vol;
        zoneVolume[5] = z6vol;

        zoneBass = new Slider[6];
        zoneBass[0] = z1bass;
        zoneBass[1] = z2bass;
        zoneBass[2] = z3bass;
        zoneBass[3] = z4bass;
        zoneBass[4] = z5bass;
        zoneBass[5] = z6bass;

        zoneTreb = new Slider[6];
        zoneTreb[0] = z1treb;
        zoneTreb[1] = z2treb;
        zoneTreb[2] = z3treb;
        zoneTreb[3] = z4treb;
        zoneTreb[4] = z5treb;
        zoneTreb[5] = z6treb;

        zoneBalance = new Slider[6];
        zoneBalance[0] = z1bal;
        zoneBalance[1] = z2bal;
        zoneBalance[2] = z3bal;
        zoneBalance[3] = z4bal;
        zoneBalance[4] = z5bal;
        zoneBalance[5] = z6bal;

        zoneMute = new CheckBox[6];
        zoneMute[0] = z1mute;
        zoneMute[1] = z2mute;
        zoneMute[2] = z3mute;
        zoneMute[3] = z4mute;
        zoneMute[4] = z5mute;
        zoneMute[5] = z6mute;

        zoneVolumeLabel = new Label[6];
        zoneVolumeLabel[0] = z1vollbl;
        zoneVolumeLabel[1] = z2vollbl;
        zoneVolumeLabel[2] = z3vollbl;
        zoneVolumeLabel[3] = z4vollbl;
        zoneVolumeLabel[4] = z5vollbl;
        zoneVolumeLabel[5] = z6vollbl;

        zoneBassLabel = new Label[6];
        zoneBassLabel[0] = z1basslbl;
        zoneBassLabel[1] = z2basslbl;
        zoneBassLabel[2] = z3basslbl;
        zoneBassLabel[3] = z4basslbl;
        zoneBassLabel[4] = z5basslbl;
        zoneBassLabel[5] = z6basslbl;

        zoneTrebLabel = new Label[6];
        zoneTrebLabel[0] = z1treblbl;
        zoneTrebLabel[1] = z2treblbl;
        zoneTrebLabel[2] = z3treblbl;
        zoneTrebLabel[3] = z4treblbl;
        zoneTrebLabel[4] = z5treblbl;
        zoneTrebLabel[5] = z6treblbl;

        zoneBalanceLabel = new Label[6];
        zoneBalanceLabel[0] = z1ballbl;
        zoneBalanceLabel[1] = z2ballbl;
        zoneBalanceLabel[2] = z3ballbl;
        zoneBalanceLabel[3] = z4ballbl;
        zoneBalanceLabel[4] = z5ballbl;
        zoneBalanceLabel[5] = z6ballbl;

        // display live slider values as integers in labels beside
        zoneVolumeInt = new IntegerProperty[6];
        z1volint = new SimpleIntegerProperty();
        z2volint = new SimpleIntegerProperty();
        z3volint = new SimpleIntegerProperty();
        z4volint = new SimpleIntegerProperty();
        z5volint = new SimpleIntegerProperty();
        z6volint = new SimpleIntegerProperty();
        z1volint.bindBidirectional(z1vol.valueProperty());
        z2volint.bindBidirectional(z2vol.valueProperty());
        z3volint.bindBidirectional(z3vol.valueProperty());
        z4volint.bindBidirectional(z4vol.valueProperty());
        z5volint.bindBidirectional(z5vol.valueProperty());
        z6volint.bindBidirectional(z6vol.valueProperty());
        zoneVolumeInt[0] = z1volint;
        zoneVolumeInt[1] = z2volint;
        zoneVolumeInt[2] = z3volint;
        zoneVolumeInt[3] = z4volint;
        zoneVolumeInt[4] = z5volint;
        zoneVolumeInt[5] = z6volint;

        zoneBassInt = new IntegerProperty[6];
        z1bassint = new SimpleIntegerProperty();
        z2bassint = new SimpleIntegerProperty();
        z3bassint = new SimpleIntegerProperty();
        z4bassint = new SimpleIntegerProperty();
        z5bassint = new SimpleIntegerProperty();
        z6bassint = new SimpleIntegerProperty();
        z1bassint.bindBidirectional(z1bass.valueProperty());
        z2bassint.bindBidirectional(z2bass.valueProperty());
        z3bassint.bindBidirectional(z3bass.valueProperty());
        z4bassint.bindBidirectional(z4bass.valueProperty());
        z5bassint.bindBidirectional(z5bass.valueProperty());
        z6bassint.bindBidirectional(z6bass.valueProperty());
        zoneBassInt[0] = z1bassint;
        zoneBassInt[1] = z2bassint;
        zoneBassInt[2] = z3bassint;
        zoneBassInt[3] = z4bassint;
        zoneBassInt[4] = z5bassint;
        zoneBassInt[5] = z6bassint;

        zoneTrebInt = new IntegerProperty[6];
        z1trebint = new SimpleIntegerProperty();
        z2trebint = new SimpleIntegerProperty();
        z3trebint = new SimpleIntegerProperty();
        z4trebint = new SimpleIntegerProperty();
        z5trebint = new SimpleIntegerProperty();
        z6trebint = new SimpleIntegerProperty();
        z1trebint.bindBidirectional(z1treb.valueProperty());
        z2trebint.bindBidirectional(z2treb.valueProperty());
        z3trebint.bindBidirectional(z3treb.valueProperty());
        z4trebint.bindBidirectional(z4treb.valueProperty());
        z5trebint.bindBidirectional(z5treb.valueProperty());
        z6trebint.bindBidirectional(z6treb.valueProperty());
        zoneTrebInt[0] = z1trebint;
        zoneTrebInt[1] = z2trebint;
        zoneTrebInt[2] = z3trebint;
        zoneTrebInt[3] = z4trebint;
        zoneTrebInt[4] = z5trebint;
        zoneTrebInt[5] = z6trebint;

        zoneBalanceInt = new IntegerProperty[6];
        z1balint = new SimpleIntegerProperty();
        z2balint = new SimpleIntegerProperty();
        z3balint = new SimpleIntegerProperty();
        z4balint = new SimpleIntegerProperty();
        z5balint = new SimpleIntegerProperty();
        z6balint = new SimpleIntegerProperty();
        z1balint.bindBidirectional(z1bal.valueProperty());
        z2balint.bindBidirectional(z2bal.valueProperty());
        z3balint.bindBidirectional(z3bal.valueProperty());
        z4balint.bindBidirectional(z4bal.valueProperty());
        z5balint.bindBidirectional(z5bal.valueProperty());
        z6balint.bindBidirectional(z6bal.valueProperty());
        zoneBalanceInt[0] = z1balint;
        zoneBalanceInt[1] = z2balint;
        zoneBalanceInt[2] = z3balint;
        zoneBalanceInt[3] = z4balint;
        zoneBalanceInt[4] = z5balint;
        zoneBalanceInt[5] = z6balint;

        // initialize zone info
        initZones();

        // FXML Power Buttons
        z1power.setOnAction(e -> powerToggle(0));
        z2power.setOnAction(e -> powerToggle(1));
        z3power.setOnAction(e -> powerToggle(2));
        z4power.setOnAction(e -> powerToggle(3));
        z5power.setOnAction(e -> powerToggle(4));
        z6power.setOnAction(e -> powerToggle(5));

        // FXML Volume Sliders
        z1vol.setOnMouseReleased(e -> setVolume(0));
        z2vol.setOnMouseReleased(e -> setVolume(1));
        z3vol.setOnMouseReleased(e -> setVolume(2));
        z4vol.setOnMouseReleased(e -> setVolume(3));
        z5vol.setOnMouseReleased(e -> setVolume(4));
        z6vol.setOnMouseReleased(e -> setVolume(5));

        // FXML Bass Sliders
        z1bass.setOnMouseReleased(e -> setBass(0));
        z2bass.setOnMouseReleased(e -> setBass(1));
        z3bass.setOnMouseReleased(e -> setBass(2));
        z4bass.setOnMouseReleased(e -> setBass(3));
        z5bass.setOnMouseReleased(e -> setBass(4));
        z6bass.setOnMouseReleased(e -> setBass(5));

        // FXML Treble Sliders
        z1treb.setOnMouseReleased(e -> setTreble(0));
        z2treb.setOnMouseReleased(e -> setTreble(1));
        z3treb.setOnMouseReleased(e -> setTreble(2));
        z4treb.setOnMouseReleased(e -> setTreble(3));
        z5treb.setOnMouseReleased(e -> setTreble(4));
        z6treb.setOnMouseReleased(e -> setTreble(5));

        // FXML Balance Sliders
        z1bal.setOnMouseReleased(e -> setBalance(0));
        z2bal.setOnMouseReleased(e -> setBalance(1));
        z3bal.setOnMouseReleased(e -> setBalance(2));
        z4bal.setOnMouseReleased(e -> setBalance(3));
        z5bal.setOnMouseReleased(e -> setBalance(4));
        z6bal.setOnMouseReleased(e -> setBalance(5));

        // FXML Mute Checkboxes
        z1mute.setOnAction(e -> setMute(0));
        z2mute.setOnAction(e -> setMute(1));
        z3mute.setOnAction(e -> setMute(2));
        z4mute.setOnAction(e -> setMute(3));
        z5mute.setOnAction(e -> setMute(4));
        z6mute.setOnAction(e -> setMute(5));
    }
}