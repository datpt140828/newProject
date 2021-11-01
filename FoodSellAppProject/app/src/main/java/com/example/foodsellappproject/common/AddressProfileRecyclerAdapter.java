package com.example.foodsellappproject.common;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.controller.HomeActivity;
import com.example.foodsellappproject.dao.Address_DAO;
import com.example.foodsellappproject.database.DBConnection;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.ui.profile.AddressFragment;

import java.util.ArrayList;

public class AddressProfileRecyclerAdapter extends RecyclerView.Adapter<AddressProfileRecyclerAdapter.ViewHolder> {

    private ArrayList<Address> getAddresses;
    private HomeActivity activity;

    public AddressProfileRecyclerAdapter(ArrayList<Address> getAddresses, HomeActivity activity) {
        this.getAddresses = getAddresses;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AddressProfileRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_profile_list_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressProfileRecyclerAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(getAddresses.get(position).getName());
        holder.tvAddress.setText(getAddresses.get(position).getDetail());
        holder.tvPhone.setText(getAddresses.get(position).getPhone());

        //handle tvBtnModify
        holder.tvBtnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(v.getContext());
//                SharedPreferences pref = v.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
//                final String user_logined = pref.getString("user_logined", "");
                Toast.makeText(v.getContext(), "get here", Toast.LENGTH_SHORT).show();

                dialog.setContentView(R.layout.popup_add_new_address);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                final EditText etName, etAddress, etPhone;
                final TextView tvBtnCancel, tvBtnSave;
                etName = dialog.getWindow().findViewById(R.id.etOldPassword);
                etAddress = dialog.getWindow().findViewById(R.id.etNewPassword);
                etPhone = dialog.getWindow().findViewById(R.id.etAgainNewPassword);
                tvBtnCancel = dialog.getWindow().findViewById(R.id.tvBtnCancel);
                tvBtnSave = dialog.getWindow().findViewById(R.id.tvBtnComment);

                etName.setText(getAddresses.get(position).getName());
                etAddress.setText(getAddresses.get(position).getDetail());
                etPhone.setText(getAddresses.get(position).getPhone());

                //handle btnCancel
                tvBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tvBtnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Address_DAO address_dao = DBConnection.getDBConnection(v.getContext()).createAddressDao();
                        address_dao.updateById(etName.getText().toString(), etAddress.getText().toString(),
                                etPhone.getText().toString(), getAddresses.get(position).getId());

                        dialog.dismiss();
                        Toast.makeText(v.getContext(), "Lưu thành công", Toast.LENGTH_SHORT).show();

                        AddressFragment addressFragment = new AddressFragment();
                        FragmentManager manager = activity.getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.addressProfileFragment, addressFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });


            }
        });

        //handle tvBtnDelete
        holder.tvBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Address_DAO address_dao = DBConnection.getDBConnection(v.getContext()).createAddressDao();
                address_dao.updateStatusOfDelete("blocked", getAddresses.get(position).getId());

                AddressFragment addressFragment = new AddressFragment();
                FragmentManager manager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.addressProfileFragment, addressFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvAddress, tvPhone, tvBtnModify, tvBtnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvBtnModify = itemView.findViewById(R.id.tvBtnModify);
            tvBtnDelete = itemView.findViewById(R.id.tvBtnViewDetail);
        }
    }
}
