package com.example.plantport.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.plantport.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
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

import retrofit2.http.Url;

public class FullScreenView extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    TextView description,date,Descri,date_demo;
    ImageView fullScreen;
    String url;
    String Date,Description;
    LinearLayout layout;
    ProgressBar progressBar;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        playerView = findViewById(R.id.player_view_2);
        progressBar = findViewById(R.id.progress_barvideo_1);
        fullScreen = playerView.findViewById(R.id.btn_fullscreen);
        description = findViewById(R.id.des);
        Descri = findViewById(R.id.decri_video);
        date_demo = findViewById(R.id.date_demo);
        date = findViewById(R.id.date_full);
        layout = findViewById(R.id.linear_lay);

        Intent intent = getIntent();
        url = intent.getStringExtra("VideoURL");
        Date = intent.getStringExtra("Date");
        Description = intent.getStringExtra("Description");

        date.setText(Date);
        Descri.setText(Description);


        LoadControl loadControl = new DefaultLoadControl();

        //Initialize Band Width control
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        //initialize track selector
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(getApplicationContext(),trackSelector,loadControl);
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

        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        if(flag){

                            fullScreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_fullscreen));
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)playerView.getLayoutParams();
                            params.width = params.MATCH_PARENT;
                            params.height =700;
                            playerView.setLayoutParams(params);
                            description.setWidth(50);
                            description.setHeight(50);
                            Descri.setWidth(50);
                            Descri.setHeight(500);
                            date.setWidth(200);
                            date.setHeight(50);
                            date_demo.setWidth(200);
                            date_demo.setHeight(50);
                            flag = false;

                        }else {

                            fullScreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_fexit));
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)playerView.getLayoutParams();
                            params.width = params.MATCH_PARENT;
                            params.height =params.MATCH_PARENT;
                            playerView.setLayoutParams(params);
                            description.setWidth(0);
                            description.setHeight(0);
                            Descri.setWidth(0);
                            Descri.setHeight(0);
                            date.setWidth(0);
                            date.setHeight(0);
                            date_demo.setWidth(0);
                            date_demo.setHeight(0);
                            flag = true;
                        }
                    }
                });
    }
}