package com.naser.rlam.Helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.naser.rlam.model.Book;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    Context context;
    private Realm realm = null;

    public RealmController(Application application) {
        realm.init(application);
        realm = Realm.getDefaultInstance();
    }


    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        realm.delete(Book.class);
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<Book> getBooks() {

        return realm.where(Book.class).findAll();
    }

    //query a single item with the given id
    public Book getBook(String id) {

        return realm.where(Book.class).equalTo("id", id).findFirst();
    }

    //

    //query example
    public RealmResults<Book> queryedBooks() {

        return realm.where(Book.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}
