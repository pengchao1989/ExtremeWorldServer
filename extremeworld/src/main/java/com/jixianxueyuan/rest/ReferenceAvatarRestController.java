package com.jixianxueyuan.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.entity.ReferenceAvatar;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.service.ReferenceAvatarService;

@RestController
@RequestMapping(value = "/api/v1/reference_avatar")
public class ReferenceAvatarRestController {
	
	@Autowired
	ReferenceAvatarService referenceAvatarService;

	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse get(){
		
		ReferenceAvatar referenceAvatar = referenceAvatarService.getRandom();
		return MyResponse.ok(referenceAvatar);
	}
}
