package com.example.safecity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class notEighteen extends Fragment {
    private FrameLayout parent_fl;
    private Button goBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_not_eighteen, container, false);
        parent_fl = getActivity().findViewById(R.id.introFL);
        goBack = v.findViewById(R.id.goBackBtn);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(parent_fl.getId(), new F2());
                ft.commit();
            }
        });
        return v;
    }
}