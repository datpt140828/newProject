package com.example.foodsellappproject.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.AddressProfileRecyclerAdapter;
import com.example.foodsellappproject.controller.HomeActivity;
import com.example.foodsellappproject.dao.Address_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Address;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    private ImageView ivBack;
    private RecyclerView rvAddress;
    private TextView tvBtnAddNewAddress;

    private void loadView(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        rvAddress = view.findViewById(R.id.rvOrderDetail);
        tvBtnAddNewAddress = view.findViewById(R.id.tvBtnAddNewAddress);
    }

    private DBConnection db;

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
        db = DBConnection.getDBConnection(getContext());
        Address_DAO addressDao = db.createAddressDao();

        ArrayList<Address> getAddresses = (ArrayList<Address>) addressDao.listAll("active");

        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        final String user_logined = pref.getString("user_logined", "");
        ArrayList<Address> getAddresses1 = new ArrayList<>();
        for (int i = 0; i < getAddresses.size(); i++) {
            if (getAddresses.get(i).getUser_id().equalsIgnoreCase(user_logined)) {
                getAddresses1.add(getAddresses.get(i));
            }
        }

        AddressProfileRecyclerAdapter adapter = new AddressProfileRecyclerAdapter(getAddresses1, (HomeActivity) getActivity());
        rvAddress.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvAddress.setLayoutManager(linearLayoutManager);

        //handle tvBtnAddNewAddress
        final Dialog dialog = new Dialog(getActivity());
        tvBtnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "get here", Toast.LENGTH_SHORT).show();
                setTvBtnAddNewAddress(dialog, user_logined);
            }
        });

    }
    private void setTvBtnAddNewAddress(final Dialog dialog, final String user_logined) {
        dialog.setContentView(R.layout.popup_add_new_address);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        final EditText etName, etAddress, etPhone;
        final TextView tvBtnCancel, tvBtnAddNew;
        etName = dialog.getWindow().findViewById(R.id.etOldPassword);
        etAddress = dialog.getWindow().findViewById(R.id.etNewPassword);
        etPhone = dialog.getWindow().findViewById(R.id.etAgainNewPassword);
        tvBtnCancel = dialog.getWindow().findViewById(R.id.tvBtnCancel);
        tvBtnAddNew = dialog.getWindow().findViewById(R.id.tvBtnComment);

        //handle btnCancel
        tvBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //handle btnAdd
        tvBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Address_DAO address_dao = db.createAddressDao();
                Address address = new Address();
                address.setUser_id(user_logined);
                address.setDetail(etAddress.getText().toString());
                address.setPhone(etPhone.getText().toString());
                address.setName(etName.getText().toString());
                address.setStatusOfDelete("active");
                address_dao.insert(address);

                Toast.makeText(getContext(), "SizeAddress: " + ((ArrayList<Address>) address_dao.listAll("active")).size(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                AddressFragment addressFragment = new AddressFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.addressProfileFragment, addressFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }


    private void setIvBack(){
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.addressProfileFragment, profileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}