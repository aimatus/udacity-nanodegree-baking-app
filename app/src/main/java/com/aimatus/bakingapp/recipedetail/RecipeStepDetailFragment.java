package com.aimatus.bakingapp.recipedetail;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aimatus.bakingapp.R;
import com.aimatus.bakingapp.model.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Baking App Project
 * Udacity Associate Android Developer Fast Track Nanodegree Program
 * October 2017
 *
 * @author Abraham Matus
 */
public class RecipeStepDetailFragment extends Fragment {

    static int stepIndex;
    static Recipe recipe;
    static boolean isLargeScreen = false;

    @BindView(R.id.tv_step_description)
    TextView descriptionTextView;

    @BindView(R.id.tv_no_video_available)
    TextView noVideoTextView;

    @BindView(R.id.b_previous_button)
    Button previousStepButton;

    @BindView(R.id.b_next_button)
    Button nextStepButton;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.iv_thumbnail_image)

    ImageView mThumbnailImageView;
    SimpleExoPlayer mExoPlayer;
    private long exoPlayerPosition;
    private boolean exoPlayerPlayWhenReady;


    public RecipeStepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            stepIndex = savedInstanceState.getInt(getString(R.string.step_index_tag));
            recipe = (Recipe) savedInstanceState.getSerializable(getString(R.string.recipe_tag));
            exoPlayerPosition = savedInstanceState.getLong(getString(R.string.exo_player_position_tag));
            exoPlayerPlayWhenReady = savedInstanceState.getBoolean(getString(R.string.exo_player_play_when_ready_tag));
        } else {
            getParamsFromActivity();
            exoPlayerPlayWhenReady = true;
        }

        descriptionTextView.setText(recipe.getSteps().get(stepIndex).getDescription());
        initializeMultimedia();
        initializeNavButtons();
        setButtonsClickListener();
        setFullscreenVideoConfigurationIfLandscapeAndSmallScreen();

        return rootView;
    }

    private void getParamsFromActivity() {
        if (getActivity() instanceof RecipeStepDetailActivity) {
            RecipeStepDetailActivity activity = (RecipeStepDetailActivity) getActivity();
            stepIndex = activity.stepIndex;
            recipe = activity.recipe;
        }
    }

    private void initializeMultimedia() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
        }

        String videoURL = recipe.getSteps().get(stepIndex).getVideoURL();

        if (videoURL != null && !videoURL.isEmpty()) {
            initializeExoPlayer(videoURL);
        } else {
            initializeThumbnailImage();
        }
    }

    private void initializeExoPlayer(String videoURL) {
        noVideoTextView.setVisibility(View.GONE);
        mThumbnailImageView.setVisibility(View.GONE);
        mPlayerView.setVisibility(View.VISIBLE);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(videoURL),
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);

        mExoPlayer.prepare(mediaSource);
        mExoPlayer.seekTo(exoPlayerPosition);
        mExoPlayer.setPlayWhenReady(exoPlayerPlayWhenReady);
    }

    private void initializeThumbnailImage() {
        String recipeThumbnailUrl = recipe.getSteps().get(stepIndex).getThumbnailURL();
        if (recipeThumbnailUrl != null && !recipeThumbnailUrl.isEmpty()) {
            Picasso.get()
                    .load(recipeThumbnailUrl)
                    .placeholder(R.drawable.invalid_thumbnail)
                    .error(R.drawable.invalid_thumbnail)
                    .into(mThumbnailImageView);
            mThumbnailImageView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        } else {
            mThumbnailImageView.setVisibility(View.GONE);
            noVideoTextView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        }
    }

    private void initializeNavButtons() {
        if (stepIndex == 0) {
            previousStepButton.setEnabled(false);
        } else {
            nextStepButton.setEnabled(true);
        }
    }

    private void setButtonsClickListener() {
        setNextStepButtonClickListener();
        setPreviousStepButtonClickListener();
    }

    private void setFullscreenVideoConfigurationIfLandscapeAndSmallScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !isLargeScreen) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            mPlayerView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, height));
            nextStepButton.setVisibility(View.GONE);
            previousStepButton.setVisibility(View.GONE);
        }
    }

    private void setPreviousStepButtonClickListener() {
        previousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepIndex--;
                descriptionTextView.setText(recipe.getSteps().get(stepIndex).getDescription());
                initializeMultimedia();
                if (stepIndex == 0) {
                    nextStepButton.setEnabled(true);
                    previousStepButton.setEnabled(false);
                } else {
                    nextStepButton.setEnabled(true);
                    previousStepButton.setEnabled(true);
                }
                if (!isLargeScreen) {
                    getActivity().setTitle(recipe.getSteps().get(stepIndex).getShortDescription());
                }

            }
        });
    }

    private void setNextStepButtonClickListener() {
        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepIndex++;
                descriptionTextView.setText(recipe.getSteps().get(stepIndex).getDescription());
                initializeMultimedia();
                if (stepIndex == recipe.getSteps().size() - 1) {
                    nextStepButton.setEnabled(false);
                    previousStepButton.setEnabled(true);
                } else {
                    nextStepButton.setEnabled(true);
                    previousStepButton.setEnabled(true);
                }
                if (!isLargeScreen) {
                    getActivity().setTitle(recipe.getSteps().get(stepIndex).getShortDescription());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.step_index_tag), stepIndex);
        outState.putSerializable(getString(R.string.recipe_tag), recipe);

        if (mExoPlayer != null) {
            outState.putLong(getString(R.string.exo_player_position_tag), mExoPlayer.getCurrentPosition());
            outState.putBoolean(getString(R.string.exo_player_play_when_ready_tag), mExoPlayer.getPlayWhenReady());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            exoPlayerPlayWhenReady = mExoPlayer.getPlayWhenReady();
            exoPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMultimedia();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
