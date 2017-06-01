package adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentAdapter extends FragmentPagerAdapter {
	FragmentManager fm;
	List<Fragment> list;
	public MyFragmentAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.fm=fm;
		this.list=list;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0%(list.size()));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
