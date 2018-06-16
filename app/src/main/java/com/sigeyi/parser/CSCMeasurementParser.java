package com.sigeyi.parser;

import android.bluetooth.BluetoothGattCharacteristic;

import java.util.Locale;

public class CSCMeasurementParser {
	private static final byte WHEEL_REV_DATA_PRESENT = 0x01; // 1 bit
	private static final byte CRANK_REV_DATA_PRESENT = 0x02; // 1 bit

	public static String parse(final BluetoothGattCharacteristic characteristic) {
		int offset = 0;
		final int flags = characteristic.getValue()[offset]; // 1 byte
		offset += 1;

		final boolean wheelRevPresent = (flags & WHEEL_REV_DATA_PRESENT) > 0;
		final boolean crankRevPreset = (flags & CRANK_REV_DATA_PRESENT) > 0;

		int wheelRevolutions = 0;
		int lastWheelEventTime = 0;
		if (wheelRevPresent) {
			wheelRevolutions = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset);
			offset += 4;

			lastWheelEventTime = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset); // 1/1024 s
			offset += 2;
		}

		int crankRevolutions = 0;
		int lastCrankEventTime = 0;
		if (crankRevPreset) {
			crankRevolutions = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
			offset += 2;

			lastCrankEventTime = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
			//offset += 2;
		}

		final StringBuilder builder = new StringBuilder();
		if (wheelRevPresent) {
			builder.append(String.format(Locale.US, "Wheel rev: %d,\n", wheelRevolutions));
			builder.append(String.format(Locale.US, "Last wheel event time: %d ms,\n", lastWheelEventTime));
		}
		if (crankRevPreset) {
			builder.append(String.format(Locale.US, "Crank rev: %d,\n", crankRevolutions));
			builder.append(String.format(Locale.US, "Last crank event time: %d ms,\n", lastCrankEventTime));
		}
		builder.setLength(builder.length() - 2);
		return builder.toString();
	}
}
