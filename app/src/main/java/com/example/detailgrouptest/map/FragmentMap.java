package com.example.detailgrouptest.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.detailgrouptest.R;

public class FragmentMap extends Fragment {

  private MapActivity mapActivity;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    getActivity().startActivity(new Intent(getActivity(), MapActivity.class));

    return inflater.inflate(R.layout.fragment_map, container, false);
  }
}