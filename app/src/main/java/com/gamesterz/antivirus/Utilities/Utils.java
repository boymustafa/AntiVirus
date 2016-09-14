package com.gamesterz.antivirus.Utilities;

import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

/**
 * Created by Boy Mustafa on 10/06/16.
 */
public class Utils {

    public static class DeviceMemory{
        public static float getInternalStorageSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float total = ((float)statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            return total;
        }

        public static float getInternalFreeSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float free  = ((float)statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            return free;
        }

        public static float getInternalUsedSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float total = ((float)statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            float free  = ((float)statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            float busy  = total - free;
            return busy;
        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
