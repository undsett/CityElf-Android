package com.hillelevo.cityelf.activities;


import com.hillelevo.cityelf.R;
import com.hillelevo.cityelf.fragments.AdminAdvertFragment;
import com.hillelevo.cityelf.fragments.AdminPollFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

public class AdminActivity extends FragmentActivity {

  private TabLayout tabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin);

    ViewPager pager = (ViewPager) findViewById(R.id.viewpagerAdmin);

    pager.setAdapter(new AdminPagerAdapter(getSupportFragmentManager()));

    // Set custom tabs for ViewPager
    tabLayout = (TabLayout) findViewById(R.id.tabsAdmin);
    tabLayout.setupWithViewPager(pager);
    setupTabs();

  }

  /**
   * Custom Adapter for ViewPager
   */
  private class AdminPagerAdapter extends FragmentPagerAdapter {

    public AdminPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
      switch (pos) {
        case 0:
          return new AdminAdvertFragment();
        case 1:
          return new AdminPollFragment();
        default:
          return new AdminAdvertFragment();
      }
    }

    /**
     * Tabs in ViewPager
     *
     * @return tabs amount
     */
    @Override
    public int getCount() {
      return 2;
    }
  }

  /**
   * Set up tabs for ViewPager
   */
  private void setupTabs() {
    TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.view_pager_tab, null);
    tabOne.setText(R.string.tab_adverts_title);
    tabLayout.getTabAt(0).setCustomView(tabOne);

    TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.view_pager_tab, null);
    tabTwo.setText(R.string.tab_polls_title);
    tabLayout.getTabAt(1).setCustomView(tabTwo);

  }
}
