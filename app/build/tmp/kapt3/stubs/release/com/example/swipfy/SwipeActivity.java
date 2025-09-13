package com.example.swipfy;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u0010\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u0010\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u0012\u0010\u0016\u001a\u00020\u000f2\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u000fH\u0014J\u0010\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\bH\u0002J\b\u0010\u001c\u001a\u00020\u000fH\u0002J\b\u0010\u001d\u001a\u00020\u000fH\u0002J\b\u0010\u001e\u001a\u00020\u000fH\u0002J\u0010\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0002J\u0010\u0010!\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u0010\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0013H\u0002J\b\u0010$\u001a\u00020\u000fH\u0002J\b\u0010%\u001a\u00020\u000fH\u0002J\u0010\u0010&\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/example/swipfy/SwipeActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "adapter", "Lcom/example/swipfy/SongAdapter;", "binding", "Lcom/example/swipfy/databinding/ActivitySwipeBinding;", "currentPosition", "", "player", "Landroidx/media3/exoplayer/ExoPlayer;", "songs", "", "Lcom/example/swipfy/data/models/Song;", "addToPlaylist", "", "song", "fetchSongs", "query", "", "handleDislike", "handleLike", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "playPreviewAt", "position", "setupListeners", "setupSearch", "setupViewPager", "showArtistInfo", "artistName", "showLyrics", "showToast", "message", "swipeToNext", "togglePlayback", "updateNowPlaying", "app_release"})
public final class SwipeActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.swipfy.databinding.ActivitySwipeBinding binding;
    private com.example.swipfy.SongAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.swipfy.data.models.Song> songs = null;
    @org.jetbrains.annotations.Nullable()
    private androidx.media3.exoplayer.ExoPlayer player;
    private int currentPosition = 0;
    
    public SwipeActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupViewPager() {
    }
    
    private final void setupListeners() {
    }
    
    private final void setupSearch() {
    }
    
    private final void swipeToNext() {
    }
    
    private final void fetchSongs(java.lang.String query) {
    }
    
    private final void handleLike(com.example.swipfy.data.models.Song song) {
    }
    
    private final void handleDislike(com.example.swipfy.data.models.Song song) {
    }
    
    private final void addToPlaylist(com.example.swipfy.data.models.Song song) {
    }
    
    private final void showLyrics(com.example.swipfy.data.models.Song song) {
    }
    
    private final void showArtistInfo(java.lang.String artistName) {
    }
    
    private final void togglePlayback() {
    }
    
    private final void playPreviewAt(int position) {
    }
    
    private final void updateNowPlaying(com.example.swipfy.data.models.Song song) {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void showToast(java.lang.String message) {
    }
}