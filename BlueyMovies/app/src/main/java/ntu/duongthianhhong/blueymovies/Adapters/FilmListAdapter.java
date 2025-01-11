//package ntu.duongthianhhong.blueymovies.Adapters;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import ntu.duongthianhhong.blueymovies.R;
//
//public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {
//    ArrayList <Film> items;
//    Context context;
//
//    public FilmListAdapter(ArrayList<Film> items) {
//        this.items = items;
//    }
//
//    @NonNull
//    @Override
//    public FilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_viewholder,parent,false);
//        return new ViewHolder(inflate);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FilmListAdapter.ViewHolder holder, int position) {
//        holder.titleTxt.setText(items.get(position).getTitle());
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions= requestOptions.transform(new CenterCrop(), new RoundedCorners(30));
//        Glide.with(context)
//                .load(items.get(position).getPoster())
//                .apply(requestOptions)
//                .into(holder.pic);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("object", items.get(position));
//                context.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTxt;
//        ImageView pic;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTxt = itemView.findViewById(R.id.nameTxt);
//            pic = itemView.findViewById(R.id.pic);
//        }
//    }
//}
