package com.example.foodsellappproject.ui.cart;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.ui.home.AddToCartFragment;
import com.example.foodsellappproject.common.MyOrderDetailRecyclerAdapter;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.entity.Comment;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.Order;
import com.example.foodsellappproject.entity.OrderDetail;
import com.example.foodsellappproject.ui.profile.MyOrderFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(String param1, String param2) {
        OrderDetailFragment fragment = new OrderDetailFragment();
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
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    private TextView tvOrderId, tvDeliveredStatus, tvOrderTime, tvName, tvAddress, tvPhone, tvTotalOfBill, tvBtnEvaluateAndComment;
    private RecyclerView rvOrderDetail;
    private ImageView ivBack;

    private void loadView(View view) {
        ivBack = view.findViewById(R.id.ivBack1);
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvDeliveredStatus = view.findViewById(R.id.tvDeliveredStatus);
        tvOrderTime = view.findViewById(R.id.tvOrderTime);
        tvName = view.findViewById(R.id.tvName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvTotalOfBill = view.findViewById(R.id.tvTotalOfBill);
        tvBtnEvaluateAndComment = view.findViewById(R.id.tvBtnEvaluateAndComment);
        rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
    }

    private DBConnection db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load data from layout
        loadView(view);

        //handle ivBack
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIvBack();
            }
        });
        //load database
        db = DBConnection.getDBConnection(getContext());
        final SharedPreferences pref = getContext().getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
        int orderId = pref.getInt("orderId", 0);
        tvOrderId.setText("Mã đơn hàng:   " + orderId);
        //Toast.makeText(getContext(), "orderId = " + orderId, Toast.LENGTH_SHORT).show();


        ArrayList<OrderDetail> getOrderDetailsByOrderId = (ArrayList<OrderDetail>) db.createOrderDetailDao().getOrderDetailsByOrderId(orderId);
        ArrayList<Food_Category> getFoodCategories = (ArrayList<Food_Category>) db.createFoodCategoryDAO().getFoodCategories();
        final ArrayList<Food> getFoods = (ArrayList<Food>) db.createFoodDAO().listAll();
        ArrayList<Food> getFoods1 = new ArrayList<>();
        //handle array list
        handleArrayList(getOrderDetailsByOrderId, getFoods, getFoods1);

        //handle rvOrderDetail
        final Bundle bundle = this.getArguments();
        MyOrderDetailRecyclerAdapter adapter = new MyOrderDetailRecyclerAdapter(getFoods1, getFoodCategories, getOrderDetailsByOrderId, bundle);
        rvOrderDetail.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvOrderDetail.setLayoutManager(manager);

        //handle text view
        if (orderId != 0) {
            ArrayList<Order> orderById = (ArrayList<Order>) db.createOrderDao().getOrderById(orderId);
            //load all address both of active and blocked
            ArrayList<Address> address = (ArrayList<Address>) db.createAddressDao().listAddressById(orderById.get(0).getAddressId());
            //Toast.makeText(getContext(), "AddressId = " + orderById.get(0).getAddressId(), Toast.LENGTH_SHORT).show();
            tvName.setText(address.get(0).getName());
            tvAddress.setText(address.get(0).getDetail());
            tvPhone.setText(address.get(0).getPhone());
            tvTotalOfBill.setText(String.valueOf(orderById.get(0).getTotalOfBillMoney()));
        } else {
            Toast.makeText(getContext(), "orderId = " + orderId, Toast.LENGTH_SHORT).show();
        }


        //handle tvBtnEvaluateAndComment

        SharedPreferences prefStatusOrder = getActivity().getSharedPreferences("statusOfOrder", Context.MODE_PRIVATE);
        String statusOfOrder = prefStatusOrder.getString("statusOfOrder", "delivered");
        if (statusOfOrder.equalsIgnoreCase("delivered")) {
            tvBtnEvaluateAndComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    if (bundle != null) {
                        boolean checked = bundle.getBoolean("checked");
                        final int foodId = bundle.getInt("foodId", 0);
                        final String nameOfFood = bundle.getString("nameOfFood", "");

                        if (checked == true) {
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.popup_evaluate_comment_product);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            final EditText etComment = dialog.getWindow().findViewById(R.id.etComment);
                            TextView tvBtnComment = dialog.getWindow().findViewById(R.id.tvBtnComment);

                            tvBtnComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Toast.makeText(getContext(), "foodId = " + foodId, Toast.LENGTH_SHORT).show();

                                    Comment comment = new Comment();
                                    SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                                    String user_logined = pref.getString("user_logined", "");
                                    comment.setUserId(user_logined);
                                    comment.setCommentContent(etComment.getText().toString());
                                    comment.setCommentTime(new Date());
                                    comment.setFood_id(foodId);
                                    DBConnection.getDBConnection(getContext()).createCommentDao().insert(comment);


                                    //move to that food
                                    SharedPreferences pref1 = v.getContext().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref1.edit();
                                    editor.putString("nameOfFood", nameOfFood);
                                    editor.commit();

                                    AddToCartFragment addToCartFragment = new AddToCartFragment();
                                    FragmentManager manager = getFragmentManager();
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.replace(R.id.orderDetailFragment, addToCartFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                    dialog.dismiss();
                                    builder.setMessage("Cảm ơn vì sự đánh giá và góp ý của bạn.");
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            });
                        } else {
                            builder.setMessage("Vui lòng chọn 1 sản phẩm để đánh giá.");
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }

                    } else {
                        builder.setMessage("Vui lòng chọn 1 sản phẩm để đánh giá.");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                }
            });
        } else { // handle delivering

            tvDeliveredStatus.setText("Đang chờ xử lý.");
            tvDeliveredStatus.setTextColor(Color.RED);
            tvBtnEvaluateAndComment.setText("Hủy đơn hàng");
            tvBtnEvaluateAndComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog();
                }
            });
        }


    }


    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không ?");
        builder.setIcon(android.R.drawable.star_big_on);


        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = DBConnection.getDBConnection(getContext());
                final SharedPreferences pref = getContext().getSharedPreferences("handleCbComment", Context.MODE_PRIVATE);
                int orderId = pref.getInt("orderId", 0);
                ArrayList<Order> getOrderById = (ArrayList<Order>) db.createOrderDao().getOrderById(orderId);
                getOrderById.get(0).setStatusOfOrder("canceled");
                db.createOrderDao().update(getOrderById.get(0));

                Toast.makeText(getContext(), "Hủy đơn hàng thành công.", Toast.LENGTH_SHORT).show();
                setIvBack();
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //textView.setText("Button cancel is clicked");
            }
        });

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void handleArrayList(ArrayList<OrderDetail> getOrderDetailsByOrderId, ArrayList<Food> getFoods, ArrayList<Food> getFoods1) {

        //  String a = "";
        for (int i = 0; i < getFoods.size(); i++) {
            if (i < 30) {
                getFoods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else{
                getFoods.get(i).setImageId(getResId("h1", R.drawable.class));
            }


            for (int j = 0; j < getOrderDetailsByOrderId.size(); j++) {
                if (getFoods.get(i).getId() == getOrderDetailsByOrderId.get(j).getFood_id()) {
                    getFoods1.add(getFoods.get(i));
                    //a += " " + getFoods.get(i).getId();
                    break;
                }
            }
        }
        //  Toast.makeText(getContext(), "a = " + a + "SizeCart=" + getCarts1.size() + " and SizeFood=" + getFoods1.size(), Toast.LENGTH_SHORT).show();

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

    private void setIvBack() {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.orderDetailFragment, myOrderFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}