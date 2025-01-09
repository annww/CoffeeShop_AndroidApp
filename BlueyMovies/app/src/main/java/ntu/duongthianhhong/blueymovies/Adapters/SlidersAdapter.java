package ntu.duongthianhhong.blueymovies.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import org.w3c.dom.Text;

import java.util.List;

import ntu.duongthianhhong.blueymovies.Domains.SliderItems;

public class SlidersAdapter extends RecyclerView.Adapter<SlidersAdapter.SliderViewholder> {
    private List<SliderItems> sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();
        }
    };

    public SlidersAdapter(List<SliderItems> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlidersAdapter.SliderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SlidersAdapter.SliderViewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewholder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTxt, genreTxt, ageTxt, yearTxt, timeTxt;

        public SliderViewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.)
        }
    }
}
