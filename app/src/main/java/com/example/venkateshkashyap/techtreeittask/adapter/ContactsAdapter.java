package com.example.venkateshkashyap.techtreeittask.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.venkateshkashyap.techtreeittask.R;
import com.example.venkateshkashyap.techtreeittask.interfaces.OnItemClickedListener;
import com.example.venkateshkashyap.techtreeittask.model.ContactVo;

import java.util.List;

/**
 * Created by Venkatesh Kashyap on 8/25/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<ContactVo> mContactVoList;
    private Context mContext;
    private OnItemClickedListener onItemClickedListenerCallBack;

    public ContactsAdapter(List<ContactVo> contactVoList, Context context, OnItemClickedListener clickedListener) {
        mContactVoList = contactVoList;
        mContext = context;
        onItemClickedListenerCallBack = clickedListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null, false);
        final ContactViewHolder viewHolder = new ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, final int position) {
        ContactVo contactVo = mContactVoList.get(position);
        holder.contactName.setText(contactVo.getContactName());
        holder.contactNumber.setText(contactVo.getContactNumber());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListenerCallBack.onClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactVoList.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout;
        private TextView contactName;
        private TextView contactNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout_constraint);
            contactName = itemView.findViewById(R.id.tv_contact_name);
            contactNumber = itemView.findViewById(R.id.tv_phone_number);
        }
    }
}
