package com.example.pc_user.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pc_user.MainActivity;
import com.example.pc_user.R;
import com.example.pc_user.activities.EditPasswordActivity;
import com.example.pc_user.functions.get_const.Const;
import com.example.pc_user.functions.notification_dialog.ShowDialog;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
    private ImageView imgBack;
    private CircleImageView imgUser;
    private ImageView imgOpenGallery, imgSetting;
    private TextView txtUserName, textEmail;
    private LinearLayout layoutInfo;
    private ProgressDialog progressDialog;
    private Button btnUpdateProfile, btnUpdatePass;
    private LinearLayout layoutLogout;

    private EditText inputUserName, inputEmail;
    private Button btnCancel, btnUpdate;
    private TextView txtWarningMessage;
    private Button btnNo, btnYes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'
        view = inflater.inflate(R.layout.fragment_account, container, false);
        _init();
        _setAnimation();
        _setDefault();
        _setOnclick();

        return view;
    }

    private void _init() {
        imgBack = view.findViewById(R.id.imgBackUser);
        imgUser = view.findViewById(R.id.imgUser);
        imgOpenGallery = view.findViewById(R.id.imgChooseImageBack);
        imgSetting = view.findViewById(R.id.imgSetting);
        txtUserName = view.findViewById(R.id.textUserName);
        textEmail = view.findViewById(R.id.textEmail);
        layoutInfo = view.findViewById(R.id.layoutProfile);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        progressDialog = new ProgressDialog(getActivity());
    }

    private void _setAnimation() {
        float v = 0;
        // img back
        imgBack.setTranslationY(-300);
        imgBack.setAlpha(v);
        imgBack.animate().translationY(v).alpha(1).setDuration(750).setStartDelay(100).start();

        // avt
        imgUser.setTranslationX(-500);
        imgUser.setAlpha(v);
        imgUser.animate().translationX(v).alpha(1).setDuration(900).setStartDelay(100).start();

        // img open gallery
        imgOpenGallery.setTranslationX(300);
        imgOpenGallery.setAlpha(v);
        imgOpenGallery.animate().translationX(v).alpha(1).setDuration(1000).setStartDelay(200).start();

        // Layout profile
        layoutInfo.setTranslationX(-1000);
        layoutInfo.setAlpha(v);
        layoutInfo.animate().translationX(v).alpha(1).setDuration(1300).setStartDelay(300).start();

        // img setting
        imgSetting.setTranslationX(300);
        imgSetting.setAlpha(v);
        imgSetting.animate().translationX(v).alpha(1).setDuration(1300).setStartDelay(500).start();

        // layout logout
        layoutLogout.setTranslationY(300);
        layoutLogout.setAlpha(v);
        layoutLogout.animate().translationY(v).alpha(1).setDuration(750).setStartDelay(100).start();
    }

    private void _setDefault() {
        FirebaseUser userCurrent = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("CheckUser", userCurrent+"");
        if (userCurrent == null) return;
        String email = userCurrent.getEmail();
        String userName = (userCurrent.getDisplayName() == null || userCurrent.getDisplayName().length() == 0 || userCurrent.getDisplayName().isEmpty())
                ? "USER NAME" : userCurrent.getDisplayName();

        Glide.with(getActivity())
                .load(userCurrent.getPhotoUrl())
                .error(R.mipmap.ic_launcher)
                .into(imgUser);
        textEmail.setText(email);
        txtUserName.setText(
                (userName == null) ? "USER NAME" : userName
        );
    }

    private void _setOnclick() {
        imgOpenGallery.setOnClickListener(view -> _openGallery());
        imgSetting.setOnClickListener(view -> _showDialogOption());
        layoutLogout.setOnClickListener(view -> _logOut());
    }

    private void _openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    final private ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    progressDialog.show();

                    Intent i = result.getData();
                    if (i == null) return;
                    Uri uri = i.getData();
                    Glide.with(requireActivity()) // show image after chosen
                            .load(uri)
                            .error(R.mipmap.ic_launcher)
                            .into(imgUser);

                    _handleUpdateAvt(uri);
                }
            });

    private void _handleUpdateAvt(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    progressDialog.cancel();
                    if (task.isSuccessful()) {
                        Log.d("Update img user", "Successful");
                    } else {
                        Log.d("Update img user", "Fail");
                    }
                });
    }

    private void _showDialogOption() {
        Dialog dialog = ShowDialog.getDialogChooseOption(getActivity(), 0);

        btnUpdatePass = dialog.findViewById(R.id.btnUpdatePass);
        btnUpdateProfile = dialog.findViewById(R.id.btnUpdteProfile);

        _setOnclick(dialog, 1);
    }

    private void _setOnclick(Dialog dialog, int flag) {
        // flag == 1 => dialog update
        // flag == -1 => dialog logout
        if (flag == 1) {
            btnUpdateProfile.setOnClickListener(view -> {
                dialog.cancel();
                _showDialogUpdateProfile();
            });
            btnUpdatePass.setOnClickListener(view -> {
                dialog.cancel();
                startActivity(new Intent(getActivity(), EditPasswordActivity.class));
            });
        } else {
            btnNo.setOnClickListener(view -> dialog.cancel());
            btnYes.setOnClickListener(view -> _handleLogOut(dialog));
        }
    }

    private void _showDialogUpdateProfile() {
        Dialog dialog = ShowDialog.getDialogChooseOption(getActivity(), 1);

        inputUserName = dialog.findViewById(R.id.inputUpdateUserName);
        inputEmail = dialog.findViewById(R.id.inputUpdateEmail);
        btnCancel = dialog.findViewById(R.id.btnCancelUpdateProfile);
        btnUpdate = dialog.findViewById(R.id.btnUpdateProfile);

        _setDefault(1);
        btnCancel.setOnClickListener(view -> dialog.cancel());
        btnUpdate.setOnClickListener(view -> _handleUpdateProfile(dialog));
    }

    private void _showDialogUpdatePass() {
        Dialog dialog = ShowDialog.getDialogChooseOption(getActivity(), -1);

        inputUserName = dialog.findViewById(R.id.inputUpdatePass);
        btnCancel = dialog.findViewById(R.id.btnCancelUpdatePass);
        btnUpdate = dialog.findViewById(R.id.btnUpdatePass);

        btnCancel.setOnClickListener(view -> dialog.cancel());
        btnUpdate.setOnClickListener(view ->_handleUpdatePass());
    }

    private void _setDefault(int flag) {
        // flag == 1 => dialog update profile
        // flag = -1 => dialog update pass
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String userName = user.getDisplayName() == null ? "USER NAME" : user.getDisplayName() ;
        String email = user.getEmail();


        if (flag == 1) {
            inputEmail.setText(userName);
            inputEmail.setText(email);
        }
    }

    private void _handleUpdateProfile(Dialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("User == null", (user == null)+"");
        if (user == null) return;

        String userName = inputUserName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        if (userName.isEmpty() || email.isEmpty()) {
            ShowDialog.handleShowDialog(getActivity(), Const.flagErrorDialog, "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        // update user name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d("Update user name", "Successful!");
                    } else {
                        Log.d("Update user name", "Fail!");
                    }
                });
        user.updateEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Update email", "Successful!");
                    } else {
                        Log.d("Update email", "Successful!");
                    }
                });

        progressDialog.show();
        new Handler().postDelayed(() -> {
            progressDialog.cancel();
            dialog.cancel();
            _setDefault();
        }, 3000);
    }

    private void _handleUpdatePass() {
        startActivity(new Intent(getActivity(), EditPasswordActivity.class));
    }

    private void _logOut() {
        Dialog dialog = ShowDialog.getDialogLogout(getActivity());

        _init(dialog);
        _setOnclick(dialog, -1);
    }

    private void _init(Dialog dialog) {
        txtWarningMessage = dialog.findViewById(R.id.textWarningMessage);
        btnNo = dialog.findViewById(R.id.btnNoWarningDialog);
        btnYes = dialog.findViewById(R.id.btnYesWarningDialog);

        txtWarningMessage.setText("Bạn có muốn đăng xuất?");
        btnYes.setText("Có");
        btnNo.setText("Không");
    }

    private void _handleLogOut(Dialog dialog) {
        AuthUI.getInstance()
                .signOut(requireActivity())
                .addOnCompleteListener(task -> {
                    dialog.cancel();
                    startActivity(new Intent(requireActivity(), MainActivity.class));
                });
    }
}