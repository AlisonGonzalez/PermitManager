package com.pm.permitmanager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pm.permitmanager.CompanyFragment;
import com.pm.permitmanager.PeriodFragment;
import com.pm.permitmanager.SalesFragment;

/**
 * Created by AlisonGonzalez on 06/03/18.
 */

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                CompanyFragment companyFragment = new CompanyFragment();
                return companyFragment;
            case 1:
                SalesFragment salesFragment = new SalesFragment();
                return salesFragment;
            case 2:
                PeriodFragment periodFragment = new PeriodFragment();
                return periodFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
