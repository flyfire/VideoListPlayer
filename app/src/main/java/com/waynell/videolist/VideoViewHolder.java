package com.waynell.videolist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.waynell.videolist.model.VideoListItem;
import com.waynell.videolist.target.VideoLoadTarget;
import com.waynell.videolist.target.VideoProgressTarget;
import com.waynell.videolist.ui.TextureVideoView;

import java.io.File;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;


public class VideoViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.video_view)
    public TextureVideoView videoView;

    @Bind(R.id.video_text)
    public TextView videoTitle;

    @Bind(R.id.video_cover)
    public ImageView videoCover;

    @Bind(R.id.video_progress)
    public CircularProgressBar progressBar;

    private final VideoProgressTarget progressTarget;
    private final VideoLoadTarget videoTarget;

    public VideoViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        videoTarget = new VideoLoadTarget(videoView, videoCover);
        progressTarget = new VideoProgressTarget(videoTarget, progressBar);
    }

    public void bind(VideoListItem item) {
        videoTarget.bind(item);
        videoTitle.setText(String.format("Video Position %s", item.getPosition()));
        progressTarget.setModel(item.getVideoUrl());
        Glide.with(itemView.getContext())
                .using(VideoListGlideModule.getOkHttpUrlLoader(), InputStream.class)
                .load(new GlideUrl(item.getVideoUrl()))
                .as(File.class)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(progressTarget);
    }
}
