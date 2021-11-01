package com.example.foodsellappproject.ui.cart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.foodsellappproject.common.AddressRecyclerViewAdapter;
import com.example.foodsellappproject.dao.Address_DAO;
import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.dao.OrderDetail_DAO;
import com.example.foodsellappproject.dao.Order_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.service.CheckDeliveringService;
import com.example.foodsellappproject.service.StartedService;
import com.example.foodsellappproject.ui.profile.MyOrderFragment;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckOutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckOutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckOutFragment newInstance(String param1, String param2) {
        CheckOutFragment fragment = new CheckOutFragment();
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
        return inflater.inflate(R.layout.fragment_check_out, container, false);
    }

    private ImageView ivBack;
    private TextView tvBtnAddNewAddress, tvBtnOrder, tvTotalOfOrder, tvMoneyOfShip, tvTotalOfBill;
    private RecyclerView rvAddress;
    private DBConnection db;
    private TextView tvCheckOutService;

    private void loadView(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        tvBtnAddNewAddress = view.findViewById(R.id.tvBtnAddNewAddress);
        tvMoneyOfShip = view.findViewById(R.id.tvMoneyOfShip);
        rvAddress = view.findViewById(R.id.rvOrderDetail);
        tvCheckOutService = view.findViewById(R.id.tvCheckOutService);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //register service
//        IntentFilter filter1 = new IntentFilter("CALCULATE_TIME_DELIVERING");
//        this.getActivity().registerReceiver(receiver1, filter1);

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
        if (getAddresses.size() > 0) {
            Bundle bundle = this.getArguments();
            //handle rvAddress
            AddressRecyclerViewAdapter adapter = new AddressRecyclerViewAdapter(getAddresses1, bundle);
            rvAddress.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rvAddress.setLayoutManager(linearLayoutManager);
        }


        final double moneyOfShip = 20000;
        tvMoneyOfShip.setText(moneyOfShip + " đ");

        //get totalOfBill
        SharedPreferences pref1 = getActivity().getSharedPreferences("TotalOfOrderMoney", Context.MODE_PRIVATE);
        double totalOfOrder = pref1.getFloat("totalOfOrder", 0);
        tvTotalOfOrder = view.findViewById(R.id.tvAddress);
        tvTotalOfOrder.setText(totalOfOrder + " đ");

        tvTotalOfBill = view.findViewById(R.id.tvTotalOfBill);
        final double totalOfBill = moneyOfShip + totalOfOrder;
        tvTotalOfBill.setText(totalOfBill + " đ");


        //handle tvBtnAddNewAddress
        final Dialog dialog = new Dialog(getActivity());
        tvBtnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvBtnAddNewAddress(dialog, user_logined);
            }
        });


        //handle tvBtnOrder
        tvBtnOrder = view.findViewById(R.id.tvBtnEvaluateAndComment);
        final Bundle bundle = getArguments();
        tvBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvBtnOrder(v, bundle, user_logined, moneyOfShip, totalOfBill);
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
                // Bundle b = new Bundle();
                //  b.putInt("Added", 1);
                CheckOutFragment checkOutFragment = new CheckOutFragment();
                //checkOutFragment.setArguments(b);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.checkOutFragment, checkOutFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
    }

    //private ArrayList<Integer> getLastOrderIdJustInserted = new ArrayList<>();
    private int getLastOrderIdJustInserted = 0;

    private void setTvBtnOrder(View v, Bundle bundle, String user_logined, double moneyOfShip, double totalOfBill) {
        if (bundle != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            if (bundle.getBoolean("checked", false) == false) {
                builder.setMessage("Vui lòng chọn một địa chỉ.");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {

                Cart_DAO cartDAO = db.createCartDao();
                ArrayList<Cart> getCarts = (ArrayList<Cart>) cartDAO.listAll();
                final ArrayList<Cart> getCarts1 = new ArrayList<>();
                for (int i = 0; i < getCarts.size(); i++) {
                    if (user_logined.equalsIgnoreCase(getCarts.get(i).getUser_id())) {
                        getCarts1.add(getCarts.get(i));
                    }
                }

                //handle Insert Order and OrderDetail
                OrderDetail_DAO orderDetailDao = db.createOrderDetailDao();
                //orderDetailDao.deleteAll();
                ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) orderDetailDao.listAll();

                Toast.makeText(getContext(), "SizeCart = " + getCarts1.size() + " SizeOrderDetail = " + orderDetails.size(), Toast.LENGTH_SHORT).show();

                //   bundle.putBoolean("checked",false);
                Order_DAO orderDao = db.createOrderDao();
                Order order = new Order();
                order.setMoneyOfShip(moneyOfShip);
                order.setTotalOfBillMoney(totalOfBill);
                order.setOrderTime(new Date());
                order.setStatusOfOrder("delivering");
                SharedPreferences pref = v.getContext().getSharedPreferences("handleAddress", Context.MODE_PRIVATE);
                int addressId = pref.getInt("addressId", 0);
                order.setAddressId(addressId);

                orderDao.insert(order);
                //String s=   new SimpleDateFormat("hh:mm:ss").format(new Date());

                int getOrderLastId = orderDao.getLastId();

                getLastOrderIdJustInserted = getOrderLastId;
                for (int i = 0; i < getCarts1.size(); i++) {

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setUser_id(getCarts1.get(i).getUser_id());
                    orderDetail.setFood_id(getCarts1.get(i).getFood_id());
                    orderDetail.setQuantity(getCarts1.get(i).getQuantity());
                    orderDetail.setOrder_id(getOrderLastId);
                    orderDetailDao.insert(orderDetail);

                }
                Toast.makeText(getContext(), "SizeCart = " + getCarts1.size() + " SizeOrderDetail = " + ((ArrayList<OrderDetail>) orderDetailDao.listAll()).size(), Toast.LENGTH_SHORT).show();

                //handle service to calculate time of delivering -> delivered
//                Intent intent1 = new Intent(this.getActivity(), StartedService.class);
//                this.getActivity().startService(intent1);// test for fun

                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Đặt hàng thành công !");
                AlertDialog alertDialog = builder1.create();
                alertDialog.show();
                alertDialog.dismiss();

                MyOrderFragment myOrderFragment = new MyOrderFragment();
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.checkOutFragment, myOrderFragment)
                        .addToBackStack(null)
                        .commit();


//                Intent intent2 = new Intent(this.getActivity(), CheckDeliveringService.class);
//                this.getActivity().startService(intent2);// handle delivering

            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Vui lòng chọn một địa chỉ.");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private BroadcastReceiver receiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int percent = intent.getIntExtra("percent", -1);
            if (percent <= 100) {
                tvBtnOrder.setText("Đang xử lý: " + percent + "%");
            }
            if (percent == 100) {
                tvBtnOrder.setText("Hoàn thành");

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Đặt hàng thành công !");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.dismiss();
            }
            if (percent == 101) {
                MyOrderFragment myOrderFragment = new MyOrderFragment();
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.checkOutFragment, myOrderFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    };
//    private BroadcastReceiver receiver2 = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int percent = intent.getIntExtra("percent", -1);
//            //tvCheckOutService.setText("Đang xử lý: " + percent + "%");
//            if (percent == 100) {
//                DBConnection db = DBConnection.getDBConnection(context);
//                ArrayList<Order> getOrders = (ArrayList<Order>)db.createOrderDao().listAllByStatusOfOrder("delivering");
//                for (int i = 0; i <getOrders.size() ; i++) {
//                    if(getOrders.get(i).getId()== getLastOrderIdJustInserted){
//                        getOrders.get(i).setStatusOfOrder("delivered");
//                        db.createOrderDao().update(getOrders.get(i));
//                        Toast.makeText(context, "delivering -> delivered", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                }
//
//            }
//        }
//    };

    private void setIvBack() {
        CartFragment cartFragment = new CartFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.checkOutFragment, cartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}