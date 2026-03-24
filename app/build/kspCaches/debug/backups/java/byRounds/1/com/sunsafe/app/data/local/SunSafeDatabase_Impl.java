package com.sunsafe.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.sunsafe.app.data.local.dao.ExposureDao;
import com.sunsafe.app.data.local.dao.ExposureDao_Impl;
import com.sunsafe.app.data.local.dao.SkinPhotoDao;
import com.sunsafe.app.data.local.dao.SkinPhotoDao_Impl;
import com.sunsafe.app.data.local.dao.UserDao;
import com.sunsafe.app.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SunSafeDatabase_Impl extends SunSafeDatabase {
  private volatile UserDao _userDao;

  private volatile ExposureDao _exposureDao;

  private volatile SkinPhotoDao _skinPhotoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `skinTypeOrdinal` INTEGER NOT NULL, `defaultSPF` INTEGER NOT NULL, `vitaminDDeficient` INTEGER NOT NULL, `age` INTEGER NOT NULL, `name` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exposure_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER, `durationMinutes` REAL NOT NULL, `avgLux` REAL NOT NULL, `maxLux` REAL NOT NULL, `uvIndex` REAL NOT NULL, `latitude` REAL, `longitude` REAL, `locationName` TEXT NOT NULL, `sunscreenApplied` INTEGER NOT NULL, `sunscreenSPF` INTEGER NOT NULL, `burned` INTEGER NOT NULL, `notes` TEXT NOT NULL, `synced` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exposure_sessions_userId` ON `exposure_sessions` (`userId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_exposure_sessions_startTime` ON `exposure_sessions` (`startTime`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `skin_photos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `imagePath` TEXT NOT NULL, `bodyPart` TEXT NOT NULL, `note` TEXT NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_skin_photos_userId` ON `skin_photos` (`userId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '309a08dcfd804daffb3bf3a1e7522bb3')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `exposure_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `skin_photos`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(7);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("skinTypeOrdinal", new TableInfo.Column("skinTypeOrdinal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("defaultSPF", new TableInfo.Column("defaultSPF", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("vitaminDDeficient", new TableInfo.Column("vitaminDDeficient", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.sunsafe.app.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsExposureSessions = new HashMap<String, TableInfo.Column>(16);
        _columnsExposureSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("durationMinutes", new TableInfo.Column("durationMinutes", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("avgLux", new TableInfo.Column("avgLux", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("maxLux", new TableInfo.Column("maxLux", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("uvIndex", new TableInfo.Column("uvIndex", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("locationName", new TableInfo.Column("locationName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("sunscreenApplied", new TableInfo.Column("sunscreenApplied", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("sunscreenSPF", new TableInfo.Column("sunscreenSPF", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("burned", new TableInfo.Column("burned", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExposureSessions.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExposureSessions = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysExposureSessions.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesExposureSessions = new HashSet<TableInfo.Index>(2);
        _indicesExposureSessions.add(new TableInfo.Index("index_exposure_sessions_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        _indicesExposureSessions.add(new TableInfo.Index("index_exposure_sessions_startTime", false, Arrays.asList("startTime"), Arrays.asList("ASC")));
        final TableInfo _infoExposureSessions = new TableInfo("exposure_sessions", _columnsExposureSessions, _foreignKeysExposureSessions, _indicesExposureSessions);
        final TableInfo _existingExposureSessions = TableInfo.read(db, "exposure_sessions");
        if (!_infoExposureSessions.equals(_existingExposureSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "exposure_sessions(com.sunsafe.app.data.local.entity.ExposureEntity).\n"
                  + " Expected:\n" + _infoExposureSessions + "\n"
                  + " Found:\n" + _existingExposureSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsSkinPhotos = new HashMap<String, TableInfo.Column>(6);
        _columnsSkinPhotos.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSkinPhotos.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSkinPhotos.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSkinPhotos.put("imagePath", new TableInfo.Column("imagePath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSkinPhotos.put("bodyPart", new TableInfo.Column("bodyPart", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSkinPhotos.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSkinPhotos = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSkinPhotos.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("userId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSkinPhotos = new HashSet<TableInfo.Index>(1);
        _indicesSkinPhotos.add(new TableInfo.Index("index_skin_photos_userId", false, Arrays.asList("userId"), Arrays.asList("ASC")));
        final TableInfo _infoSkinPhotos = new TableInfo("skin_photos", _columnsSkinPhotos, _foreignKeysSkinPhotos, _indicesSkinPhotos);
        final TableInfo _existingSkinPhotos = TableInfo.read(db, "skin_photos");
        if (!_infoSkinPhotos.equals(_existingSkinPhotos)) {
          return new RoomOpenHelper.ValidationResult(false, "skin_photos(com.sunsafe.app.data.local.entity.SkinPhotoEntity).\n"
                  + " Expected:\n" + _infoSkinPhotos + "\n"
                  + " Found:\n" + _existingSkinPhotos);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "309a08dcfd804daffb3bf3a1e7522bb3", "dfd3264257f69bf76f04ebd08e7aaec4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","exposure_sessions","skin_photos");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `exposure_sessions`");
      _db.execSQL("DELETE FROM `skin_photos`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ExposureDao.class, ExposureDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SkinPhotoDao.class, SkinPhotoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public ExposureDao exposureDao() {
    if (_exposureDao != null) {
      return _exposureDao;
    } else {
      synchronized(this) {
        if(_exposureDao == null) {
          _exposureDao = new ExposureDao_Impl(this);
        }
        return _exposureDao;
      }
    }
  }

  @Override
  public SkinPhotoDao skinPhotoDao() {
    if (_skinPhotoDao != null) {
      return _skinPhotoDao;
    } else {
      synchronized(this) {
        if(_skinPhotoDao == null) {
          _skinPhotoDao = new SkinPhotoDao_Impl(this);
        }
        return _skinPhotoDao;
      }
    }
  }
}
