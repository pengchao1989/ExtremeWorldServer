package com.jixianxueyuan.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.entity.Video;
import com.jixianxueyuan.rest.dto.VideoDTO;
import com.jixianxueyuan.service.VideoService;

@RestController
@RequestMapping(value = "/api/secure/v1/video")
public class VideoRestController
{
	private static Logger logger = LoggerFactory.getLogger(VideoRestController.class);
	
	@Autowired
	VideoService videoService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public List<VideoDTO> list()
	{
		List<Video> videoEntityList = videoService.getAll();
		
		List<VideoDTO> videoDTOs = BeanMapper.mapList(videoEntityList, VideoDTO.class);
		return videoDTOs;
	}
}
