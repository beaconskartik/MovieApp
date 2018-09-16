/**
 * Created by Kartik on 9/16/18.
 */

package com.movie.app.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public final class MovieFragmentUtils {
    public static Fragment getTopFragmentInBackStack(FragmentManager fragmentManager) {
        Fragment topFragment = null;
        int backStackSize = fragmentManager.getBackStackEntryCount();
        if (backStackSize > 0) {
            String fragmentTag = fragmentManager.getBackStackEntryAt(backStackSize - 1).getName();
            topFragment = fragmentManager.findFragmentByTag(fragmentTag);
        }
        return topFragment;
    }
}
