package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.gilgamesh.abon.motot.R;

public class RatingAdapter extends RecyclerView.Adapter<RatingHolder> {
    List<RatingItem> items;
    private final RatingAdapter.OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public RatingAdapter (List<RatingItem> items, RatingAdapter.OnItemClickListener mOnItemClickListener) {
        this.items = items;
        this.mOnItemClickListener = mOnItemClickListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public RatingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_km, parent, false);
        return new RatingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingHolder holder, int position) {
        if (items == null || items.size() <= position) return;
        holder.bind(items.get(position), position);
        holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getContactId();
    }

    public void addItems(List<RatingItem> newItems) {
        if (items == null) return;
        int start = items.size();
        items.addAll(newItems);
        notifyItemRangeInserted(start, newItems.size());
    }

    public void clearItems() {
        if (items == null) return;
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public Long getContactId (int position) {
        if (items == null || items.isEmpty()) return -1L;
        int size = items.size();
        if (position < size) {
            return items.get(position).getContactId();
        }
        return -1L;
    }
}
