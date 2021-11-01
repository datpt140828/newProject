package com.example.foodsellappproject.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.dao.User_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    private TextView tvChangePassword, tvBtnSave;
    private EditText etName, etDob, etEmail, etPhone, etPassword;
    private ImageView ivBack;

    private void loadView(View view) {
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        tvBtnSave = view.findViewById(R.id.tvBtnSave);
        etName = view.findViewById(R.id.etOldPassword);
        etDob = view.findViewById(R.id.etDob);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etAgainNewPassword);
        etPassword = view.findViewById(R.id.etPassword);
        ivBack = view.findViewById(R.id.ivBack);
    }

    private DBConnection db;
    private User_DAO userDao;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load view from layout
        loadView(view);

        //handle ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIvBack();
            }
        });

        //load data from room
        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String user_logined = pref.getString("username", "");

        db = DBConnection.getDBConnection(getContext());
        userDao = db.createUserDAO();

        //handle btnSave
        final User finalUser = setTextView(userDao, user_logined);
        tvBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvBtnSave(finalUser, v);
            }
        });

        //update again after save
        userDao = db.createUserDAO();
        setTextView(userDao, user_logined);

        //handle tvChangePassword
        final Dialog dialog = new Dialog(getActivity());
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvChangePassword(dialog, user_logined);
            }
        });
    }

    private EditText etOldPassword, etNewPassword, etAgainNewPassword;
    private TextView tvBtnSaveChange;

    private void setTvChangePassword(final Dialog dialog, final String user_logined) {
        dialog.setContentView(R.layout.popup_change_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        etOldPassword = dialog.getWindow().findViewById(R.id.etOldPassword);
        etNewPassword = dialog.getWindow().findViewById(R.id.etNewPassword);
        etAgainNewPassword = dialog.getWindow().findViewById(R.id.etAgainNewPassword);
        tvBtnSaveChange = dialog.getWindow().findViewById(R.id.tvBtnComment);

        userDao = db.createUserDAO();
        final User u = (User) userDao.getUserById(user_logined);
        final String password = u.getPassword();

        tvBtnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvBtnSaveChange(password, v, u, dialog);
            }
        });

    }

    private void setTvBtnSaveChange(String password, View v, User u, Dialog dialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        if (password.equalsIgnoreCase(etOldPassword.getText().toString().trim())) {
            if (etNewPassword.getText().toString().equalsIgnoreCase(etAgainNewPassword.getText().toString())) {

                if (etNewPassword.getText().toString().trim().isEmpty()) {
                    builder.setMessage("Mật khẩu không được trống");
                } else {
                    u.setPassword(etNewPassword.getText().toString().trim());
                    userDao.update(u);
                    builder.setMessage("Thay đổi mật khẩu thành công !");
                    dialog.dismiss();
                }

            } else {

                builder.setMessage("Mật khẩu 1 và 2 phải giống nhau");
            }
        } else {
            builder.setMessage("Mật khẩu cũ không chính xác.");
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setTvBtnSave(final User finalUser, View v) {
        finalUser.setFullName(etName.getText().toString());
        finalUser.setDob(etDob.getText().toString());
        finalUser.setEmail2(etEmail.getText().toString());
        finalUser.setPhone(etPhone.getText().toString());
        userDao.update(finalUser);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Cập nhật thông tin thành công !");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private User setTextView(User_DAO userDao, String user_logined) {
        ArrayList<User> getUsers = (ArrayList<User>) userDao.listAll();

        User user = new User();
        for (int i = 0; i < getUsers.size(); i++) {
            if (getUsers.get(i).getUsername().equalsIgnoreCase(user_logined)) {
                user = getUsers.get(i);
                break;
            }
        }

        etName.setText(user.getFullName());
        etDob.setText(user.getDob());
        etEmail.setText(user.getEmail2());
        etPhone.setText(user.getPhone());
        etPassword.setText(user.getPassword());
        return user;
    }

    private void setIvBack() {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.editProfileFragment, profileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}