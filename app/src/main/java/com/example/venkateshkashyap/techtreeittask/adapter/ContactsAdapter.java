package com.example.venkateshkashyap.techtreeittask.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.model.ContactVo;

import java.util.List;

/**
 * Created by Venkatesh Kashyap on 8/25/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<ContactVo> mContactVoList;
    private Context mContext;

    public ContactsAdapter(List<ContactVo> contactVoList, Context context) {
        mContactVoList = contactVoList;
        mContext = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null, false);
        ContactViewHolder viewHolder = new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactVo contactVo = mContactVoList.get(position);
        holder.contactName.setText(contactVo.getContactName());
        holder.contactNumber.setText(contactVo.getContactNumber());
    }

    @Override
    public int getItemCount() {
        return mContactVoList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView contactName;
        private TextView contactNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.tv_contact_name);
            contactNumber = itemView.findViewById(R.id.tv_phone_number);
        }
    }
}
