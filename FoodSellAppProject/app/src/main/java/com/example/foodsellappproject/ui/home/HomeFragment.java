package com.example.foodsellappproject.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.common.FoodCategoryRecyclerAdapter;
import com.example.foodsellappproject.common.FoodRecyclerAdapter;
import com.example.foodsellappproject.controller.HomeActivity;
import com.example.foodsellappproject.dao.Food_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Food;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class HomeFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private DBConnection db;
    private Food_DAO foodDAO;
    private ArrayList<Food> foods = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageView ivFilter;
    private EditText etSearch;
    private Button btnSearch;

    private void loadView(View view) {
        ivFilter = view.findViewById(R.id.ivFilter);
        recyclerView = view.findViewById(R.id.rvFoods);
        etSearch = view.findViewById(R.id.etSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //load view from layout
        loadView(view);

        //load data from room
        db = DBConnection.getDBConnection(getContext());
        foodDAO = db.createFoodDAO();
        foods = (ArrayList<Food>) foodDAO.listAll();

        for (int i = 0; i < foods.size(); i++) {
            if (i < 30) {
                foods.get(i).setImageId(getResId("image" + (i + 1), R.drawable.class));
            } else {
                foods.get(i).setImageId(getResId("h1", R.drawable.class));
            }
        }

        FoodRecyclerAdapter adapter = new FoodRecyclerAdapter(foods, (HomeActivity) getActivity());
        recyclerView.setAdapter(adapter);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //recyclerView.setLayoutManager(layoutManager);

        //handle btnSearch
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch();
            }
        });

        //handle ivFilter
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFilter();
            }
        });

        //handle btnApply from screen of Filter
        handleBtnApplyFromScreenOfFilter();


    }

    private void handleBtnApplyFromScreenOfFilter() {
        SharedPreferences pref = getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
        int applied = pref.getInt("applied", 0);
        if (applied == 1) {
            String etPriceFrom = pref.getString("etPriceFrom", "");
            String etPriceTo = pref.getString("etPriceTo", "");
            int etPriceFrom1 = 0;
            int etPriceTo1 = 0;
            if (!etPriceFrom.trim().isEmpty()) etPriceFrom1 = Integer.parseInt(etPriceFrom);
            if (!etPriceTo.trim().isEmpty()) etPriceTo1 = Integer.parseInt(etPriceTo);

            ArrayList<Food> foods1 = new ArrayList<>();
            for (int i = 0; i < foods.size(); i++) {

                int length = FoodCategoryRecyclerAdapter.ViewHolder.getFoodCategoryIdsFromBtnApply.size();
                for (int j = 0; j < length; j++) {

                    if (foods.get(i).getId_type() == FoodCategoryRecyclerAdapter.ViewHolder.getFoodCategoryIdsFromBtnApply.get(j)
                            && Integer.parseInt(foods.get(i).getPrice().trim()) >= etPriceFrom1
                            && Integer.parseInt(foods.get(i).getPrice().trim()) <= etPriceTo1 && etPriceTo1 != 0) {
                        foods1.add(foods.get(i));
                        break;
                    } else if (Integer.parseInt(foods.get(i).getPrice().trim()) > etPriceTo1 && etPriceTo1 != 0) {
                        break;
                    }
                    if (foods.get(i).getId_type() == FoodCategoryRecyclerAdapter.ViewHolder.getFoodCategoryIdsFromBtnApply.get(j)
                            && Integer.parseInt(foods.get(i).getPrice().trim()) >= etPriceFrom1) {
                        foods1.add(foods.get(i));
                        break;
                    }
                    if (foods.get(i).getId_type() == FoodCategoryRecyclerAdapter.ViewHolder.getFoodCategoryIdsFromBtnApply.get(j)
                            && etPriceFrom1 == 0 && etPriceTo1 == 0) {
                        foods1.add(foods.get(i));
                        break;
                    }
                }
            }
            FoodRecyclerAdapter adapterFilter = new FoodRecyclerAdapter(foods1, (HomeActivity) getActivity());
            recyclerView.setAdapter(adapterFilter);
        } else if (applied == 2) { //this case: user only enter price which not choose food category
            String etPriceFrom = pref.getString("etPriceFrom", "");
            String etPriceTo = pref.getString("etPriceTo", "");
            int etPriceFrom1 = 0;
            int etPriceTo1 = 0;
            if (!etPriceFrom.trim().isEmpty()) etPriceFrom1 = Integer.parseInt(etPriceFrom);
            if (!etPriceTo.trim().isEmpty()) etPriceTo1 = Integer.parseInt(etPriceTo);

            ArrayList<Food> foods1 = new ArrayList<>();
            for (int i = 0; i < foods.size(); i++) {

                if (Integer.parseInt(foods.get(i).getPrice().trim()) >= etPriceFrom1
                        && Integer.parseInt(foods.get(i).getPrice().trim()) <= etPriceTo1 && etPriceTo1 != 0) {
                    foods1.add(foods.get(i));
                }
                if (Integer.parseInt(foods.get(i).getPrice().trim()) >= etPriceFrom1
                        && etPriceTo1 == 0) {
                    foods1.add(foods.get(i));
                }

            }
            FoodRecyclerAdapter adapterFilter = new FoodRecyclerAdapter(foods1, (HomeActivity) getActivity());
            recyclerView.setAdapter(adapterFilter);
        }
    }

    private void ivFilter() {
        FilterFragment filterFragment = new FilterFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, filterFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void btnSearch() {
        String textSearch = etSearch.getText().toString().trim();
        int checkMark = 0;
        for (int i = 0; i < foods.size(); i++) {
            if (foods.get(i).getName().toUpperCase().contains(textSearch.toUpperCase())) {
                checkMark = 1;
                break;
            }
        }
        if (checkMark == 0) {
            FoodRecyclerAdapter adapterFilter = new FoodRecyclerAdapter(new ArrayList<Food>(), (HomeActivity) getActivity());
            recyclerView.setAdapter(adapterFilter);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            // builder.setTitle("T")
            builder.setMessage("Không tìm thấy");
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {// if found
            ArrayList<Food> foods1 = new ArrayList<>();
            for (int i = 0; i < foods.size(); i++) {
                if (foods.get(i).getName().toUpperCase().contains(textSearch.toUpperCase())) {
                    foods1.add(foods.get(i));
                }
            }
            if (!textSearch.trim().isEmpty()) {
                FoodRecyclerAdapter adapterFilter = new FoodRecyclerAdapter(foods1, (HomeActivity) getActivity());
                recyclerView.setAdapter(adapterFilter);
            }
        }
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