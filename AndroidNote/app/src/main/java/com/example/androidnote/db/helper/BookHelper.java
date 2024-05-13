package com.example.androidnote.db.helper;

import com.example.androidnote.db.AdviceDao;
import com.example.androidnote.db.BookDao;
import com.example.androidnote.db.DaoManager;
import com.example.androidnote.model.Book;
import com.shangyizhou.develop.log.SLog;

import java.util.List;

public class BookHelper {
    private static final String TAG = BookHelper.class.getSimpleName();
    private static volatile BookHelper instance;
    BookDao mBookDao;

    public BookHelper() {
        mBookDao = DaoManager.getInstance().getDaoSession().getBookDao();
    }

    public static BookHelper getInstance() {
        if (instance == null) {
            synchronized (BookHelper.class) {
                if (instance == null) {
                    instance = new BookHelper();
                }
            }
        }
        return instance;
    }

    private boolean isDataBaseValid() {
        return mBookDao != null;
    }

    public void save(Book book) {
        SLog.i(TAG, "save book: " + book);
        if (!isDataBaseValid()) {
            return;
        }
        mBookDao.insertOrReplace(book);
    }

    public void save(List<Book> bookList) {
        SLog.i(TAG, "save bookList size is " + bookList.size());
        if (!isDataBaseValid()) {
            return;
        }
        for (Book book : bookList) {
            mBookDao.insertOrReplace(book);
        }
    }

    public List<Book> getAllBook() {
        SLog.i(TAG, "getAllBook: ");
        if (!isDataBaseValid()) {
            return null;
        }
        return mBookDao.loadAll();
    }

    public List<Book> getBookByUser(String userId) {
        SLog.i(TAG, "getBookByUser: userId=" + userId);
        if (!isDataBaseValid()) {
            return null;
        }
        return mBookDao.queryBuilder().where(AdviceDao.Properties.UserId.eq(userId)).list();
    }
}
