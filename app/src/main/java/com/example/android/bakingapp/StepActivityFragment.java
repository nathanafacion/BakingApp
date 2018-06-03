package com.example.android.bakingapp;

import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.bakingapp.utils.Recipe;
import com.example.android.bakingapp.utils.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;


public class StepActivityFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepActivityFragment.class.getSimpleName();
    private static MediaSession mMediaSession;
    private SimpleExoPlayer mExoPlayer;
    static private ArrayList<Step> stepList;
    static private int position;
    static private Recipe recipe;
    private Step step;
    private SimpleExoPlayerView mPlayerView;
    private PlaybackState.Builder mStateBuilder;

    public StepActivityFragment() {
    }



    public static void previousStep(Context c){
        Class destinationClass = DetailRecipeActivity.class;
        Intent intentMovie = new Intent(c, destinationClass);
        intentMovie.putExtra("step", stepList);
        intentMovie.putExtra("position",position-1);
        intentMovie.putExtra("recipe",recipe);
        c.startActivity(intentMovie);

    }

    public static void previousStep(View v){
        Class destinationClass = StepActivity.class;
        Intent intentMovie = new Intent(v.getContext(), destinationClass);
        intentMovie.putExtra("step", stepList);
        intentMovie.putExtra("position",position-1);
        intentMovie.putExtra("recipe",recipe);
        v.getContext().startActivity(intentMovie);

    }

    public void nextStep(View v){
        Class destinationClass = StepActivity.class;
        Intent intentMovie = new Intent(v.getContext(), destinationClass);
        intentMovie.putExtra("step", stepList);
        intentMovie.putExtra("position",position+1);
        intentMovie.putExtra("recipe",recipe);
        v.getContext().startActivity(intentMovie);

    }

    public void setArguments(Step step, int position){
        this.step = step;
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intentDetailItem = getActivity().getIntent();

        // Initialize the player view.

        View rootView = inflater.inflate(R.layout.number_list_step_detail, container, false);
        if (intentDetailItem != null && intentDetailItem.hasExtra("position")) {

            position = intentDetailItem.getExtras().getInt("position");
            stepList = intentDetailItem.getExtras().getParcelableArrayList("step");
            recipe = intentDetailItem.getExtras().getParcelable("recipe");
            step = stepList.get(position);
        }

        mPlayerView = rootView.findViewById(R.id.playerView);
        if (step == null){
            return rootView;
        }

        if (step.getVideo_url().isEmpty() || step.getVideo_url().length() == 0) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                mPlayerView.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(Uri.parse(step.getVideo_url()));
            }

            TextView description = rootView.findViewById(R.id.tv_step_detail);
            description.setText(step.getDescription());

            if(stepList == null){
                rootView.findViewById(R.id.buttons_layout).setVisibility(View.GONE);
            } else {

                Button next = rootView.findViewById(R.id.bt_next);
                if (position == stepList.size()-1) {
                    next.setVisibility(View.INVISIBLE);
                } else {
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextStep(v);
                        }
                    });
                }
                Button previous = rootView.findViewById(R.id.bt_previous);
                if (position == 0) {
                    previous.setVisibility(View.INVISIBLE);
                } else {
                    previous.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            previousStep(v);
                        }
                    });
                }
            }

        return rootView;

    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSession(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackState.Builder()
                .setActions(
                        PlaybackState.ACTION_PLAY |
                                PlaybackState.ACTION_PAUSE |
                                PlaybackState.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSession.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    /**
     * Release the player when the activity is destroyed.
     */


    private void releasePlayer() {
        if (mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}