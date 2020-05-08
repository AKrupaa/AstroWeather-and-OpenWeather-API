package com.example.astroweathercz2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private static final int MAX_LENGHT_OF_FRAGMENTS = 3;
    private ArrayList<Fragment> arrayList = new ArrayList<>();


    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }

    public void replaceFragment(Fragment fragment, int arrayPosition) {
        arrayList.remove(arrayPosition);
        arrayList.add(arrayPosition, fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

//        return arrayList.get(position);
        switch (position) {
            case 0:
                return Options.newInstance();
            case 1:
                return Moon.newInstance();
            case 2:
                return Sun.newInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {

        return MAX_LENGHT_OF_FRAGMENTS;
//        return arrayList.size();
    }
}