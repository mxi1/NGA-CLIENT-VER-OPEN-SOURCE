package sp.phone.adapter.beta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justwen on 2018/3/23.
 */

public abstract class BaseAdapter<E, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    public static final int VIEW_TYPE_EMPTY = 1;

    public static final int VIEW_TYPE_ITEM = 0;

    public static final int VIEW_TYPE_HEADER = 200;

    public static final int VIEW_TYPE_FOOTER = 100;

    protected View.OnClickListener mOnClickListener;

    protected View.OnLongClickListener mOnLongClickListener;

    protected Context mContext;

    protected LayoutInflater mLayoutInflater;

    protected List<E> mDataList = new ArrayList<>();

    private SparseArray<View> mHeaderViews;

    private View mEmptyView;

    private SparseArray<View> mFooterViews;


    public BaseAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void addHeaderView(View... headerViews) {
        if (mHeaderViews == null) {
            mHeaderViews = new SparseArray<>();
        }
        for (View view : headerViews) {
            mHeaderViews.put(VIEW_TYPE_HEADER + mHeaderViews.size(), view);
        }
    }

    public void addFooterView(View... footerViews) {
        if (mFooterViews == null) {
            mFooterViews = new SparseArray<>();
        }

        for (View view : footerViews) {
            mFooterViews.put(VIEW_TYPE_FOOTER + mFooterViews.size(), view);
        }
    }

    public void removeFooterView(View... footerViews) {
        for (View view : footerViews) {
            int index = mFooterViews.indexOfValue(view);
            mFooterViews.removeAt(index);
        }
    }

    public void removeHeaderView(View... headerViews) {
        for (View view : headerViews) {
            int index = mHeaderViews.indexOfValue(view);
            mHeaderViews.removeAt(index);
        }
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
    }

    private int getFooterViewCount() {
        return mFooterViews == null ? 0 : mFooterViews.size();
    }

    private int getHeaderViewCount() {
        return mHeaderViews == null ? 0 : mHeaderViews.size();
    }

    protected int getItemViewCount() {
        return mDataList.size();
    }

    private int getEmptyViewCount() {
        return getItemViewCount() == 0 && mEmptyView != null? 1 : 0;
    }

    private boolean isFooterViewType(int position) {
        return position >= getItemCount() - getFooterViewCount();
    }

    private boolean isHeaderViewType(int position) {
        return position < getHeaderViewCount();
    }

    @Override
    public int getItemCount() {
        return getFooterViewCount() + getHeaderViewCount() + getItemViewCount() + getEmptyViewCount();
    }

    @Override
    public int getItemViewType(int position) {

        if (isFooterViewType(position)) {
            return mFooterViews.keyAt(position);
        } else if (isHeaderViewType(position)) {
            return mHeaderViews.keyAt(position);
        } else if (getEmptyViewCount() > 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    public void setData(List<E> list) {
        mDataList.clear();
        mDataList.addAll(list);
        notifyDataSetChanged();
    }

    public E getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (mFooterViews != null && mFooterViews.get(viewType) != null) {
            viewHolder = onCreateFooterViewHolder(parent, mFooterViews.get(viewType), viewType - VIEW_TYPE_FOOTER);
        } else if (mHeaderViews != null && mHeaderViews.get(viewType) != null) {
            viewHolder = onCreateHeaderViewHolder(parent, mHeaderViews.get(viewType), viewType - VIEW_TYPE_HEADER);
        } else if (mEmptyView != null && viewType == VIEW_TYPE_EMPTY) {
            viewHolder = onCreateEmptyViewHolder(parent, mEmptyView);
        } else {
            viewHolder = onCreateItemViewHolder(parent, mLayoutInflater);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewType(position)) {
            onBindHeaderViewHolder(holder, position);
        } else if (isFooterViewType(position)) {
            onBindFooterViewHolder(holder, position - getHeaderViewCount() - getItemViewCount() - getEmptyViewCount());
        } else if (getItemViewCount() == 0) {
            onBindEmptyViewHolder(holder);
        } else {
            onBindItemViewHolder((T) holder, position - getHeaderViewCount());
        }
    }

    protected RecyclerView.ViewHolder onCreateEmptyViewHolder(ViewGroup parent, View emptyView) {
        return new RecyclerView.ViewHolder(emptyView) {
        };
    }

    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, View footerView, int position) {
        return new RecyclerView.ViewHolder(footerView) {
        };
    }

    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, View headerView, int position) {
        return new RecyclerView.ViewHolder(headerView) {
        };
    }

    protected void onBindEmptyViewHolder(RecyclerView.ViewHolder holder) {

    }

    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    protected abstract void onBindItemViewHolder(T holder, int position);


    protected abstract T onCreateItemViewHolder(ViewGroup parent, LayoutInflater inflater);


    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

}
