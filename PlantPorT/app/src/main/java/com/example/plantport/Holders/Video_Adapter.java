package com.example.plantport.Holders;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantport.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class Video_Adapter extends RecyclerView.ViewHolder   {

        public TextView date;
        SimpleExoPlayer exoPlayer;
        View mView;
        PlayerView playerView;
        ProgressBar progressBar;

        public Video_Adapter(View itemView) {

            super(itemView);
            mView = itemView;
            date = (TextView)mView.findViewById(R.id.videolistdate);
            progressBar = mView.findViewById(R.id.progress1);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,android.graphics.PorterDuff.Mode.MULTIPLY);

            playerView = (PlayerView)mView.findViewById(R.id.player_view);

        }

        public void setVideo(Application application, String url) {

            try {

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
//                DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = (SimpleExoPlayer)ExoPlayerFactory.newSimpleInstance(application);
                Uri uri = Uri.parse(url);
                DefaultHttpDataSourceFactory defaultHttpDataSourceFactory = new DefaultHttpDataSourceFactory("Plant Video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(uri,defaultHttpDataSourceFactory,extractorsFactory,null,null);
                playerView.setPlayer(exoPlayer);
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

