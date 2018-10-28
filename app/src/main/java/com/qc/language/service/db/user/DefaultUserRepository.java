package com.qc.language.service.db.user;

import android.content.ContentValues;
import android.database.Cursor;

import com.qc.language.service.db.DB;
import com.qc.language.service.db.DBCurUser;
import com.qc.language.service.db.data.UserDetails;
import com.squareup.sqlbrite.BriteDatabase;

/**
 * 默认的UserRepository实现
 */
public class DefaultUserRepository implements UserRepository {

    private BriteDatabase mDatabase;

    public DefaultUserRepository(BriteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }


    @Override public void saveUserDetails(final UserDetails details) {
        if (details == null) {
            return;
        }

        if (getUserDetailsByUsername(details.getUsername()) == null) {
            ContentValues loginUser = new DBCurUser.Builder()
                    .name(details.getName())
                    .username(details.getUsername())
                    .cellphone(details.getCellphone())
                    .status(details.getStatus())
                    .password(details.getPassword())
                    .endTime(details.getEndTime())
                    .token(details.getToken())
                    .build();
            mDatabase.insert(DBCurUser.TABLE_USER, loginUser);
        } else {
            ContentValues loginUser = new DBCurUser.Builder()
                    .name(details.getName())
                    .username(details.getUsername())
                    .cellphone(details.getCellphone())
                    .status(details.getStatus())
                    .password(details.getPassword())
                    .endTime(details.getEndTime())
                    .token(details.getToken())
                    .build();
            mDatabase.update(DBCurUser.TABLE_USER, loginUser, DBCurUser.COLUMN_USERNAME + " == ?", details.getUsername());
        }
    }

    @Override public UserDetails getUserDetailsByUsername(String username) {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBCurUser.TABLE_USER + " where " + DBCurUser.COLUMN_USERNAME + " == '" + username + "'", null);
        return convert(cursor);
    }

    @Override
    public void removeUserDetailsByUsername(String username) {
        mDatabase.delete(DBCurUser.TABLE_USER, DBCurUser.COLUMN_USERNAME + " == ?", username);
    }

    private UserDetails convert(Cursor cursor) {
        if (null == cursor) {
            return null;
        }
        try {
            while (cursor.moveToNext()) {
                UserDetails userDetails = new UserDetails();
                userDetails.setUsername(DB.getString(cursor, DBCurUser.COLUMN_USERNAME));
                userDetails.setPassword(DB.getString(cursor, DBCurUser.COLUMN_PASSWORD));
                userDetails.setToken(DB.getString(cursor,DBCurUser.COLUMN_TOKEN));
                userDetails.setName(DB.getString(cursor, DBCurUser.COLUMN_NAME));
                userDetails.setCellphone(DB.getString(cursor, DBCurUser.COLUMN_CELLPHONE));
                userDetails.setEndTime(DB.getString(cursor,DBCurUser.COLUMN_END_TIME));
                userDetails.setStatus(DB.getString(cursor, DBCurUser.COLUMN_STATUS));
                return userDetails;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }
}
