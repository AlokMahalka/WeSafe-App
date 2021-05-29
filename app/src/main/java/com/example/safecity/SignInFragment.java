package com.example.safecity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {
    private FrameLayout parent_fl;
    private EditText email;
    private EditText pass;
    private Button signInBtn;
    private TextView registerTV;
    private ProgressBar pb;
    private FirebaseAuth fa;
    private String regex_email = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        parent_fl = getActivity().findViewById(R.id.main_fl);
        email = v.findViewById(R.id.signin_ed_email);
        pass = v.findViewById(R.id.sign_in_password);
        signInBtn = v.findViewById(R.id.signInBtn);
        pb = v.findViewById(R.id.signinPB);
        fa = FirebaseAuth.getInstance();
        registerTV = v.findViewById(R.id.tv_register);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPass();
            }
        });
    }

    public void setFragment(Fragment fg) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        ft.replace(parent_fl.getId(), fg);
        ft.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(pass.getText())) {
                signInBtn.setEnabled(true);
            } else {
                signInBtn.setEnabled(false);
            }
        } else {
            signInBtn.setEnabled(false);
        }
    }

    private void checkEmailandPass() {
        if (email.getText().toString().matches(regex_email)) {
            if (pass.length() >= 8) {
                signInBtn.setEnabled(false);
                signInBtn.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);

                fa.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        } else {
                            pb.setVisibility(View.INVISIBLE);
                            signInBtn.setEnabled(true);
                            signInBtn.setVisibility(View.VISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(getActivity(), "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Invalid Credentials!", Toast.LENGTH_SHORT).show();
        }
    }

}