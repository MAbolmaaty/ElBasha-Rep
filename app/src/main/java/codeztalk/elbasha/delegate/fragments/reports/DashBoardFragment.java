package codeztalk.elbasha.delegate.fragments.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.fragments.BaseFragment;

public class DashBoardFragment extends BaseFragment
{

    TabLayout tabLayout;




    private Reports5Fragment reports5Fragment;
    private Reports6Fragment reports6Fragment;
    private Reports7Fragment reports7Fragment;
    private Reports8Fragment reports8Fragment;
    private Reports9Fragment reports9Fragment;
    private Reports10Fragment reports10Fragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.fragment_dashboard);


        reports5Fragment = Reports5Fragment.newInstance();
        reports6Fragment = Reports6Fragment.newInstance();
        reports7Fragment = Reports7Fragment.newInstance();
        reports8Fragment = Reports8Fragment.newInstance();
        reports9Fragment = Reports9Fragment.newInstance();
        reports10Fragment = Reports10Fragment.newInstance();


        ViewPager mViewPager = mView.findViewById(R.id.my_pager);
        tabLayout = mView.findViewById(R.id.tabs);


        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter( getChildFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] mFragments = new Fragment[]{

                    reports5Fragment,
                    reports6Fragment,
                    reports7Fragment,
                    reports8Fragment,
                    reports9Fragment,
                    reports10Fragment,


            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.report5),
                    getString(R.string.report6),
                    getString(R.string.report7),
                    getString(R.string.report8),
                    getString(R.string.report9),
                    getString(R.string.report10),


            };

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };


        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


        return mView;

    }

}
