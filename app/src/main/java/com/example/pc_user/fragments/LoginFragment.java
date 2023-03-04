package com.example.pc_user.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.pc_user.R;
import com.example.pc_user.activities.HomeActivity;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.interact_db_local.DBLocal;
import com.example.pc_user.functions.notification_dialog.ShowDialog;
import com.example.pc_user.functions.validate.ValidateInput;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private EditText inputEmail, inputPass;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        _init();
        _setOnclick();

        return view;
    }

    private void _init() {
        inputEmail = view.findViewById(R.id.inputEmail);
        inputPass = view.findViewById(R.id.inputPass);
        btnLogin = view.findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(getActivity());
    }

    private void _setOnclick() {
        btnLogin.setOnClickListener(view -> _login());
    }

    private void _login() {
        String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();

        boolean isNull = ValidateInput.isNull(email, pass);
        boolean isEmail = ValidateInput.isEmail(email);
        boolean isBreaking = ValidateInput.isBreakingLogin(getActivity(), isNull, isEmail);
        if (isBreaking) return;

        progressDialog.show();
        _handleLogin(email, pass);
    }

    private void _handleLogin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    progressDialog.cancel();
                    if (task.isSuccessful()) {
                        DBLocal.saveUserCurrent(getActivity(), email, pass);
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finishAffinity();
                    } else {
                        ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Đăng nhập thất bại!!!");
                        Log.e("Error sign in", task.getException()+"");
                    }
                });
    }
}