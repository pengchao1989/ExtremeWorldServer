package com.jixianxueyuan.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_video_detail")
public class VideoDetail extends IdEntity
{
	private String videoSource;
	private String thumbnail;
	
	public String getVideoSource()
	{
		return videoSource;
	}

	public void setVideoSource(String videoSource)
	{
		this.videoSource = videoSource;
	}
	public String getThumbnail()
	{
		return thumbnail;
	}
	public void setThumbnail(String thumbnail)
	{
		this.thumbnail = thumbnail;
	}
	
}
