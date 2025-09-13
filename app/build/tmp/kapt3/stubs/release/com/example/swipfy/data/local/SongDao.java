package com.example.swipfy.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0018\u0010\u000f\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u001e\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016\u00a8\u0006\u0017"}, d2 = {"Lcom/example/swipfy/data/local/SongDao;", "", "addToPlaylist", "", "id", "", "playlistName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllSongs", "", "Lcom/example/swipfy/data/models/Song;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLikedSongs", "getPlaylistSongs", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSongById", "insert", "song", "(Lcom/example/swipfy/data/models/Song;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLikeStatus", "isLiked", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
@androidx.room.Dao()
public abstract interface SongDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.swipfy.data.models.Song song, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM songs WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSongById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.swipfy.data.models.Song> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM songs WHERE isLiked = 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLikedSongs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM songs WHERE playlistName = :playlistName")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPlaylistSongs(@org.jetbrains.annotations.NotNull()
    java.lang.String playlistName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion);
    
    @androidx.room.Query(value = "UPDATE songs SET isLiked = :isLiked WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLikeStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String id, boolean isLiked, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE songs SET playlistName = :playlistName WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object addToPlaylist(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.Nullable()
    java.lang.String playlistName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM songs")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllSongs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.swipfy.data.models.Song>> $completion);
}