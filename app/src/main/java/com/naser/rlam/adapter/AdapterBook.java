package com.naser.rlam.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.naser.rlam.Helper.RealmController;
import com.naser.rlam.R;
import com.naser.rlam.model.Book;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.MyViewHolder> {
    Context context;
    int layout;
    RealmResults<Book> mList;
    Activity activity;
    private Realm realm;


    public AdapterBook(Context context, RealmResults<Book> mList, int layout, Activity activity) {
        this.context = context;
        this.layout = layout;
        this.mList = mList;
        this.activity = activity;
        realm = RealmController.getInstance().getRealm();

    }

    @Override
    public AdapterBook.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(layout, parent, false);

        final AdapterBook.MyViewHolder vh = new AdapterBook.MyViewHolder(view, viewType);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }

        });

        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterBook.MyViewHolder holder, final int position) {
        holder.bindItem(mList.get(position));
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//tow by delete
        /*        realm.beginTransaction();
                RealmResults<Book> results = realm.where(Book.class).findAll();
                 Book b = results.get(position);
                  b.deleteFromRealm();
                //  remove single match
                 results.deleteFromRealm(position);
                realm.commitTransaction();*/

                // Get the book title to show it in toast message
   /*             Book b = results.get(position);
                String title = b.getTitle();*/

                realm.beginTransaction();
                //String title =  mList.get(position).getTitle();
                mList.get(position).deleteFromRealm();
                realm.commitTransaction();
                notifyDataSetChanged();

                //  Toast.makeText(context, title + " is removed from Realm", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editTitle = (EditText) content.findViewById(R.id.title);
                final EditText editAuthor = (EditText) content.findViewById(R.id.author);
                final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail);

                editTitle.setText(mList.get(position).getTitle());
                editAuthor.setText(mList.get(position).getAuthor());
                editThumbnail.setText(mList.get(position).getImageUrl());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Book")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RealmResults<Book> results = RealmController.with(activity).getBooks();
                                realm.beginTransaction();
                                results.get(position).setAuthor(editAuthor.getText().toString());
                                results.get(position).setTitle(editTitle.getText().toString());
                                results.get(position).setImageUrl(editThumbnail.getText().toString());
                                realm.commitTransaction();

                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textTitle;
        public TextView textAuthor;
        public TextView textDescription;
        public ImageView imageBackground;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            card = itemView.findViewById(R.id.card_books);
            textTitle = itemView.findViewById(R.id.text_books_title);
            textAuthor = itemView.findViewById(R.id.text_books_author);
            textDescription = itemView.findViewById(R.id.text_books_description);
            imageBackground = itemView.findViewById(R.id.image_background);

        }

        public void bindItem(Book book) {

            textTitle.setText(book.getTitle());
            textAuthor.setText(book.getAuthor());
            textDescription.setText(book.getDescription());


        }
    }


}