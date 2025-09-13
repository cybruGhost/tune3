package com.example.swipfy.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.swipfy.data.models.Song;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SongDao_Impl implements SongDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Song> __insertionAdapterOfSong;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLikeStatus;

  private final SharedSQLiteStatement __preparedStmtOfAddToPlaylist;

  public SongDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSong = new EntityInsertionAdapter<Song>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `songs` (`id`,`title`,`artist`,`album`,`albumCover`,`previewUrl`,`duration`,`genre`,`releaseDate`,`popularity`,`externalUrl`,`isLiked`,`playlistName`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Song entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getTitle() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTitle());
        }
        if (entity.getArtist() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getArtist());
        }
        if (entity.getAlbum() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAlbum());
        }
        if (entity.getAlbumCover() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAlbumCover());
        }
        if (entity.getPreviewUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPreviewUrl());
        }
        statement.bindLong(7, entity.getDuration());
        if (entity.getGenre() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getGenre());
        }
        if (entity.getReleaseDate() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getReleaseDate());
        }
        statement.bindLong(10, entity.getPopularity());
        if (entity.getExternalUrl() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getExternalUrl());
        }
        final int _tmp = entity.isLiked() ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.getPlaylistName() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPlaylistName());
        }
      }
    };
    this.__preparedStmtOfUpdateLikeStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE songs SET isLiked = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAddToPlaylist = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE songs SET playlistName = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Song song, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSong.insert(song);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLikeStatus(final String id, final boolean isLiked,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLikeStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isLiked ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
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
          __preparedStmtOfUpdateLikeStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object addToPlaylist(final String id, final String playlistName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddToPlaylist.acquire();
        int _argIndex = 1;
        if (playlistName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, playlistName);
        }
        _argIndex = 2;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
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
          __preparedStmtOfAddToPlaylist.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getSongById(final String id, final Continuation<? super Song> $completion) {
    final String _sql = "SELECT * FROM songs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Song>() {
      @Override
      @Nullable
      public Song call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfAlbumCover = CursorUtil.getColumnIndexOrThrow(_cursor, "albumCover");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfReleaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseDate");
          final int _cursorIndexOfPopularity = CursorUtil.getColumnIndexOrThrow(_cursor, "popularity");
          final int _cursorIndexOfExternalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "externalUrl");
          final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLiked");
          final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistName");
          final Song _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final String _tmpAlbumCover;
            if (_cursor.isNull(_cursorIndexOfAlbumCover)) {
              _tmpAlbumCover = null;
            } else {
              _tmpAlbumCover = _cursor.getString(_cursorIndexOfAlbumCover);
            }
            final String _tmpPreviewUrl;
            if (_cursor.isNull(_cursorIndexOfPreviewUrl)) {
              _tmpPreviewUrl = null;
            } else {
              _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            }
            final int _tmpDuration;
            _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final String _tmpReleaseDate;
            if (_cursor.isNull(_cursorIndexOfReleaseDate)) {
              _tmpReleaseDate = null;
            } else {
              _tmpReleaseDate = _cursor.getString(_cursorIndexOfReleaseDate);
            }
            final int _tmpPopularity;
            _tmpPopularity = _cursor.getInt(_cursorIndexOfPopularity);
            final String _tmpExternalUrl;
            if (_cursor.isNull(_cursorIndexOfExternalUrl)) {
              _tmpExternalUrl = null;
            } else {
              _tmpExternalUrl = _cursor.getString(_cursorIndexOfExternalUrl);
            }
            final boolean _tmpIsLiked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIsLiked = _tmp != 0;
            final String _tmpPlaylistName;
            if (_cursor.isNull(_cursorIndexOfPlaylistName)) {
              _tmpPlaylistName = null;
            } else {
              _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
            }
            _result = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumCover,_tmpPreviewUrl,_tmpDuration,_tmpGenre,_tmpReleaseDate,_tmpPopularity,_tmpExternalUrl,_tmpIsLiked,_tmpPlaylistName);
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
  public Object getLikedSongs(final Continuation<? super List<Song>> $completion) {
    final String _sql = "SELECT * FROM songs WHERE isLiked = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfAlbumCover = CursorUtil.getColumnIndexOrThrow(_cursor, "albumCover");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfReleaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseDate");
          final int _cursorIndexOfPopularity = CursorUtil.getColumnIndexOrThrow(_cursor, "popularity");
          final int _cursorIndexOfExternalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "externalUrl");
          final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLiked");
          final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistName");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final String _tmpAlbumCover;
            if (_cursor.isNull(_cursorIndexOfAlbumCover)) {
              _tmpAlbumCover = null;
            } else {
              _tmpAlbumCover = _cursor.getString(_cursorIndexOfAlbumCover);
            }
            final String _tmpPreviewUrl;
            if (_cursor.isNull(_cursorIndexOfPreviewUrl)) {
              _tmpPreviewUrl = null;
            } else {
              _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            }
            final int _tmpDuration;
            _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final String _tmpReleaseDate;
            if (_cursor.isNull(_cursorIndexOfReleaseDate)) {
              _tmpReleaseDate = null;
            } else {
              _tmpReleaseDate = _cursor.getString(_cursorIndexOfReleaseDate);
            }
            final int _tmpPopularity;
            _tmpPopularity = _cursor.getInt(_cursorIndexOfPopularity);
            final String _tmpExternalUrl;
            if (_cursor.isNull(_cursorIndexOfExternalUrl)) {
              _tmpExternalUrl = null;
            } else {
              _tmpExternalUrl = _cursor.getString(_cursorIndexOfExternalUrl);
            }
            final boolean _tmpIsLiked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIsLiked = _tmp != 0;
            final String _tmpPlaylistName;
            if (_cursor.isNull(_cursorIndexOfPlaylistName)) {
              _tmpPlaylistName = null;
            } else {
              _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
            }
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumCover,_tmpPreviewUrl,_tmpDuration,_tmpGenre,_tmpReleaseDate,_tmpPopularity,_tmpExternalUrl,_tmpIsLiked,_tmpPlaylistName);
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
  public Object getPlaylistSongs(final String playlistName,
      final Continuation<? super List<Song>> $completion) {
    final String _sql = "SELECT * FROM songs WHERE playlistName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (playlistName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, playlistName);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Song>>() {
      @Override
      @NonNull
      public List<Song> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfAlbumCover = CursorUtil.getColumnIndexOrThrow(_cursor, "albumCover");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
          final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
          final int _cursorIndexOfReleaseDate = CursorUtil.getColumnIndexOrThrow(_cursor, "releaseDate");
          final int _cursorIndexOfPopularity = CursorUtil.getColumnIndexOrThrow(_cursor, "popularity");
          final int _cursorIndexOfExternalUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "externalUrl");
          final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLiked");
          final int _cursorIndexOfPlaylistName = CursorUtil.getColumnIndexOrThrow(_cursor, "playlistName");
          final List<Song> _result = new ArrayList<Song>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Song _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpArtist;
            if (_cursor.isNull(_cursorIndexOfArtist)) {
              _tmpArtist = null;
            } else {
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final String _tmpAlbumCover;
            if (_cursor.isNull(_cursorIndexOfAlbumCover)) {
              _tmpAlbumCover = null;
            } else {
              _tmpAlbumCover = _cursor.getString(_cursorIndexOfAlbumCover);
            }
            final String _tmpPreviewUrl;
            if (_cursor.isNull(_cursorIndexOfPreviewUrl)) {
              _tmpPreviewUrl = null;
            } else {
              _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            }
            final int _tmpDuration;
            _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
            final String _tmpGenre;
            if (_cursor.isNull(_cursorIndexOfGenre)) {
              _tmpGenre = null;
            } else {
              _tmpGenre = _cursor.getString(_cursorIndexOfGenre);
            }
            final String _tmpReleaseDate;
            if (_cursor.isNull(_cursorIndexOfReleaseDate)) {
              _tmpReleaseDate = null;
            } else {
              _tmpReleaseDate = _cursor.getString(_cursorIndexOfReleaseDate);
            }
            final int _tmpPopularity;
            _tmpPopularity = _cursor.getInt(_cursorIndexOfPopularity);
            final String _tmpExternalUrl;
            if (_cursor.isNull(_cursorIndexOfExternalUrl)) {
              _tmpExternalUrl = null;
            } else {
              _tmpExternalUrl = _cursor.getString(_cursorIndexOfExternalUrl);
            }
            final boolean _tmpIsLiked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIsLiked = _tmp != 0;
            final String _tmpPlaylistName;
            if (_cursor.isNull(_cursorIndexOfPlaylistName)) {
              _tmpPlaylistName = null;
            } else {
              _tmpPlaylistName = _cursor.getString(_cursorIndexOfPlaylistName);
            }
            _item = new Song(_tmpId,_tmpTitle,_tmpArtist,_tmpAlbum,_tmpAlbumCover,_tmpPreviewUrl,_tmpDuration,_tmpGenre,_tmpReleaseDate,_tmpPopularity,_tmpExternalUrl,_tmpIsLiked,_tmpPlaylistName);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
