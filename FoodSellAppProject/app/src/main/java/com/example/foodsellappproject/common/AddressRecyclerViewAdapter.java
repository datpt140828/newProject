package com.example.foodsellappproject.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Address;
import com.example.foodsellappproject.ui.cart.CheckOutFragment;

import java.util.ArrayList;

public class AddressRecyclerViewAdapter extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Address> getAddresses;
    private Bundle b;

    public AddressRecyclerViewAdapter(ArrayList<Address> getAddresses, Bundle b) {
        this.getAddresses = getAddresses;
        this.b = b;
    }

    @NonNull
    @Override
    public AddressRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_custom, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final AddressRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(getAddresses.get(position).getName());
        holder.tvPhone.setText(getAddresses.get(position).getPhone());
        holder.tvAddress.setText(getAddresses.get(position).getDetail());
        if (b != null) {
            boolean checked = b.getBoolean("checked", false);
            int position2 = b.getInt("position", 0);
            if (position2 == position) {
                holder.cbAddress.setChecked(checked);
                //    Toast.makeText(holder.itemView.getContext(), "Bundle "+position2, Toast.LENGTH_SHORT).show();
            }
        }
        holder.cbAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (holder.cbAddress.isChecked()) {
                    holder.cbAddress.setChecked(true);
                    bundle.putBoolean("checked", true);
                    SharedPreferences pref = v.getContext().getSharedPreferences("handleAddress", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("addressId",getAddresses.get(position).getId());
                    editor.commit();
                } else {
                    holder.cbAddress.setChecked(false);
                    bundle.putBoolean("checked", false);
                }
                //     Toast.makeText(holder.itemView.getContext(), ""+bundle.getBoolean("checked",false), Toast.LENGTH_SHORT).show();
                bundle.putInt("position", position);

                CheckOutFragment checkOutFragment = new CheckOutFragment();
                checkOutFragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.checkOutFragment, checkOutFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getAddresses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvAddress, tvPhone;
        private CheckBox cbAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            cbAddress = itemView.findViewById(R.id.cbAddress);
        }
    }
}
