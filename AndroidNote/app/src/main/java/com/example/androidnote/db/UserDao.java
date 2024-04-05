package com.example.androidnote.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.androidnote.model.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Dr = new Property(1, int.class, "dr", false, "DR");
        public final static Property HeadUrl = new Property(2, String.class, "headUrl", false, "HEAD_URL");
        public final static Property Obj = new Property(3, String.class, "obj", false, "OBJ");
        public final static Property Name = new Property(4, String.class, "name", false, "NAME");
        public final static Property Auth_token = new Property(5, String.class, "auth_token", false, "AUTH_TOKEN");
        public final static Property Account = new Property(6, String.class, "account", false, "ACCOUNT");
        public final static Property Password = new Property(7, String.class, "password", false, "PASSWORD");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"DR\" INTEGER NOT NULL ," + // 1: dr
                "\"HEAD_URL\" TEXT," + // 2: headUrl
                "\"OBJ\" TEXT," + // 3: obj
                "\"NAME\" TEXT NOT NULL ," + // 4: name
                "\"AUTH_TOKEN\" TEXT," + // 5: auth_token
                "\"ACCOUNT\" TEXT NOT NULL ," + // 6: account
                "\"PASSWORD\" TEXT NOT NULL );"); // 7: password
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getDr());
 
        String headUrl = entity.getHeadUrl();
        if (headUrl != null) {
            stmt.bindString(3, headUrl);
        }
 
        String obj = entity.getObj();
        if (obj != null) {
            stmt.bindString(4, obj);
        }
        stmt.bindString(5, entity.getName());
 
        String auth_token = entity.getAuth_token();
        if (auth_token != null) {
            stmt.bindString(6, auth_token);
        }
        stmt.bindString(7, entity.getAccount());
        stmt.bindString(8, entity.getPassword());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getDr());
 
        String headUrl = entity.getHeadUrl();
        if (headUrl != null) {
            stmt.bindString(3, headUrl);
        }
 
        String obj = entity.getObj();
        if (obj != null) {
            stmt.bindString(4, obj);
        }
        stmt.bindString(5, entity.getName());
 
        String auth_token = entity.getAuth_token();
        if (auth_token != null) {
            stmt.bindString(6, auth_token);
        }
        stmt.bindString(7, entity.getAccount());
        stmt.bindString(8, entity.getPassword());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getInt(offset + 1), // dr
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // headUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // obj
            cursor.getString(offset + 4), // name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // auth_token
            cursor.getString(offset + 6), // account
            cursor.getString(offset + 7) // password
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setDr(cursor.getInt(offset + 1));
        entity.setHeadUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setObj(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setName(cursor.getString(offset + 4));
        entity.setAuth_token(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAccount(cursor.getString(offset + 6));
        entity.setPassword(cursor.getString(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
