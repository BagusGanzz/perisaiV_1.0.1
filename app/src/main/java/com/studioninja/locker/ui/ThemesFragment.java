package com.studioninja.locker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.studioninja.locker.R;

public class ThemesFragment extends Fragment implements OnClickListener {

	ImageView theme1_button, theme2_button, theme3_button, theme4_button, theme5_button, theme6_button, theme7_button, theme8_button, theme9_button, theme10_button, theme11_button, theme12_button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_themes, container, false);
		getActivity().setTitle(R.string.fragment_title_themes);

		theme1_button = (ImageView) root.findViewById(R.id.theme1);
		theme2_button = (ImageView) root.findViewById(R.id.theme2);
		theme3_button = (ImageView) root.findViewById(R.id.theme3);
		theme4_button = (ImageView) root.findViewById(R.id.theme4);
		theme5_button = (ImageView) root.findViewById(R.id.theme5);
		theme6_button = (ImageView) root.findViewById(R.id.theme6);
		theme7_button = (ImageView) root.findViewById(R.id.theme7);
		theme8_button = (ImageView) root.findViewById(R.id.theme8);
		theme9_button = (ImageView) root.findViewById(R.id.theme9);
		theme10_button = (ImageView) root.findViewById(R.id.theme10);
		theme11_button = (ImageView) root.findViewById(R.id.theme11);
		theme12_button = (ImageView) root.findViewById(R.id.theme12);

		theme1_button.setOnClickListener(this);
		theme2_button.setOnClickListener(this);
		theme3_button.setOnClickListener(this);
		theme4_button.setOnClickListener(this);
		theme5_button.setOnClickListener(this);
		theme6_button.setOnClickListener(this);
		theme7_button.setOnClickListener(this);
		theme8_button.setOnClickListener(this);
		theme9_button.setOnClickListener(this);
		theme10_button.setOnClickListener(this);
		theme11_button.setOnClickListener(this);
		theme12_button.setOnClickListener(this);

		return root;
	}

	@Override
	public void onClick(View v) {
		if (v == theme1_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 0);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 1);
		} else if (v == theme2_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 1);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 2);
		} else if (v == theme3_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 2);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 3);
		} else if (v == theme4_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 3);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 4);
		} else if (v == theme5_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 4);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 5);
		} else if (v == theme6_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 5);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 6);
		} else if (v == theme7_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 6);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 7);
		} else if (v == theme8_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 7);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 8);
		} else if (v == theme9_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 8);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 9);
		} else if (v == theme10_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 9);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 10);
		} else if (v == theme11_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 10);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 11);
		} else if (v == theme12_button) {
			Intent i = new Intent(getActivity(), ThemeSlider.class);
			i.putExtra("index", 11);
			startActivity(i);
			// PreferenceConnector.writeInteger(getActivity(), PreferenceConnector.CURRENT_THEME, 12);
		}
	}

}
