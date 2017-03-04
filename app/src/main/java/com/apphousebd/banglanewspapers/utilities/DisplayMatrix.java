package com.apphousebd.banglanewspapers.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Asif Imtiaz Shaafi on 03, 2017.
 * Email: a15shaafi.209@gmail.com
 */

public class DisplayMatrix {


    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
