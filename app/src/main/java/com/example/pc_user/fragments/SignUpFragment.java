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
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
    private EditText inputEmail, inputPass, inputConfirm;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        _init();
        _setOnclick();

        return view;
    }

    private void _init() {
        inputEmail = view.findViewById(R.id.inputEmail);
        inputPass = view.findViewById(R.id.inputPass);
        inputConfirm = view.findViewById(R.id.inputPassConfirm);
        btnRegister = view.findViewById(R.id.btnRegister);

        progressDialog = new ProgressDialog(getActivity());

        auth = FirebaseAuth.getInstance();
    }

    private void _setOnclick() {
        btnRegister.setOnClickListener(view -> _register());
    }

    private void _register() {
        String email = inputEmail.getText().toString().trim();
        String pass = inputPass.getText().toString().trim();
        String confirm = inputConfirm.getText().toString().trim();

        boolean isNull = ValidateInput.isNull(email, pass, confirm);
        boolean isEmail = ValidateInput.isEmail(email);
        boolean isPass = ValidateInput.isPass(pass);
        boolean isConfirm = ValidateInput.isConfirm(pass, confirm);
        boolean isBreaking = ValidateInput.isBreakingSignUp(getActivity(), isNull, isEmail, isPass, isConfirm);
        if (isBreaking) return;

        progressDialog.show();
        _handlerRegister(email, pass);
    }

    private void _handlerRegister(String email, String pass) {
        // create user in auth
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    progressDialog.cancel();
                    if (task.isSuccessful()) {
                        DBLocal.saveUserCurrent(getActivity(), email, pass);
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finishAffinity();
                    } else {
                        ShowDialog.handleShowDialog(requireActivity(), Const.flagErrorDialog, "Đăng kí tài khoản thất bại!");
                        Log.e("Error Sign Up", task.getException()+"");
                    }
                });
    }
}