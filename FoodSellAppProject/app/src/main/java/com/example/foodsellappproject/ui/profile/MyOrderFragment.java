package com.example.foodsellappproject.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.MyOrdersRecyclerAdapter;
import com.example.foodsellappproject.dao.Order_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrderFragment newInstance(String param1, String param2) {
        MyOrderFragment fragment = new MyOrderFragment();
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
        return inflater.inflate(R.layout.fragment_my_order, container, false);
    }


    private ImageView ivBack;
    private TextView tvBtnDelivered, tvBtnDelivering;
    private RecyclerView rvOrdersByUserId;

    private void loadView(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        tvBtnDelivered = view.findViewById(R.id.tvBtnDelivered);
        tvBtnDelivering = view.findViewById(R.id.tvBtnDelivering);
        rvOrdersByUserId = view.findViewById(R.id.rvOrdersByUserId);

    }

    private DBConnection db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load view fromm layout
        loadView(view);

        //handle ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIvBack();
            }
        });


        //handle both btn delivered and delivering
        SharedPreferences prefStatusOrder = getActivity().getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editorStatusOrder = prefStatusOrder.edit();

        String statusOfOrder = prefStatusOrder.getString("statusOfOrder","delivered");


        if (statusOfOrder.equalsIgnoreCase("delivered")) {
            tvBtnDelivered.setTextColor(Color.WHITE);
            tvBtnDelivered.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dagiaohang));
            tvBtnDelivering.setTextColor(Color.RED);
            tvBtnDelivering.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.chuagiaohang));
        } else {
            tvBtnDelivering.setTextColor(Color.WHITE);
            tvBtnDelivering.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dagiaohang));
            tvBtnDelivered.setTextColor(Color.RED);
            tvBtnDelivered.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.chuagiaohang));
        }

        tvBtnDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorStatusOrder.putString("statusOfOrder", "delivered");
                editorStatusOrder.commit();

                MyOrderFragment myOrderFragment = new MyOrderFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.myOrderFragment,myOrderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tvBtnDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editorStatusOrder.putString("statusOfOrder", "delivering");
                editorStatusOrder.commit();

                MyOrderFragment myOrderFragment = new MyOrderFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.myOrderFragment,myOrderFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
   //     String statusOfOrder = prefStatusOrder.getString("statusOfOrder", "delivered");
        Toast.makeText(getContext(), "status = " + statusOfOrder, Toast.LENGTH_SHORT).show();

        //handle rvOrdersByUserId
        db = DBConnection.getDBConnection(getContext());
        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_logined = pref.getString("username", "");
        ArrayList<Integer> getOrderIdByUserId = (ArrayList<Integer>) db.createOrderDetailDao().listOrderIdByUserId(user_logined);
        ArrayList<Order> getOrders = (ArrayList<Order>) db.createOrderDao().listAllByStatusOfOrder(statusOfOrder);
        ArrayList<Order> getOrders1 = new ArrayList<>();
        for (int i = 0; i < getOrders.size(); i++) {
            for (int j = 0; j < getOrderIdByUserId.size(); j++) {
                if (getOrders.get(i).getId() == getOrderIdByUserId.get(j)) {
                    getOrders1.add(getOrders.get(i));
                    break;
                }
            }
        }
        ArrayList<OrderDetail> getOrderDetails = (ArrayList<OrderDetail>) db.createOrderDetailDao().listAll();
        MyOrdersRecyclerAdapter adapter = new MyOrdersRecyclerAdapter(getOrders1, getOrderDetails);
        rvOrdersByUserId.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvOrdersByUserId.setLayoutManager(manager);

    }

    private void setIvBack() {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.myOrderFragment, profileFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}