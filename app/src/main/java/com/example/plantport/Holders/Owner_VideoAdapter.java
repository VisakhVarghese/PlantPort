package com.example.plantport.Holders;

;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.drm.DrmStore;
import android.graphics.drawable.Drawable;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class Owner_VideoAdapter extends RecyclerView.ViewHolder {

    public TextView datee;
    public SimpleExoPlayer exoPlayer;
    public PlayerView playerView;
    ImageView btnFullscreen;
    ProgressBar progressBar;
    Context context,mContext;
    FloatingActionButton fbn;
    androidx.appcompat.app.ActionBar actionBar;
    Activity activity;
    View mView;
    boolean fullscreen = false;



    public Owner_VideoAdapter(@NonNull View itemView, Context context, ActionBar actionBar,FloatingActionButton fbn) {
        super(itemView);

        this.context = context;
        this.mContext = itemView.getContext();
        this.actionBar = actionBar;
        this.fbn = fbn;

        activity = (Activity) itemView.getContext();




        mView = itemView;
        playerView = (PlayerView)mView.findViewById(R.id.player_view_1);
        datee = (TextView)mView.findViewById(R.id.videolistdate_1);
//        btnFullscreen = playerView.findViewById(R.id.btn_fullscreen);
        progressBar = (ProgressBar)mView.findViewById(R.id.progress_barvideo);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,android.graphics.PorterDuff.Mode.MULTIPLY);

//        ((Activity) context)


    }




    public void setVideo(Application application, String url) {

        try {

//            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

            // Initialize load control
            LoadControl loadControl = new DefaultLoadControl();
            //Initialize Band Width control
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//                    .Builder(application).build();
//                DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
            //initialize track selector
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application,trackSelector,loadControl);
            Uri uri = Uri.parse(url);
            DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("Plant Video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(uri,defaultHttpDataSourceFactory,extractorsFactory,null,null);

            playerView.setPlayer(exoPlayer);
            playerView.setKeepScreenOn(true);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if (playbackState == Player.STATE_BUFFERING){

                        progressBar.setVisibility(View.VISIBLE);
                    }else if (playbackState == Player.STATE_READY){

                        progressBar.setVisibility(View.INVISIBLE );
                    }

                }

                @Override
                public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

                }

                @Override
                public void onIsPlayingChanged(boolean isPlaying) {

                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

//            btnFullscreen.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(fullscreen){
//
//                        btnFullscreen.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_fullscreen));
//                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//
//                        if (actionBar != null){
//
//                            actionBar.show();
//                        }
//                        fbn.show();
//                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
//                        params.width = params.MATCH_PARENT;
//                        params.height =600;
//                        playerView.setLayoutParams(params);
//                        fullscreen = false;
//
//                    }else {
//
//                        btnFullscreen.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_fexit));
//                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//                        if (actionBar != null){
//
//                            actionBar.hide();
//
//                        }
//                        fbn.hide();
//                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)playerView.getLayoutParams();
//                        params.width = params.MATCH_PARENT;
//                        params.height =params.MATCH_PARENT;
//                        playerView.setLayoutParams(params);
//                        fullscreen = true;
//
//                    }
//                }
//            });



        } catch (Exception exception) {

            String stringBuilder = "Exoplayer Error" + exception.toString();
            Log.e("OwnerVideoAdapterList", stringBuilder);
        }
    }


}


