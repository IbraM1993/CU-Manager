package fl.futuerx.com.mutmanager.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class GenericFragmentAdapter extends SmartFragmentStatePagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titles;
    public GenericFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        setFragmentList(fragmentList);
        this.titles = titleList == null ? new ArrayList<String>() : titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragmentList().get(position);
    }

    @Override
    public int getCount() {
        return getFragmentList().size();
    }

    public List<Fragment> getFragmentList() {
        if(fragmentList == null)
            fragmentList = new ArrayList<>();
        return fragmentList;
    }

    public void setFragmentList(List<Fragment> fragmentList) {
        this.fragmentList = new ArrayList<>();
        if(fragmentList != null)
            this.fragmentList = fragmentList;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position > -1 && position < titles.size())
            return titles.get(position);
        return super.getPageTitle(position);
    }
}
