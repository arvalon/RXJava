package ru.arvalon.rx.chapter4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.arvalon.rx.R;
import ru.arvalon.rx.chapter4.pojo.Photo;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>{

    final private Context context;
    final private List<Photo> photos;

    PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_list_card_view, viewGroup, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder photoViewHolder, int i) {
        final Photo photo = photos.get(i);
        photoViewHolder.personName.setText(photo.getTitle());
        photoViewHolder.personAge.setText(photo.getUsername());
        Picasso.with(context).load(photo.getThumbnailUrl()).into(photoViewHolder.personPhoto);
        //photoViewHolder.personPhoto.setImageResource(photos.get(i).photoId);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PhotoViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            personName = itemView.findViewById(R.id.person_name);
            personAge = itemView.findViewById(R.id.person_age);
            personPhoto = itemView.findViewById(R.id.person_photo);
        }
    }
}
