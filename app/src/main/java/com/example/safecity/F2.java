package com.example.safecity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class F2 extends Fragment {
    private Button yesBtn;
    private Button noBtn;
    private FrameLayout parent_fl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_f2, container, false);
        parent_fl = getActivity().findViewById(R.id.introFL);
        yesBtn = v.findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), VerificationActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        noBtn = v.findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(parent_fl.getId(), new notEighteen());
                ft.commit();
            }
        });
        return v;
    }
}