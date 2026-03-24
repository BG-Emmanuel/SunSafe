package com.sunsafe.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sunsafe.app.data.local.entity.ExposureEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ExposureDao_Impl implements ExposureDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ExposureEntity> __insertionAdapterOfExposureEntity;

  private final EntityDeletionOrUpdateAdapter<ExposureEntity> __updateAdapterOfExposureEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSession;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsBurned;

  private final SharedSQLiteStatement __preparedStmtOfCloseSession;

  public ExposureDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfExposureEntity = new EntityInsertionAdapter<ExposureEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exposure_sessions` (`id`,`userId`,`startTime`,`endTime`,`durationMinutes`,`avgLux`,`maxLux`,`uvIndex`,`latitude`,`longitude`,`locationName`,`sunscreenApplied`,`sunscreenSPF`,`burned`,`notes`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExposureEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEndTime());
        }
        statement.bindDouble(5, entity.getDurationMinutes());
        statement.bindDouble(6, entity.getAvgLux());
        statement.bindDouble(7, entity.getMaxLux());
        statement.bindDouble(8, entity.getUvIndex());
        if (entity.getLatitude() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLongitude());
        }
        statement.bindString(11, entity.getLocationName());
        final int _tmp = entity.getSunscreenApplied() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getSunscreenSPF());
        final int _tmp_1 = entity.getBurned() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        statement.bindString(15, entity.getNotes());
        final int _tmp_2 = entity.getSynced() ? 1 : 0;
        statement.bindLong(16, _tmp_2);
      }
    };
    this.__updateAdapterOfExposureEntity = new EntityDeletionOrUpdateAdapter<ExposureEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `exposure_sessions` SET `id` = ?,`userId` = ?,`startTime` = ?,`endTime` = ?,`durationMinutes` = ?,`avgLux` = ?,`maxLux` = ?,`uvIndex` = ?,`latitude` = ?,`longitude` = ?,`locationName` = ?,`sunscreenApplied` = ?,`sunscreenSPF` = ?,`burned` = ?,`notes` = ?,`synced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExposureEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        statement.bindLong(3, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEndTime());
        }
        statement.bindDouble(5, entity.getDurationMinutes());
        statement.bindDouble(6, entity.getAvgLux());
        statement.bindDouble(7, entity.getMaxLux());
        statement.bindDouble(8, entity.getUvIndex());
        if (entity.getLatitude() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getLongitude());
        }
        statement.bindString(11, entity.getLocationName());
        final int _tmp = entity.getSunscreenApplied() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getSunscreenSPF());
        final int _tmp_1 = entity.getBurned() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        statement.bindString(15, entity.getNotes());
        final int _tmp_2 = entity.getSynced() ? 1 : 0;
        statement.bindLong(16, _tmp_2);
        statement.bindLong(17, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exposure_sessions WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkAsBurned = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exposure_sessions SET burned = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfCloseSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE exposure_sessions \n"
                + "        SET endTime = ?, avgLux = ?, maxLux = ?, durationMinutes = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final ExposureEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfExposureEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSession(final ExposureEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfExposureEntity.handle(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSession(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSession.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSession.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsBurned(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsBurned.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsBurned.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object closeSession(final long id, final long endTime, final float avgLux,
      final float maxLux, final double duration, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCloseSession.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, endTime);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, avgLux);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, maxLux);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, duration);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfCloseSession.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveSession(final Continuation<? super ExposureEntity> $completion) {
    final String _sql = "SELECT * FROM exposure_sessions WHERE endTime IS NULL LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExposureEntity>() {
      @Override
      @Nullable
      public ExposureEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final int _cursorIndexOfAvgLux = CursorUtil.getColumnIndexOrThrow(_cursor, "avgLux");
          final int _cursorIndexOfMaxLux = CursorUtil.getColumnIndexOrThrow(_cursor, "maxLux");
          final int _cursorIndexOfUvIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "uvIndex");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfSunscreenApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenApplied");
          final int _cursorIndexOfSunscreenSPF = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenSPF");
          final int _cursorIndexOfBurned = CursorUtil.getColumnIndexOrThrow(_cursor, "burned");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final ExposureEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final double _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getDouble(_cursorIndexOfDurationMinutes);
            final float _tmpAvgLux;
            _tmpAvgLux = _cursor.getFloat(_cursorIndexOfAvgLux);
            final float _tmpMaxLux;
            _tmpMaxLux = _cursor.getFloat(_cursorIndexOfMaxLux);
            final double _tmpUvIndex;
            _tmpUvIndex = _cursor.getDouble(_cursorIndexOfUvIndex);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationName;
            _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            final boolean _tmpSunscreenApplied;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSunscreenApplied);
            _tmpSunscreenApplied = _tmp != 0;
            final int _tmpSunscreenSPF;
            _tmpSunscreenSPF = _cursor.getInt(_cursorIndexOfSunscreenSPF);
            final boolean _tmpBurned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBurned);
            _tmpBurned = _tmp_1 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _result = new ExposureEntity(_tmpId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpDurationMinutes,_tmpAvgLux,_tmpMaxLux,_tmpUvIndex,_tmpLatitude,_tmpLongitude,_tmpLocationName,_tmpSunscreenApplied,_tmpSunscreenSPF,_tmpBurned,_tmpNotes,_tmpSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ExposureEntity>> getAllSessions() {
    final String _sql = "SELECT * FROM exposure_sessions ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exposure_sessions"}, new Callable<List<ExposureEntity>>() {
      @Override
      @NonNull
      public List<ExposureEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final int _cursorIndexOfAvgLux = CursorUtil.getColumnIndexOrThrow(_cursor, "avgLux");
          final int _cursorIndexOfMaxLux = CursorUtil.getColumnIndexOrThrow(_cursor, "maxLux");
          final int _cursorIndexOfUvIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "uvIndex");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfSunscreenApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenApplied");
          final int _cursorIndexOfSunscreenSPF = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenSPF");
          final int _cursorIndexOfBurned = CursorUtil.getColumnIndexOrThrow(_cursor, "burned");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<ExposureEntity> _result = new ArrayList<ExposureEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExposureEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final double _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getDouble(_cursorIndexOfDurationMinutes);
            final float _tmpAvgLux;
            _tmpAvgLux = _cursor.getFloat(_cursorIndexOfAvgLux);
            final float _tmpMaxLux;
            _tmpMaxLux = _cursor.getFloat(_cursorIndexOfMaxLux);
            final double _tmpUvIndex;
            _tmpUvIndex = _cursor.getDouble(_cursorIndexOfUvIndex);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationName;
            _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            final boolean _tmpSunscreenApplied;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSunscreenApplied);
            _tmpSunscreenApplied = _tmp != 0;
            final int _tmpSunscreenSPF;
            _tmpSunscreenSPF = _cursor.getInt(_cursorIndexOfSunscreenSPF);
            final boolean _tmpBurned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBurned);
            _tmpBurned = _tmp_1 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _item = new ExposureEntity(_tmpId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpDurationMinutes,_tmpAvgLux,_tmpMaxLux,_tmpUvIndex,_tmpLatitude,_tmpLongitude,_tmpLocationName,_tmpSunscreenApplied,_tmpSunscreenSPF,_tmpBurned,_tmpNotes,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<ExposureEntity>> getSessionsInRange(final long startTime, final long endTime) {
    final String _sql = "SELECT * FROM exposure_sessions WHERE startTime >= ? AND startTime <= ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exposure_sessions"}, new Callable<List<ExposureEntity>>() {
      @Override
      @NonNull
      public List<ExposureEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final int _cursorIndexOfAvgLux = CursorUtil.getColumnIndexOrThrow(_cursor, "avgLux");
          final int _cursorIndexOfMaxLux = CursorUtil.getColumnIndexOrThrow(_cursor, "maxLux");
          final int _cursorIndexOfUvIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "uvIndex");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfSunscreenApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenApplied");
          final int _cursorIndexOfSunscreenSPF = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenSPF");
          final int _cursorIndexOfBurned = CursorUtil.getColumnIndexOrThrow(_cursor, "burned");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<ExposureEntity> _result = new ArrayList<ExposureEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExposureEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final double _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getDouble(_cursorIndexOfDurationMinutes);
            final float _tmpAvgLux;
            _tmpAvgLux = _cursor.getFloat(_cursorIndexOfAvgLux);
            final float _tmpMaxLux;
            _tmpMaxLux = _cursor.getFloat(_cursorIndexOfMaxLux);
            final double _tmpUvIndex;
            _tmpUvIndex = _cursor.getDouble(_cursorIndexOfUvIndex);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationName;
            _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            final boolean _tmpSunscreenApplied;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSunscreenApplied);
            _tmpSunscreenApplied = _tmp != 0;
            final int _tmpSunscreenSPF;
            _tmpSunscreenSPF = _cursor.getInt(_cursorIndexOfSunscreenSPF);
            final boolean _tmpBurned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBurned);
            _tmpBurned = _tmp_1 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _item = new ExposureEntity(_tmpId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpDurationMinutes,_tmpAvgLux,_tmpMaxLux,_tmpUvIndex,_tmpLatitude,_tmpLongitude,_tmpLocationName,_tmpSunscreenApplied,_tmpSunscreenSPF,_tmpBurned,_tmpNotes,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSessionsForDay(final long dayStart, final long dayEnd,
      final Continuation<? super List<ExposureEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM exposure_sessions \n"
            + "        WHERE startTime >= ? AND startTime <= ?\n"
            + "        ORDER BY startTime ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, dayEnd);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExposureEntity>>() {
      @Override
      @NonNull
      public List<ExposureEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMinutes");
          final int _cursorIndexOfAvgLux = CursorUtil.getColumnIndexOrThrow(_cursor, "avgLux");
          final int _cursorIndexOfMaxLux = CursorUtil.getColumnIndexOrThrow(_cursor, "maxLux");
          final int _cursorIndexOfUvIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "uvIndex");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfSunscreenApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenApplied");
          final int _cursorIndexOfSunscreenSPF = CursorUtil.getColumnIndexOrThrow(_cursor, "sunscreenSPF");
          final int _cursorIndexOfBurned = CursorUtil.getColumnIndexOrThrow(_cursor, "burned");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
          final List<ExposureEntity> _result = new ArrayList<ExposureEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExposureEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final double _tmpDurationMinutes;
            _tmpDurationMinutes = _cursor.getDouble(_cursorIndexOfDurationMinutes);
            final float _tmpAvgLux;
            _tmpAvgLux = _cursor.getFloat(_cursorIndexOfAvgLux);
            final float _tmpMaxLux;
            _tmpMaxLux = _cursor.getFloat(_cursorIndexOfMaxLux);
            final double _tmpUvIndex;
            _tmpUvIndex = _cursor.getDouble(_cursorIndexOfUvIndex);
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpLocationName;
            _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            final boolean _tmpSunscreenApplied;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSunscreenApplied);
            _tmpSunscreenApplied = _tmp != 0;
            final int _tmpSunscreenSPF;
            _tmpSunscreenSPF = _cursor.getInt(_cursorIndexOfSunscreenSPF);
            final boolean _tmpBurned;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBurned);
            _tmpBurned = _tmp_1 != 0;
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            final boolean _tmpSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfSynced);
            _tmpSynced = _tmp_2 != 0;
            _item = new ExposureEntity(_tmpId,_tmpUserId,_tmpStartTime,_tmpEndTime,_tmpDurationMinutes,_tmpAvgLux,_tmpMaxLux,_tmpUvIndex,_tmpLatitude,_tmpLongitude,_tmpLocationName,_tmpSunscreenApplied,_tmpSunscreenSPF,_tmpBurned,_tmpNotes,_tmpSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DailyAggregateRow>> getDailyAggregates(final long since) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            (startTime / 86400000) * 86400000 as date,\n"
            + "            SUM(durationMinutes) as totalMinutes,\n"
            + "            MAX(uvIndex) as peakUv,\n"
            + "            SUM(CASE WHEN sunscreenApplied THEN 1 ELSE 0 END) as sunscreenCount,\n"
            + "            MAX(CASE WHEN burned THEN 1 ELSE 0 END) as wasBurned,\n"
            + "            COUNT(*) as sessionsCount\n"
            + "        FROM exposure_sessions\n"
            + "        WHERE startTime >= ?\n"
            + "        GROUP BY (startTime / 86400000)\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"exposure_sessions"}, new Callable<List<DailyAggregateRow>>() {
      @Override
      @NonNull
      public List<DailyAggregateRow> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfTotalMinutes = 1;
          final int _cursorIndexOfPeakUv = 2;
          final int _cursorIndexOfSunscreenCount = 3;
          final int _cursorIndexOfWasBurned = 4;
          final int _cursorIndexOfSessionsCount = 5;
          final List<DailyAggregateRow> _result = new ArrayList<DailyAggregateRow>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailyAggregateRow _item;
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final double _tmpTotalMinutes;
            _tmpTotalMinutes = _cursor.getDouble(_cursorIndexOfTotalMinutes);
            final double _tmpPeakUv;
            _tmpPeakUv = _cursor.getDouble(_cursorIndexOfPeakUv);
            final int _tmpSunscreenCount;
            _tmpSunscreenCount = _cursor.getInt(_cursorIndexOfSunscreenCount);
            final boolean _tmpWasBurned;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfWasBurned);
            _tmpWasBurned = _tmp != 0;
            final int _tmpSessionsCount;
            _tmpSessionsCount = _cursor.getInt(_cursorIndexOfSessionsCount);
            _item = new DailyAggregateRow(_tmpDate,_tmpTotalMinutes,_tmpPeakUv,_tmpSunscreenCount,_tmpWasBurned,_tmpSessionsCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTodayTotalMinutes(final long dayStart, final long dayEnd,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(durationMinutes), 0) \n"
            + "        FROM exposure_sessions \n"
            + "        WHERE startTime >= ? AND startTime <= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, dayStart);
    _argIndex = 2;
    _statement.bindLong(_argIndex, dayEnd);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
