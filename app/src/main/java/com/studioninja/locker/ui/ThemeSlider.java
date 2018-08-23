package com.studioninja.locker.ui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.studioninja.locker.R;

public class ThemeSlider extends Activity implements OnClickListener {

	ViewPager viewPager;
	MyPagerAdapter myPagerAdapter;
	ImageView back, applytheme;
	TextView theme_title;
	int index = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.themeslider);
		viewPager = (ViewPager) findViewById(R.id.myviewpager);
		back = (ImageView) findViewById(R.id.back);
		applytheme = (ImageView) findViewById(R.id.applytheme);
		theme_title = (TextView) findViewById(R.id.theme_title);

		Bundle bundle = this.getIntent().getExtras();
		index = bundle.getInt("index");

		myPagerAdapter = new MyPagerAdapter();
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setCurrentItem(index);
		theme_title.setText("Theme" + (index + 1));

		back.setOnClickListener(this);
		applytheme.setOnClickListener(this);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrollStateChanged(int arg0) {
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageSelected(int currentPage) {
				// currentPage is the position that is currently displayed.
				index = currentPage;
				theme_title.setText("Theme" + (currentPage + 1));

			}

		});

	}

	private class MyPagerAdapter extends PagerAdapter {

		int NumberOfPages = 12;

		int[] res = { R.drawable.theme1_bg, R.drawable.theme2_bg, R.drawable.theme3_bg, R.drawable.theme4_bg, R.drawable.theme5_bg, R.drawable.theme6_bg, R.drawable.theme7_bg, R.drawable.theme8_bg, R.drawable.theme9_bg,
				R.drawable.theme10_bg, R.drawable.theme11_bg, R.drawable.theme12_bg };

		// int[] backgroundcolor = { 0xFF101010, 0xFF202020, 0xFF303030, 0xFF404040, 0xFF505050 };

		@Override
		public int getCount() {
			return NumberOfPages;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			TextView textView = new TextView(ThemeSlider.this);
			textView.setTextColor(Color.TRANSPARENT);
			textView.setTextSize(30);
			textView.setTypeface(Typeface.DEFAULT_BOLD);
			textView.setText("");

			// Toast.makeText(ThemeSlider.this, "position>>>" + (position + 1), Toast.LENGTH_LONG).show();

			// final int page = position;

			ImageView imageView = new ImageView(ThemeSlider.this);
			imageView.setImageResource(res[position]);
			LayoutParams imageParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(imageParams);

			LinearLayout layout = new LinearLayout(ThemeSlider.this);
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			// layout.setBackgroundColor(backgroundcolor[position]);
			layout.setLayoutParams(layoutParams);
			layout.addView(textView);
			layout.addView(imageView);

			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					index = position;
					// Toast.makeText(ThemeSlider.this, "Page " + page + " clicked", Toast.LENGTH_LONG).show();
				}
			});

			container.addView(layout);
			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Toast.makeText(ThemeSlider.this, "destroyItem>>>" + (position + 1), Toast.LENGTH_LONG).show();
			container.removeView((LinearLayout) object);
		}

	}

	@Override
	public void onClick(View v) {
		if (v == back) {
			finish();
		} else if (v == applytheme) {

			Toast.makeText(ThemeSlider.this, "Theme" + (index + 1) + " applied", Toast.LENGTH_LONG).show();
			PreferenceConnector.writeInteger(ThemeSlider.this, PreferenceConnector.CURRENT_THEME, index + 1);
		}
	}

}