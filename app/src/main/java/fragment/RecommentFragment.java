package fragment;



import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;

import adapter.MyFragmentAdapter;

public class RecommentFragment extends Fragment implements ViewPager.OnPageChangeListener, Button.OnClickListener {
	MyFragmentAdapter adapter;
	List<Fragment> list=new ArrayList<Fragment>();
	ViewPager viewPager;
	TextView progressLine;
	Button btn_songList,btn_live,btn_rank;
	RelativeLayout relativeLayout;
	int width;
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_recomment, null);
		viewPager = (ViewPager) view.findViewById(R.id.recomment_viewpager);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btn_rank= (Button) view.findViewById(R.id.recomment_ranks);
		btn_live= (Button) view.findViewById(R.id.recomment_live);
		btn_songList= (Button) view.findViewById(R.id.recomment_musiclist);
		btn_rank.setOnClickListener(this);
		btn_songList.setOnClickListener(this);
		btn_live.setOnClickListener(this);
		relativeLayout= (RelativeLayout) view.findViewById(R.id.recomment_nav_progress);
		progressLine=new TextView(getContext());
		Point point=new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(point);
		width=point.x;
		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width/3,5);
		progressLine.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		progressLine.setLayoutParams(params);
		relativeLayout.addView(progressLine);

		list.add(new RecommentMusicListFragment());
		list.add(new RecommentLiveFragment());
		list.add(new RecommentRanksFragment());
		adapter=new MyFragmentAdapter(getChildFragmentManager(),list);
		viewPager.addOnPageChangeListener(this);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
		btn_songList.setTextColor(getResources().getColor(R.color.colorPrimary));


	}

	@Override
	public void onResume() {
		super.onResume();
//		list.add(new RecommentMusicListFragment());
//		list.add(new RecommentLiveFragment());
//		list.add(new RecommentRanksFragment());
//		adapter=new MyFragmentAdapter(getChildFragmentManager(),list);
//		viewPager.addOnPageChangeListener(this);
//		viewPager.setAdapter(adapter);
//		viewPager.setCurrentItem(0);
//		btn_songList.setTextColor(getResources().getColor(R.color.colorPrimary));

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		int left = (position*width+positionOffsetPixels)/3;
		relativeLayout.setPadding(left,0,0,0);

	}

	@Override
	public void onPageSelected(int position) {
		click(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onClick(View v) {
		click(v.getId());
	}

	private void click(int id){
		if (id==R.id.recomment_musiclist||id==0){
			btn_songList.setTextColor(getResources().getColor(R.color.colorPrimary));
			btn_live.setTextColor(getResources().getColor(R.color.colorUnClick2));
			btn_rank.setTextColor(getResources().getColor(R.color.colorUnClick2));
			viewPager.setCurrentItem(0);
		}else if(id==R.id.recomment_live||id==1){
			btn_live.setTextColor(getResources().getColor(R.color.colorPrimary));
			btn_songList.setTextColor(getResources().getColor(R.color.colorUnClick2));
			btn_rank.setTextColor(getResources().getColor(R.color.colorUnClick2));
			viewPager.setCurrentItem(1);
		}else if (id==R.id.recomment_ranks||id==2){
			btn_rank.setTextColor(getResources().getColor(R.color.colorPrimary));
			btn_live.setTextColor(getResources().getColor(R.color.colorUnClick2));
			btn_songList.setTextColor(getResources().getColor(R.color.colorUnClick2));
			viewPager.setCurrentItem(2);
		}
	}
}
