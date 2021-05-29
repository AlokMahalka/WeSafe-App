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
import com.google.common.collect.Maps;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpFragment extends Fragment {
    private FrameLayout parent_fl;
    private EditText name;
    private EditText email;
    private EditText pass;
    private EditText phone;
    private EditText emer1;
    private EditText emer2;
    private EditText emer3;
    private Button signUpBtn;
    private ProgressBar pb;
    private TextView login;
    private FirebaseAuth fba;
    private FirebaseFirestore ffs;
    private String regex_email = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);
        name = v.findViewById(R.id.signUpName);
        email = v.findViewById(R.id.signUpEmail);
        pass = v.findViewById(R.id.signUpPassword);
        phone = v.findViewById(R.id.signUpPhone);
        signUpBtn = v.findViewById(R.id.signUpBtn);
        emer1 = v.findViewById(R.id.emer1);
        emer2 = v.findViewById(R.id.emer2);
        emer3 = v.findViewById(R.id.emer3);
        pb = v.findViewById(R.id.signUpPB);
        login = v.findViewById(R.id.tv_signIn);
        parent_fl = getActivity().findViewById(R.id.main_fl);
        fba = FirebaseAuth.getInstance();
        ffs = FirebaseFirestore.getInstance();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
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
        name.addTextChangedListener(new TextWatcher() {
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
        phone.addTextChangedListener(new TextWatcher() {
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
        emer1.addTextChangedListener(new TextWatcher() {
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
        emer2.addTextChangedListener(new TextWatcher() {
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
        emer3.addTextChangedListener(new TextWatcher() {
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

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                signUpBtn.setVisibility(View.GONE);
                if (email.getText().toString().matches(regex_email)) {
                    checkEmailAndPass();
                } else {
                    pb.setVisibility(View.GONE);
                    signUpBtn.setVisibility(View.VISIBLE);
                    email.setError("Invalid Email Address!");
                    email.requestFocus();
                }
            }
        });
    }

    private void setFragment(Fragment fg) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        ft.replace(parent_fl.getId(), fg);
        ft.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(name.getText())) {
                if (!TextUtils.isEmpty(pass.getText()) && pass.length() >= 8) {
                    if (!TextUtils.isEmpty(phone.getText()) && phone.length() > 9) {
                        if (!TextUtils.isEmpty(emer1.getText()) && emer1.length() > 9) {
                            if (!TextUtils.isEmpty(emer2.getText()) && emer2.length() > 9) {
                                if (!TextUtils.isEmpty(emer3.getText()) && emer3.length() > 9) {
                                    signUpBtn.setEnabled(true);
                                } else {
                                    signUpBtn.setEnabled(false);
                                }
                            } else {
                                signUpBtn.setEnabled(false);
                            }
                        } else {
                            signUpBtn.setEnabled(false);
                        }
                    } else {
                        signUpBtn.setEnabled(false);
                    }
                } else {
                    signUpBtn.setEnabled(false);
                }
            } else {
                signUpBtn.setEnabled(false);
            }
        } else {
            signUpBtn.setEnabled(false);
        }

    }

    private void checkEmailAndPass() {
        fba.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> userdata = new HashMap<>();
                    userdata.put("FullName", name.getText().toString());
                    userdata.put("Email", email.getText().toString());
                    userdata.put("Password", pass.getText().toString());
                    userdata.put("Phone", phone.getText().toString());
                    userdata.put("Emer1", emer1.getText().toString());
                    userdata.put("Emer2", emer2.getText().toString());
                    userdata.put("Emer3", emer3.getText().toString());

                    ffs.collection("USERS").document(fba.getUid())
                            .set(userdata)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    } else {
                                        pb.setVisibility(View.INVISIBLE);
                                        String error = task.getException().getMessage();
                                        signUpBtn.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    pb.setVisibility(View.INVISIBLE);
                    String error = task.getException().getMessage();
                    signUpBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}