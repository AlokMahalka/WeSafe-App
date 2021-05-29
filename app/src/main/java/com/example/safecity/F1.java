package com.example.safecity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class F1 extends Fragment {
    private Button agreeButton;
    private FrameLayout parent_fl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f1, container, false);
        agreeButton = v.findViewById(R.id.agreeButton);
        parent_fl = getActivity().findViewById(R.id.introFL);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new F2());
            }
        });
        return v;
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(parent_fl.getId(), fragment);
        ft.commit();
    }
}