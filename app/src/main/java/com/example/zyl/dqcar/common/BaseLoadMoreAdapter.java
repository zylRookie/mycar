package com.example.zyl.dqcar.common;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zyl.dqcar.R;


public abstract class BaseLoadMoreAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_FOOTER = 666;

    protected View mFooterView;
    private boolean mFooterEnabled = true;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private boolean isFooterShowing;

    public void setFooterView(View v) {
        mFooterView = v;
    }

    public void setFooterEnable(boolean enable) {
        mFooterEnabled = enable;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public void notifyLoadMoreCompleted() {
        isLoading = false;
        mFooterView.setVisibility(View.GONE);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof FooterViewHolder)
            isFooterShowing = true;

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams && holder instanceof FooterViewHolder) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof FooterViewHolder)
            isFooterShowing = false;
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        if (mFooterEnabled && mFooterView == null)
            mFooterView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.footer, recyclerView, false);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isFullSpan(position) || isFooter(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isFooterShowing && !isLoading) {
                    isLoading = true;
                    mFooterView.setVisibility(View.VISIBLE);
                    if (mOnLoadMoreListener != null)
                        mOnLoadMoreListener.onLoadMore();
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER)
            return new FooterViewHolder(mFooterView);
        return onCreateViewHolderImpl(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder)
            return;
        onBindViewHolderImpl((T) holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position))
            return TYPE_FOOTER;
        return getItemViewTypeImpl(position);
    }

    @Override
    public int getItemCount() {
        int count = getItemCountImpl();
        if (mFooterEnabled && count != 0)
            count++;
        return count;
    }

    protected boolean isFooter(int position) {
        return position == getItemCount() - 1 && mFooterEnabled;
    }

    protected boolean isFullSpan(int position) {
        return false;
    }

    protected abstract int getItemViewTypeImpl(int position);

    protected abstract int getItemCountImpl();

    protected abstract T onCreateViewHolderImpl(ViewGroup parent, int viewType);

    protected abstract void onBindViewHolderImpl(T holder, int position);

    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    protected static class FooterViewHolder extends RecyclerView.ViewHolder {

        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

}