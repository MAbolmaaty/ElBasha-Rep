package codeztalk.elbasha.delegate.helper;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;


public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<LinearLayout> {

    public BottomNavigationBehavior() {
        super();
    }

    public BottomNavigationBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        boolean dependsOn = dependency instanceof FrameLayout;
        return dependsOn;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, LinearLayout child, View target, int dx, int dy, int[] consumed) {
        if (dy < 0) {
            showBottomNavigationView(child);
        } else if (dy > 0) {
            hideBottomNavigationView(child);
        }
    }

    private void hideBottomNavigationView(LinearLayout view) {
        view.animate().translationY(view.getHeight());
    }

    private void showBottomNavigationView(LinearLayout view) {
        view.animate().translationY(0);
    }
}