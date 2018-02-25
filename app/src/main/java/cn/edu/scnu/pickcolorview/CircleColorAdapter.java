package cn.edu.scnu.pickcolorview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CircleColorAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context mContext;
    private List<Integer> mColorList;
    private List<Boolean> mActiveList;
    private OnItemClickListener mOnItemClickListener;

    public CircleColorAdapter(Context mContext, List<Integer> mColorList, List<Boolean> mActiveList) {
        this.mContext = mContext;
        this.mColorList = mColorList;
        this.mActiveList = mActiveList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.circle_color_view, parent, false);
        CircleColorViewHolder holder = new CircleColorViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && holder instanceof CircleColorViewHolder) {
            CircleColorViewHolder colorHolder = (CircleColorViewHolder) holder;
            colorHolder.mCircleColorView.setColor(mColorList.get(position));
            colorHolder.mCircleColorView.setActive(mActiveList.get(position));
            colorHolder.view.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mColorList.size();
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    private class CircleColorViewHolder extends RecyclerView.ViewHolder {
        View view;
        CircleColorView mCircleColorView;

        public CircleColorViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mCircleColorView = itemView.findViewById(R.id.circleColorView);
        }
    }
}
