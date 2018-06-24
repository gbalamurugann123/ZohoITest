package com.zoho.android.ui;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zoho.android.R;
import com.zoho.android.repository.NetworkState;
import com.zoho.android.repository.Status;
import com.zoho.android.vo.User;

public class UserAdapter extends PagedListAdapter<User, RecyclerView.ViewHolder> {

    private static final String TAG = "UserAdapter";
    private NetworkState networkState;
    private Context mContext;

    public UserAdapter(Context context) {
        super(User.DIFF_CALLBACK);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == R.layout.item_user_list) {
            view = layoutInflater.inflate(R.layout.item_user_list, parent, false);
            return new UserItemViewHolder(mContext,view);
        } else if (viewType == R.layout.network_state_item) {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false);
            return new NetworkStateItemViewHolder(view);
        } else {
            throw new IllegalArgumentException("unknown view type");
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_user_list:
                ((UserItemViewHolder) holder).bindTo(getItem(position));
                break;
            case R.layout.network_state_item:
                ((NetworkStateItemViewHolder) holder).bindView(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.item_user_list;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    static class UserItemViewHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userName;
        Context context;

        public UserItemViewHolder(Context context,View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
            this.context = context;
        }

        public void bindTo(User user) {
            userName.setText(user.firstName + " " + user.lastName);

            Glide.with(context).load(user.avatar)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userAvatar);

        }
    }

    static class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        private final TextView errorMsg;
        private Button button;

        public NetworkStateItemViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            errorMsg = itemView.findViewById(R.id.error_msg);

        }


        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == Status.FAILED) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(networkState.getMsg());
            } else {
                errorMsg.setVisibility(View.GONE);
            }
        }
    }
}
