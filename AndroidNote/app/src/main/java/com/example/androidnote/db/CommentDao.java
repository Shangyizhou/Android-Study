package com.example.androidnote.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.androidnote.model.Comment;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMMENT".
*/
public class CommentDao extends AbstractDao<Comment, Long> {

    public static final String TABLENAME = "COMMENT";

    /**
     * Properties of entity Comment.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property RobotId = new Property(1, String.class, "robotId", false, "ROBOT_ID");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property UserId = new Property(3, String.class, "userId", false, "USER_ID");
        public final static Property CreateTime = new Property(4, long.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(5, long.class, "updateTime", false, "UPDATE_TIME");
    }


    public CommentDao(DaoConfig config) {
        super(config);
    }
    
    public CommentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMMENT\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ROBOT_ID\" TEXT," + // 1: robotId
                "\"CONTENT\" TEXT," + // 2: content
                "\"USER_ID\" TEXT," + // 3: userId
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 4: createTime
                "\"UPDATE_TIME\" INTEGER NOT NULL );"); // 5: updateTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMMENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String robotId = entity.getRobotId();
        if (robotId != null) {
            stmt.bindString(2, robotId);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(4, userId);
        }
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getUpdateTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Comment entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String robotId = entity.getRobotId();
        if (robotId != null) {
            stmt.bindString(2, robotId);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(4, userId);
        }
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getUpdateTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Comment readEntity(Cursor cursor, int offset) {
        Comment entity = new Comment( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // robotId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userId
            cursor.getLong(offset + 4), // createTime
            cursor.getLong(offset + 5) // updateTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Comment entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRobotId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreateTime(cursor.getLong(offset + 4));
        entity.setUpdateTime(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Comment entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Comment entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Comment entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
