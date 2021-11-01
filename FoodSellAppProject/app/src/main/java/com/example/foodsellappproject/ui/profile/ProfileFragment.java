package com.example.foodsellappproject.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.ProfileRecyclerAdapter;
import com.example.foodsellappproject.controller.HomeActivity;
import com.example.foodsellappproject.controller.LoginActivity;
import com.example.foodsellappproject.dao.User_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.entity.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private TextView tvName, tvNickName, tvBtnLogout;
    private ImageView ivAvt;
    private RecyclerView rvProfile;

    private void loadView(View view) {
        tvName = view.findViewById(R.id.tvName);
        tvNickName = view.findViewById(R.id.tvNickName);
        tvBtnLogout = view.findViewById(R.id.tvBtnLogout);
        ivAvt = view.findViewById(R.id.ivAvt);
        rvProfile = view.findViewById(R.id.rvProfile);
    }

    private DBConnection db;
    private User_DAO userDao;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load view from layout
        loadView(view);

        //handle button logout
        logout();

        //load data from room
        db = DBConnection.getDBConnection(getContext());
        userDao = db.createUserDAO();
        ArrayList<User> getUsers = (ArrayList<User>) userDao.listAll();

        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_logined = pref.getString("username", "");

        int mark = 0;
        for (int i = 0; i < getUsers.size(); i++) {
            mark++;
            getUsers.get(i).setAvtImageId(getResId("ava" + mark, R.drawable.class));
            if (mark == 4) mark = 0;
        }

        User user = new User();
        for (int i = 0; i < getUsers.size(); i++) {
            if (getUsers.get(i).getUsername().equalsIgnoreCase(user_logined)) {
                user = getUsers.get(i);
                break;
            }
        }
        ivAvt.setImageResource(user.getAvtImageId());
        tvName.setText(user.getFullName());
        tvNickName.setText(user.getFullName());
        ArrayList<Order> getOrdersByUserId = new ArrayList<>();
        ArrayList<Order> getOrders = (ArrayList<Order>) db.createOrderDao().listAllByStatusOfOrderNotCanceled("delivered","delivering");
        ArrayList<OrderDetail> getOrderDetailsByUserId = (ArrayList<OrderDetail>) db.createOrderDetailDao().listAllByUserId(user_logined);
        ArrayList<Address> getAddressesByUserId = (ArrayList<Address>) db.createAddressDao().listAllByUserId1(user_logined,"active");
        for (int i = 0; i < getOrders.size(); i++) {
            for (int j = 0; j < getOrderDetailsByUserId.size(); j++) {
                if (getOrders.get(i).getId() == getOrderDetailsByUserId.get(j).getOrder_id()) {
                    getOrdersByUserId.add(getOrders.get(i));
                    break;
                }
            }
        }

        //handle rvProfile
        ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getOrdersByUserId, getAddressesByUserId);
        rvProfile.setAdapter(adapter);
        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void logout() {
        tvBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


//    private ProfileViewModel profileViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        profileViewModel =
//                ViewModelProviders.of(this).get(ProfileViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        final TextView textView = root.findViewById(R.id.text_profile);
//        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
}