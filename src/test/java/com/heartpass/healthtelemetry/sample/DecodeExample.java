/////////////////////////////////////////////////////////////////////////////////////////////
// Copyright 2024 Garmin International, Inc.
// Licensed under the Flexible and Interoperable Data Transfer (FIT) Protocol License; you
// may not use this file except in compliance with the Flexible and Interoperable Data
// Transfer (FIT) Protocol License.
/////////////////////////////////////////////////////////////////////////////////////////////


package com.heartpass.healthtelemetry.sample;

import com.garmin.fit.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
public class DecodeExample {
    public static void main(String[] args) {
        try {
            Decode decode = new Decode();
            //decode.skipHeader();        // Use on streams with no header and footer (stream contains FIT defn and data messages only)
            //decode.incompleteStream();  // This suppresses exceptions with unexpected eof (also incorrect crc)
            MesgBroadcaster mesgBroadcaster = new MesgBroadcaster(decode);
            Listener listener = new Listener();
            FileInputStream in;

            System.out.printf("FIT Decode Example Application - Protocol %d.%d Profile %d.%d %s\n", Fit.PROTOCOL_VERSION_MAJOR, Fit.PROTOCOL_VERSION_MINOR, Fit.PROFILE_VERSION_MAJOR, Fit.PROFILE_VERSION_MINOR, Fit.PROFILE_TYPE);

            if (args.length != 1) {
                System.out.println("Usage: java -jar DecodeExample.jar <filename>");
                return;
            }

            try {
                in = new FileInputStream(args[0]);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Error opening file " + args[0] + " [1]");
            }

            try {
                if (!decode.checkFileIntegrity((InputStream)in)) {
                    throw new RuntimeException("FIT file integrity failed.");
                }
            } catch (RuntimeException e) {
                System.err.print("Exception Checking File Integrity: ");
                System.err.println(e.getMessage());
                System.err.println("Trying to continue...");
            } finally {
                try {
                    in.close();
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                in = new FileInputStream(args[0]);
            } catch (java.io.IOException e) {
                throw new RuntimeException("Error opening file " + args[0] + " [2]");
            }

//            mesgBroadcaster.addListener((BloodPressureMesgListener)listener);
//            mesgBroadcaster.addListener((FileIdMesgListener)listener);
//            mesgBroadcaster.addListener((UserProfileMesgListener)listener);
           mesgBroadcaster.addListener((DeviceInfoMesgListener)listener);
//            mesgBroadcaster.addListener((MonitoringMesgListener)listener);
            // This has heart rate and timestamp
            mesgBroadcaster.addListener((RecordMesgListener)listener);

            decode.addListener((DeveloperFieldDescriptionListener)listener);

            try {
                decode.read(in, mesgBroadcaster, mesgBroadcaster);
            } catch (FitRuntimeException e) {
                // If a file with 0 data size in it's header  has been encountered,
                // attempt to keep processing the file
                if (decode.getInvalidFileDataSize()) {
                    decode.nextFile();
                    decode.read(in, mesgBroadcaster, mesgBroadcaster);
                } else {
                    System.err.print("Exception decoding file: ");
                    System.err.println(e.getMessage());

                    try {
                        in.close();
                    } catch (java.io.IOException f) {
                        throw new RuntimeException(f);
                    }

                    return;
                }
            }

            try {
                in.close();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Decoded FIT file " + args[0] + ".");

        } catch (Exception e) {
            System.out.println("Exception decoding file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static class Listener implements BloodPressureMesgListener, FileIdMesgListener, UserProfileMesgListener,
            DeviceInfoMesgListener, MonitoringMesgListener, RecordMesgListener, DeveloperFieldDescriptionListener {

        /**
         * protected static Mesg 	bloodPressureMesg
         * static int 	DiastolicPressureFieldNum
         * static int 	HeartRateFieldNum
         * static int 	HeartRateTypeFieldNum
         * static int 	Map3SampleMeanFieldNum
         * static int 	MapEveningValuesFieldNum
         * static int 	MapMorningValuesFieldNum
         * static int 	MeanArterialPressureFieldNum
         * static int 	StatusFieldNum
         * static int 	SystolicPressureFieldNum
         * static int 	TimestampFieldNum
         * static int 	UserProfileIndexFieldNum
         * @param mesg
         */
        @Override
        public void onMesg(BloodPressureMesg mesg) {

            if (mesg.getTimestamp() != null) { System.out.println ("Timestamp: " + mesg.getTimestamp()); }
            if (mesg.getDiastolicPressure() != null) { System.out.println ("Diastolic Pressure: " + mesg.getDiastolicPressure()); }
            if (mesg.getSystolicPressure() != null) { System.out.println ("Systolic Pressure: " + mesg.getSystolicPressure()); }
            if (mesg.getHeartRate() != null) { System.out.println ("Heart Rate: " + mesg.getHeartRate()); }
            if (mesg.getHeartRateType()!= null) { System.out.println ("Heart Rate Type: " + mesg.getHeartRateType()); }
            if (mesg.getMap3SampleMean()!= null) { System.out.println ("Map 3 Sample Mean: " + mesg.getMap3SampleMean()); }
        }

        @Override
        public void onMesg(FileIdMesg mesg) {
            System.out.println("File ID:");

            if (mesg.getType() != null) {
                System.out.print("   Type: ");
                System.out.println(mesg.getType().getValue());
            }

            if (mesg.getManufacturer() != null ) {
                System.out.print("   Manufacturer: ");
                System.out.println(mesg.getManufacturer());
            }

            if (mesg.getProduct() != null) {
                System.out.print("   Product: ");
                System.out.println(mesg.getProduct());
            }

            if (mesg.getSerialNumber() != null) {
                System.out.print("   Serial Number: ");
                System.out.println(mesg.getSerialNumber());
            }

            if (mesg.getNumber() != null) {
                System.out.print("   Number: ");
                System.out.println(mesg.getNumber());
            }
        }

        @Override
        public void onMesg(UserProfileMesg mesg) {
            System.out.println("User profile:");

            if (mesg.getFriendlyName() != null) {
                System.out.print("   Friendly Name: ");
                System.out.println(mesg.getFriendlyName());
            }

            if (mesg.getGender() != null) {
                if (mesg.getGender() == Gender.MALE) {
                    System.out.println("   Gender: Male");
                } else if (mesg.getGender() == Gender.FEMALE) {
                    System.out.println("   Gender: Female");
                }
            }

            if (mesg.getAge() != null) {
                System.out.print("   Age [years]: ");
                System.out.println(mesg.getAge());
            }

            if (mesg.getWeight() != null) {
                System.out.print("   Weight [kg]: ");
                System.out.println(mesg.getWeight());
            }
        }

        @Override
        public void onMesg(DeviceInfoMesg mesg) {
            System.out.println("Device info:");

            if (mesg.getTimestamp() != null) {
                System.out.print("   Timestamp: ");
                System.out.println(mesg.getTimestamp());
            }

            if (mesg.getBatteryStatus() != null) {
                System.out.print("   Battery status: ");

                switch (mesg.getBatteryStatus()) {
                case BatteryStatus.CRITICAL:
                    System.out.println("Critical");
                    break;
                case BatteryStatus.GOOD:
                    System.out.println("Good");
                    break;
                case BatteryStatus.LOW:
                    System.out.println("Low");
                    break;
                case BatteryStatus.NEW:
                    System.out.println("New");
                    break;
                case BatteryStatus.OK:
                    System.out.println("OK");
                    break;
                default:
                    System.out.println("Invalid");
                    break;
                }
            }
        }

        @Override
        public void onMesg(MonitoringMesg mesg) {
            System.out.println("Monitoring:");

            if (mesg.getTimestamp() != null) {
                System.out.print("   Timestamp: ");
                System.out.println( mesg.getTimestamp());
            }

            if (mesg.getActivityType() != null) {
                System.out.print("   Activity Type: ");
                System.out.println(mesg.getActivityType());
            }

            // Depending on the ActivityType, there may be Steps, Strokes, or Cycles present in the file
            if (mesg.getSteps() != null) {
                System.out.print("   Steps: ");
                System.out.println( mesg.getSteps());
            } else if (mesg.getStrokes() != null) {
                System.out.print("   Strokes: ");
                System.out.println(mesg.getStrokes());
            } else if (mesg.getCycles() != null) {
                System.out.print("   Cycles: ");
                System.out.println(mesg.getCycles());
            }

            printDeveloperData(mesg);
        }

        @Override
        public void onMesg(RecordMesg mesg) {
            System.out.println("Record:");
            System.out.println("mesg.getTimestamp().getTimestamp() = " + mesg.getTimestamp().getTimestamp());
            System.out.println("mesg.getTimestamp().toString() = " + mesg.getTimestamp().toString());
            System.out.println("mesg.getTimestamp().getFractionalTimestamp() = " + mesg.getTimestamp().getFractionalTimestamp());

            printValues(mesg, RecordMesg.HeartRateFieldNum);
            printValues(mesg, RecordMesg.CadenceFieldNum);
            printValues(mesg, RecordMesg.DistanceFieldNum);
            printValues(mesg, RecordMesg.SpeedFieldNum);
            printValues(mesg, RecordMesg.TemperatureFieldNum);
            printValues(mesg, RecordMesg.TimestampFieldNum);
            printValues(mesg, RecordMesg.PowerFieldNum);
            printValues(mesg, RecordMesg.CoreTemperatureFieldNum);
            printValues(mesg, RecordMesg.CurrentStressFieldNum);
            printValues(mesg, RecordMesg.Po2FieldNum);
            printValues(mesg, RecordMesg.N2LoadFieldNum);
            printValues(mesg, RecordMesg.SaturatedHemoglobinPercentMaxFieldNum);

            printDeveloperData(mesg);
        }

        private void printDeveloperData(Mesg mesg) {
            for (DeveloperField field : mesg.getDeveloperFields()) {
                if (field.getNumValues() < 1) {
                    continue;
                }

                if (field.isDefined()) {
                    System.out.print("   " + field.getName());

                    if (field.getUnits() != null) {
                        System.out.print(" [" + field.getUnits() + "]");
                    }

                    System.out.print(": ");
                } else {
                    System.out.print("   Undefined Field: ");
                }

                System.out.print(field.getValue( 0 ));
                for (int i = 1; i < field.getNumValues(); i++) {
                    System.out.print("," + field.getValue(i));
                }

                System.out.println();
            }
        }

        @Override
        public void onDescription(DeveloperFieldDescription desc) {
            System.out.println("New Developer Field Description");
            System.out.println("   App Id: " + desc.getApplicationId());
            System.out.println("   App Version: " + desc.getApplicationVersion());
            System.out.println("   Field Num: " + desc.getFieldDefinitionNumber());
        }

        private void printValues(Mesg mesg, int fieldNum) {
            Iterable<FieldBase> fields = mesg.getOverrideField((short) fieldNum);
            Field profileField = Factory.createField(mesg.getNum(), fieldNum);
            boolean namePrinted = false;

            if (profileField == null) {
                return;
            }

            for (FieldBase field : fields) {
                if (!namePrinted) {
                    System.out.println("   " + profileField.getName() + ":");
                    namePrinted = true;
                }

                if (field instanceof Field) {
                    System.out.println("      native: " + field.getValue());
                } else {
                    System.out.println("      override: " + field.getValue());
                }
            }
        }
    }
}
