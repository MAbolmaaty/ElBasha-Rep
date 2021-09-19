package codeztalk.elbasha.delegate.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import codeztalk.elbasha.delegate.R;

public class UiUtils {

    //Attaching Fragment
    public static void loadFragment(FragmentManager fragmentManager,
                                    int container,
                                    Fragment fragment,
                                    boolean addToBackStack) {
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(container, fragment)
                    .commit();
        }
    }
}
