package com.example.foodsellappproject.ui.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.AddedFoodToCartRecyclerAdapter;
import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.dao.Food_Category_DAO;
import com.example.foodsellappproject.dao.Food_DAO;
import com.example.foodsellappproject.dao.OrderDetail_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.Food_Category;
import com.example.foodsellappproject.entity.OrderDetail;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
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
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        return inflater.inflate(R.layout.fragment_cart, container, false);
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

    private RecyclerView rvListOfAddedFood;
    private DBConnection db;
    private Food_DAO foodDAO;
    private Food_Category_DAO foodCategoryDAO;
    private Cart_DAO cartDAO;
    private TextView tvTotalOfMoney, tvBtnBuy;

    private void loadView(View view) {
        rvListOfAddedFood = view.findViewById(R.id.rvListOfAddedFood);
        tvTotalOfMoney = view.findViewById(R.id.tvTotalOfMoney);
        tvBtnBuy = view.findViewById(R.id.tvBtnBuy);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //load view from layout
        loadView(view);

        //load data from room
        db = DBConnection.getDBConnection(getContext());
        foodDAO = db.createFoodDAO();
        foodCategoryDAO = db.createFoodCategoryDAO();
        cartDAO = db.createCartDao();


        ArrayList<Food> getFoods = (ArrayList<Food>) foodDAO.listAll();
        ArrayList<Food_Category> getFoodCategories = (ArrayList<Food_Category>) foodCategoryDAO.getFoodCategories();
        ArrayList<Cart> getCarts = (ArrayList<Cart>) cartDAO.listAll();
        ArrayList<Cart> getCarts1 = new ArrayList<>();
        ArrayList<Food> getFoods1 = new ArrayList<>();

        //handle arrayList
        handleArrayList(getCarts, getCarts1, getFoods, getFoods1);

        //handle rvListOfAddFood
        setRvListOfAddedFood(getFoods1, getFoodCategories, getCarts1);

        //handle totalOfMoney
        double total = getTotalOfMoney(getCarts1, getFoods1, 0);

        //handle btnBuy
        final double finalTotal = total;
        tvBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTvBtnBuy(finalTotal);
            }
        });
    }

    private void handleArrayList(ArrayList<Cart> getCarts, ArrayList<Cart> getCarts1,
                                 ArrayList<Food> getFoods, ArrayList<Food> getFoods1) {
        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_logined = pref.getString("user_logined", "");

        for (int i = 0; i < getCarts.size(); i++) {
            if (user_logined.equalsIgnoreCase(getCarts.get(i).getUser_id())) {
                getCarts1.add(getCarts.get(i));
            }
        }
        //  String a = "";

        for (int i = 0; i < getFoods.size(); i++) {
            if (i < 30) {
                getFoods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else {
                getFoods.get(i).setImageId(getResId("h1", R.drawable.class));
            }
            for (int j = 0; j < getCarts1.size(); j++) {
                if (getFoods.get(i).getId() == getCarts1.get(j).getFood_id()) {
                    getFoods1.add(getFoods.get(i));
                    //a += " " + getFoods.get(i).getId();
                    break;
                }
            }
        }
        //  Toast.makeText(getContext(), "a = " + a + "SizeCart=" + getCarts1.size() + " and SizeFood=" + getFoods1.size(), Toast.LENGTH_SHORT).show();

    }

    private void setRvListOfAddedFood(ArrayList<Food> getFoods1, ArrayList<Food_Category> getFoodCategories, ArrayList<Cart> getCarts1) {
        AddedFoodToCartRecyclerAdapter adapter = new AddedFoodToCartRecyclerAdapter(getFoods1, getFoodCategories, getCarts1);
        rvListOfAddedFood.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvListOfAddedFood.setLayoutManager(layoutManager);
    }

    private double getTotalOfMoney(ArrayList<Cart> getCarts1, ArrayList<Food> getFoods1, double total) {
        for (int i = 0; i < getCarts1.size(); i++) {
            for (int j = 0; j < getFoods1.size(); j++) {
                if (getCarts1.get(i).getFood_id() == getFoods1.get(j).getId()) {
                    total += Double.parseDouble(getFoods1.get(j).getPrice()) * getCarts1.get(i).getQuantity();
                    break;
                }
            }
        }
        tvTotalOfMoney.setText(String.valueOf(total) + " đ");
        SharedPreferences pref1 = getActivity().getSharedPreferences("TotalOfOrderMoney", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref1.edit();
        editor.putFloat("totalOfOrder", (float) total);
        editor.commit();
        return total;
    }

    private void setTvBtnBuy(double finalTotal) {
        if (finalTotal > 0) {

            CheckOutFragment checkOutFragment = new CheckOutFragment();
            //  FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.cartFragment, checkOutFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Vui lòng thêm sản phẩm vào giỏ hàng.");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}