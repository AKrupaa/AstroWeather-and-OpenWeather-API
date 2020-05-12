package com.example.astroweathercz2;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    private final int MAX_SIZE_OF_FRAGMENTS = 3;
    private ArrayList<Fragment> arrayList = new ArrayList<>();
//    private val pageIds= items.map { it.hashCode().toLong() }


    public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        if (arrayList.size() == 2) {
            if (fragment instanceof Information) {
//                Log.e("Instance off", "yes");
                arrayList.remove(1);
                notifyItemRemoved(1);
                arrayList.add(fragment);
                notifyItemInserted(1);
//                notifyDataSetChanged();
            }
        } else {
            arrayList.add(fragment);
            notifyDataSetChanged();
        }
    }


//    @Override
//    public long getItemId(int position) {
//        return arrayList.get(position).hashCode(); // make sure notifyDataSetChanged() works
//    }
//
//    @Override
//    public boolean containsItem(long itemId) {
//      i co tu niby zrobic, ciekawe ciekawe
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}