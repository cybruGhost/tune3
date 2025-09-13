package com.example.swipfy.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\f\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\b0\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\b0\u00132\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\b0\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\u0018\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0019\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u001c\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\b0\u00132\u0006\u0010\u001e\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/swipfy/data/repository/MusicRepository;", "", "songDao", "Lcom/example/swipfy/data/local/SongDao;", "(Lcom/example/swipfy/data/local/SongDao;)V", "addToLikedSongs", "", "song", "Lcom/example/swipfy/data/models/Song;", "(Lcom/example/swipfy/data/models/Song;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addToPlaylist", "playlistName", "", "(Lcom/example/swipfy/data/models/Song;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createPlaylist", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deletePlaylist", "getAllSongs", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLikedSongs", "getPlaylistSongs", "getRecommendedSongs", "getSongById", "songId", "getUserPlaylists", "removeFromLikedSongs", "removeFromPlaylist", "searchSongs", "query", "app_release"})
public final class MusicRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.swipfy.data.local.SongDao songDao = null;
    
    public MusicRepository(@org.jetbrains.annotations.NotNull()
    com.example.swipfy.data.local.SongDao songDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addToLikedSongs(@org.jetbrains.annotations.NotNull()
    com.example.swipfy.data.models.Song song, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object removeFromLikedSongs(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addToPlaylist(@org.jetbrains.annotations.NotNull()
    com.example.swipfy.data.models.Song song, @org.jetbrains.annotations.NotNull()
    java.lang.String playlistName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object removeFromPlaylist(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLikedSongs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getPlaylistSongs(@org.jetbrains.annotations.NotNull()
    java.lang.String playlistName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllSongs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getRecommendedSongs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUserPlaylists(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createPlaylist(@org.jetbrains.annotations.NotNull()
    java.lang.String playlistName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deletePlaylist(@org.jetbrains.annotations.NotNull()
    java.lang.String playlistName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchSongs(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getSongById(@org.jetbrains.annotations.NotNull()
    java.lang.String songId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.swipfy.data.models.Song> $completion) {
        return null;
    }
}