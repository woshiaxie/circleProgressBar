package com.huang.customedview.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by codemonster on 2017/8/5.
 */

public class DeviceUtil {
    public float getPixcelSize (int type, int size, DisplayMetrics metrics) {
        return TypedValue.applyDimension(type, size, metrics);
    }
}
