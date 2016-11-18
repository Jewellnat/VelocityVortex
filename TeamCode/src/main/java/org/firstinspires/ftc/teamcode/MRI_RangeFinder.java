package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by ScottShew on 10/8/2016.
 */

public class MRI_RangeFinder {
    private static final int RANGE1_REG_START = 0x04; //Register to start reading
    private static final int RANGE1_READ_LENGTH = 2; //Number of byte to read
    private byte[] range1Cache; //The read will return an array of bytes. They are stored in this variable
    private I2cAddr RANGE1ADDRESS = new I2cAddr(0x14); //Default I2C address for MR Range (7-bit)
    private I2cDevice RANGE1;
    private I2cDeviceSynch RANGE1Reader;

    public MRI_RangeFinder(I2cDevice rangeFinderDevice) {
        RANGE1 = rangeFinderDevice;
        RANGE1Reader = new I2cDeviceSynchImpl(RANGE1, RANGE1ADDRESS, false);
        RANGE1Reader.engage();
    }

    public void resetDeviceConfigurationForOpMode() {
        RANGE1.resetDeviceConfigurationForOpMode();
    }

    public int getDistanceCM() {
        range1Cache = RANGE1Reader.read(RANGE1_REG_START, RANGE1_READ_LENGTH);

        // index 0 is the sonar portion of the sensor.
        return range1Cache[0] & 0xFF;

        // index 1 is the optical portion of the sensor.
        // The return from the optical sensor increases exponentially with the
        // decrease in distance from the target.  Unfortunately, it is also
        // highly sensitive to the reflectivity of target material.
//        return range1Cache[1] & 0xFF;
    }
}
