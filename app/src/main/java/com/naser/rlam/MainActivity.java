package com.naser.rlam;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naser.rlam.Helper.RealmController;
import com.naser.rlam.adapter.AdapterBook;
import com.naser.rlam.model.Book;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private AdapterBook adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.fab);
        recycler = findViewById(R.id.recycler);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        //set toolbar
        setRealmData();
        setupRecycler();
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        //setRealmAdapter();

        Toast.makeText(this, "Press card item for edit, long press to remove item", Toast.LENGTH_LONG).show();

        //add new item
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inflater = MainActivity.this.getLayoutInflater();
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editTitle = (EditText) content.findViewById(R.id.title);
                final EditText editAuthor = (EditText) content.findViewById(R.id.author);
                final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(content)
                        .setTitle("Add book")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Book book = new Book();
                                //book.setId(RealmController.getInstance().getBooks().size() + 1);
                                book.setId((int) (RealmController.getInstance().getBooks().size() + System.currentTimeMillis()));
                                book.setTitle(editTitle.getText().toString());
                                book.setAuthor(editAuthor.getText().toString());
                                book.setImageUrl(editThumbnail.getText().toString());

                                if (editTitle.getText() == null || editTitle.getText().toString().equals("") || editTitle.getText().toString().equals(" ")) {
                                    Toast.makeText(MainActivity.this, "Entry not saved, missing title", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Persist your data easily
                                    realm.beginTransaction();
                                    realm.copyToRealm(book);
                                    realm.commitTransaction();

                                    adapter.notifyDataSetChanged();

                                    // scroll the recycler view to bottom
                                    recycler.scrollToPosition(RealmController.getInstance().getBooks().size() - 1);
                                }
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
    }


    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new AdapterBook(this, RealmController.with(this).getBooks(), R.layout.item_book, this);
        recycler.setAdapter(adapter);
    }

    private void setRealmData() {

        ArrayList<Book> books = new ArrayList<>();

        Book book = new Book();
        book.setId((int) (1 + System.currentTimeMillis()));
        book.setAuthor("Reto Meier");
        book.setTitle("Android 4 Application Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/1.png");
        books.add(book);

        book = new Book();
        book.setId((int) (2 + System.currentTimeMillis()));
        book.setAuthor("Itzik Ben-Gan");
        book.setTitle("Microsoft SQL Server 2012 T-SQL Fundamentals");
        book.setImageUrl("https://api.androidhive.info/images/realm/2.png");
        books.add(book);

        book = new Book();
        book.setId((int) (3 + System.currentTimeMillis()));
        book.setAuthor("Magnus Lie Hetland");
        book.setTitle("Beginning Python: From Novice To Professional Paperback");
        book.setImageUrl("https://api.androidhive.info/images/realm/3.png");
        books.add(book);

        book = new Book();
        book.setId((int) (4 + System.currentTimeMillis()));
        book.setAuthor("Chad Fowler");
        book.setTitle("The Passionate Programmer: Creating a Remarkable Career in Software Development");
        book.setImageUrl("https://api.androidhive.info/images/realm/4.png");
        books.add(book);

        book = new Book();
        book.setId((int) (5 + System.currentTimeMillis()));
        book.setAuthor("Yashavant Kanetkar");
        book.setTitle("Written Test Questions In C Programming");
        book.setImageUrl("https://api.androidhive.info/images/realm/5.png");
        books.add(book);

        for (int i = 0; i < books.size(); i++) {
            realm.beginTransaction();
            realm.copyToRealm(books.get(i));
            realm.commitTransaction();
        }
       /* for (Book b : books) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(b);
            realm.commitTransaction();
        }*/

        //  Prefs.with(this).setPreLoad(true);

    }

}
