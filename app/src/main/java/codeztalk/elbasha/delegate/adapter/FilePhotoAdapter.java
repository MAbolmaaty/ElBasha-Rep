package codeztalk.elbasha.delegate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.AddPocketActivity;
import codeztalk.elbasha.delegate.helper.ImageSaver;
import codeztalk.elbasha.delegate.models.fileModel;


public class FilePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 2;


    private Context context;
    private ArrayList<fileModel> filesArrayList;


    private AddPocketActivity addPocketActivity;


    public FilePhotoAdapter(AddPocketActivity addPocketActivity, ArrayList<fileModel> filesArrayList) {

        this.addPocketActivity = addPocketActivity;
        this.filesArrayList = filesArrayList;
    }

    public FilePhotoAdapter(ArrayList<fileModel> filesArrayList) {
        this.filesArrayList = filesArrayList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageSelected;
        ImageView imageDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            imageDelete = itemView.findViewById(R.id.imageDelete);
        }

        @Override
        public void onClick(View view) {

        }
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_selected, parent, false);
//        context = parent.getContext();
//
//        return new ViewHolder(itemView);
//    }


    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearAddPhoto;

        HeaderViewHolder(View view) {
            super(view);
            linearAddPhoto = view.findViewById(R.id.linearAddPhoto);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageSelected;
        ImageView imageDelete;

        ItemViewHolder(View view) {
            super(view);

            imageSelected = itemView.findViewById(R.id.imageSelected);
            imageDelete = itemView.findViewById(R.id.imageDelete);

        }

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == TYPE_HEADER) {
            row = inflater.inflate(R.layout.item_add_photo, parent, false);
            return new HeaderViewHolder(row);
        } else if (viewType == TYPE_ITEM) {
            row = inflater.inflate(R.layout.item_photo_selected, parent, false);
            return new ItemViewHolder(row);

        }
        return null;
    }


    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.linearAddPhoto.setOnClickListener(view -> addPocketActivity.addImage());
        } else if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            final fileModel fileModel = filesArrayList.get(position);

            if (fileModel.getFileType().equalsIgnoreCase("image")) {

                if (fileModel.getImagePath() != null && !fileModel.getImagePath().isEmpty()) {

                    Glide.with(context)
                            .load(fileModel.getImagePath())
                            .downloadOnly(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {

                                    if (fileModel.getImagePath() != null && !fileModel.getImagePath().isEmpty()) {
                                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                        Bitmap bitmap = BitmapFactory.decodeFile(resource.getAbsolutePath(), bmOptions);
                                        new ImageSaver(context).setFileName("myImage").setDirectoryName("basha_images").save(bitmap);

                                        bitmap = new ImageSaver(context).setFileName("myImage").setDirectoryName("basha_images").load();

                                        new ImageSaver(context).deletefile("myImage");
                                        Uri uri;
                                        uri = getImageUri(context, bitmap);
                                        fileModel.setImageUri(uri);
                                        fileModel.setImagePath(null);
                                        itemViewHolder.imageSelected.setImageURI(fileModel.getImageUri());

                                    }

                                }
                            });
                } else {

                    itemViewHolder.imageSelected.setImageURI(fileModel.getImageUri());

                }

                itemViewHolder.imageDelete.setOnClickListener(v -> {

                    deleteItem(position);
                    notifyDataSetChanged();


                });

            }


        }


    }


    public void addItem(fileModel dataObj) {
        filesArrayList.add(dataObj);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        filesArrayList.remove(index);
        notifyItemRemoved(index);
    }


    @Override
    public int getItemCount() {
        return filesArrayList.size() + 1;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

    @Override
    public int getItemViewType(int position) {

        if (position == filesArrayList.size()) {
            return TYPE_HEADER;
        }

//        if (position == 0) {
//            return TYPE_HEADER;
//        }

        return TYPE_ITEM;

    }


    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}
