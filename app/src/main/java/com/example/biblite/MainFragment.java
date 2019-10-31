package com.example.biblite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class MainFragment extends Fragment {

    public static GridView gv;
  public static GridView getGridView()
  {
      return gv;
  }
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View myFragmentView = inflater.inflate(R.layout.main_fragment, container, false);
        gv = myFragmentView.findViewById(R.id.gridviiew);
        if (MainActivity.RSS_TESTING) new HttpUtils(this).execute("http://fetchrss.com/rss/5d958a8b8a93f88e398b45675d976cf08a93f8bc3e8b4567.xml");

        else
        new HttpUtils(this).execute("http://fetchrss.com/rss/5d958a8b8a93f88e398b45675d9be5558a93f87b2e8b4567.xml");




        return myFragmentView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }




}
