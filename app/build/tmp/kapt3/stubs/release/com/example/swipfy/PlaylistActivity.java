package com.example.swipfy;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u001aB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0002J\u0010\u0010\r\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0002J\b\u0010\u000e\u001a\u00020\u000bH\u0002J\u0012\u0010\u000f\u001a\u00020\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u000bH\u0002J\b\u0010\u0013\u001a\u00020\u000bH\u0002J\b\u0010\u0014\u001a\u00020\u000bH\u0002J\u001e\u0010\u0015\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\t2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017H\u0002J\u0010\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/swipfy/PlaylistActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/example/swipfy/databinding/ActivityPlaylistBinding;", "playlistAdapter", "Lcom/example/swipfy/PlaylistActivity$PlaylistAdapter;", "playlists", "", "", "createPlaylist", "", "playlistName", "deletePlaylist", "loadPlaylists", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setupListeners", "setupRecyclerView", "showCreatePlaylistDialog", "showPlaylistContentDialog", "songs", "", "Lcom/example/swipfy/data/models/Song;", "showPlaylistSongs", "PlaylistAdapter", "app_release"})
public final class PlaylistActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.swipfy.databinding.ActivityPlaylistBinding binding;
    private com.example.swipfy.PlaylistActivity.PlaylistAdapter playlistAdapter;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<java.lang.String> playlists = null;
    
    public PlaylistActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void loadPlaylists() {
    }
    
    private final void setupListeners() {
    }
    
    private final void showCreatePlaylistDialog() {
    }
    
    private final void createPlaylist(java.lang.String playlistName) {
    }
    
    private final void showPlaylistSongs(java.lang.String playlistName) {
    }
    
    private final void showPlaylistContentDialog(java.lang.String playlistName, java.util.List<com.example.swipfy.data.models.Song> songs) {
    }
    
    private final void deletePlaylist(java.lang.String playlistName) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0013B\'\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\u0010\tJ\b\u0010\n\u001a\u00020\u000bH\u0016J\u001c\u0010\f\u001a\u00020\b2\n\u0010\r\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000bH\u0016J\u001c\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bH\u0016R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/example/swipfy/PlaylistActivity$PlaylistAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/swipfy/PlaylistActivity$PlaylistAdapter$PlaylistViewHolder;", "playlists", "", "", "onPlaylistClick", "Lkotlin/Function1;", "", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "PlaylistViewHolder", "app_release"})
    public static final class PlaylistAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.swipfy.PlaylistActivity.PlaylistAdapter.PlaylistViewHolder> {
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<java.lang.String> playlists = null;
        @org.jetbrains.annotations.NotNull()
        private final kotlin.jvm.functions.Function1<java.lang.String, kotlin.Unit> onPlaylistClick = null;
        
        public PlaylistAdapter(@org.jetbrains.annotations.NotNull()
        java.util.List<java.lang.String> playlists, @org.jetbrains.annotations.NotNull()
        kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onPlaylistClick) {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.example.swipfy.PlaylistActivity.PlaylistAdapter.PlaylistViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.ViewGroup parent, int viewType) {
            return null;
        }
        
        @java.lang.Override()
        public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
        com.example.swipfy.PlaylistActivity.PlaylistAdapter.PlaylistViewHolder holder, int position) {
        }
        
        @java.lang.Override()
        public int getItemCount() {
            return 0;
        }
        
        @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/example/swipfy/PlaylistActivity$PlaylistAdapter$PlaylistViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/swipfy/databinding/ItemPlaylistBinding;", "(Lcom/example/swipfy/PlaylistActivity$PlaylistAdapter;Lcom/example/swipfy/databinding/ItemPlaylistBinding;)V", "bind", "", "playlistName", "", "app_release"})
        public final class PlaylistViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            @org.jetbrains.annotations.NotNull()
            private final com.example.swipfy.databinding.ItemPlaylistBinding binding = null;
            
            public PlaylistViewHolder(@org.jetbrains.annotations.NotNull()
            com.example.swipfy.databinding.ItemPlaylistBinding binding) {
                super(null);
            }
            
            public final void bind(@org.jetbrains.annotations.NotNull()
            java.lang.String playlistName) {
            }
        }
    }
}