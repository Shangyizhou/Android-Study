package com.example.androidnote.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.androidnote.model.Message;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE".
*/
public class MessageDao extends AbstractDao<Message, Long> {

    public static final String TABLENAME = "MESSAGE";

    /**
     * Properties of entity Message.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, int.class, "type", false, "TYPE");
        public final static Property MessageId = new Property(2, String.class, "messageId", false, "MESSAGE_ID");
        public final static Property SessionId = new Property(3, String.class, "sessionId", false, "SESSION_ID");
        public final static Property Message = new Property(4, String.class, "message", false, "MESSAGE");
        public final static Property SendTime = new Property(5, long.class, "sendTime", false, "SEND_TIME");
        public final static Property Status = new Property(6, int.class, "status", false, "STATUS");
    }


    public MessageDao(DaoConfig config) {
        super(config);
    }
    
    public MessageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TYPE\" INTEGER NOT NULL ," + // 1: type
                "\"MESSAGE_ID\" TEXT NOT NULL ," + // 2: messageId
                "\"SESSION_ID\" TEXT NOT NULL ," + // 3: sessionId
                "\"MESSAGE\" TEXT NOT NULL ," + // 4: message
                "\"SEND_TIME\" INTEGER NOT NULL ," + // 5: sendTime
                "\"STATUS\" INTEGER NOT NULL );"); // 6: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getType());
        stmt.bindString(3, entity.getMessageId());
        stmt.bindString(4, entity.getSessionId());
        stmt.bindString(5, entity.getMessage());
        stmt.bindLong(6, entity.getSendTime());
        stmt.bindLong(7, entity.getStatus());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Message entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getType());
        stmt.bindString(3, entity.getMessageId());
        stmt.bindString(4, entity.getSessionId());
        stmt.bindString(5, entity.getMessage());
        stmt.bindLong(6, entity.getSendTime());
        stmt.bindLong(7, entity.getStatus());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Message readEntity(Cursor cursor, int offset) {
        Message entity = new Message( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // type
            cursor.getString(offset + 2), // messageId
            cursor.getString(offset + 3), // sessionId
            cursor.getString(offset + 4), // message
            cursor.getLong(offset + 5), // sendTime
            cursor.getInt(offset + 6) // status
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Message entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.getInt(offset + 1));
        entity.setMessageId(cursor.getString(offset + 2));
        entity.setSessionId(cursor.getString(offset + 3));
        entity.setMessage(cursor.getString(offset + 4));
        entity.setSendTime(cursor.getLong(offset + 5));
        entity.setStatus(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Message entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Message entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Message entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}