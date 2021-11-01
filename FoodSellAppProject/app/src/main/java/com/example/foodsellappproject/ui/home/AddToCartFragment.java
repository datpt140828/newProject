package com.example.foodsellappproject.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.CommentRecyclerViewAdapter;
import com.example.foodsellappproject.common.FoodYouCanLikeRecyclerAdapter;
import com.example.foodsellappproject.dao.Cart_DAO;
import com.example.foodsellappproject.dao.Food_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Cart;
import com.example.foodsellappproject.entity.Comment;
import com.example.foodsellappproject.entity.Food;
import com.example.foodsellappproject.entity.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddToCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddToCartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddToCartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddToCartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddToCartFragment newInstance(String param1, String param2) {
        AddToCartFragment fragment = new AddToCartFragment();
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
        return inflater.inflate(R.layout.fragment_add_to_cart, container, false);
    }

    private TextView tvNameOfFood1, tvDescription, tvBtnAddToCart;
    private ImageView ivFood;
    private RecyclerView rvYouCanLike, rvComment;
    private DBConnection db;
    private Food_DAO foodDAO;
    private Cart_DAO cartDAO;
    private ArrayList<Food> getFoods = new ArrayList<>();

    private void loadView(View view) {
        tvNameOfFood1 = view.findViewById(R.id.tvNameOfFood1);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivFood = view.findViewById(R.id.ivFood);
        tvBtnAddToCart = view.findViewById(R.id.tvBtnAddToCart);
        rvYouCanLike = view.findViewById(R.id.rvYouCanLike);
        rvComment = view.findViewById(R.id.rvComment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load view from layout
        loadView(view);

        //load data from room
        db = DBConnection.getDBConnection(getContext());
        foodDAO = db.createFoodDAO();
        getFoods = (ArrayList<Food>) foodDAO.listAll();
        for (int i = 0; i < getFoods.size(); i++) {
            if (i < 30) {
                getFoods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else {
                getFoods.get(i).setImageId(getResId("h1", R.drawable.class));
            }
        }

        SharedPreferences pref = getActivity().getSharedPreferences("chooseFood", Context.MODE_PRIVATE);
        String nameOfFood = pref.getString("nameOfFood", "");
        if (!nameOfFood.trim().isEmpty()) {
            int id_type = 0;
            int food_id = 0;
            //handle some content of textview
            for (int i = 0; i < getFoods.size(); i++) {
                if (nameOfFood.equalsIgnoreCase(getFoods.get(i).getName())) {
                    tvNameOfFood1.setText(getFoods.get(i).getName());

                    if (i < 30) {
                        ivFood.setImageResource(getResId("image" + (i + 1), R.drawable.class));
                    } else {
                        ivFood.setImageResource(getResId("h1", R.drawable.class));
                    }

                    tvDescription.setText(getFoods.get(i).getDescription());
                    id_type = getFoods.get(i).getId_type();
                    food_id = i + 1;
                    break;
                }
            }
            //handle RecyclerView YouCanLike
            handleRecyclerViewYouCanLike(id_type, nameOfFood, getFoods);

            //handle tvBtnAddToCart
            final int finalFood_id = food_id;
            tvBtnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getFoods.get(finalFood_id-1).getStatusOfFood().equalsIgnoreCase("deleted")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Mặt hàng này hiện tại đang hết hàng.");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else if (getFoods.get(finalFood_id-1).getStatusOfFood().equalsIgnoreCase("active")) {
                        TvBtnAddToCart(finalFood_id);
                    }
                }
            });

            // handle rvComment

            ArrayList<Comment> getCommentsByFoodId = (ArrayList<Comment>) db.createCommentDao().getCommentsByFoodId(food_id);
            ArrayList<User> getUsers = (ArrayList<User>) db.createUserDAO().listAll();
            int mark = 0;
            for (int i = 0; i < getUsers.size(); i++) {
                mark++;
                getUsers.get(i).setAvtImageId(getResId("ava" + mark, R.drawable.class));
                if (mark == 4) mark = 0;
            }
            CommentRecyclerViewAdapter adapter = new CommentRecyclerViewAdapter(getCommentsByFoodId, getUsers);
            rvComment.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            rvComment.setLayoutManager(manager);
        }
    }

    private void handleRecyclerViewYouCanLike(int id_type, String nameOfFood, ArrayList<Food> getFoods) {
        final ArrayList<Food> foods = new ArrayList<>();
        for (int i = 0; i < getFoods.size(); i++) {
            if (id_type == getFoods.get(i).getId_type() && !nameOfFood.equalsIgnoreCase(getFoods.get(i).getName())) {
                foods.add(getFoods.get(i));
            }
        }
        FoodYouCanLikeRecyclerAdapter adapter = new FoodYouCanLikeRecyclerAdapter(foods);
        rvYouCanLike.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvYouCanLike.setLayoutManager(layoutManager);
    }

    private void TvBtnAddToCart(int finalFood_id) {
        SharedPreferences pref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String user_logined = pref.getString("user_logined", "");

        cartDAO = db.createCartDao();

        ArrayList<Cart> getCarts = (ArrayList<Cart>) cartDAO.listAll();
        int markUpdate = 0;
        int quantity = 1;
        for (int i = 0; i < getCarts.size(); i++) {
            if (user_logined.equalsIgnoreCase(getCarts.get(i).getUser_id()) && finalFood_id == getCarts.get(i).getFood_id()) {
                markUpdate = 1;
                quantity = getCarts.get(i).getQuantity() + 1;
                break;
            }
        }
        Cart cart = new Cart();
        cart.setUser_id(user_logined);
        cart.setFood_id(finalFood_id);
        cart.setQuantity(quantity);
        if (markUpdate == 1) {
            cartDAO.updateCart(quantity, user_logined, finalFood_id);
            Toast.makeText(getContext(), "Update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Insert", Toast.LENGTH_SHORT).show();
            cartDAO.insert(cart);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Đã thêm sản phẩm vào giỏ hàng thành công.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
}