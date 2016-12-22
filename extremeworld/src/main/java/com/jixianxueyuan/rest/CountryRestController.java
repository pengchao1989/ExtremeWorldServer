package com.jixianxueyuan.rest;

import com.jixianxueyuan.rest.dto.CountryDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/secure/v1/country")
public class CountryRestController {

	@RequestMapping(method = RequestMethod.GET)
	CountryDTO get()
	{
		
		CountryDTO dto = new CountryDTO();
		dto.setName("china");
		dto.setNameZH("zhongguo");
		
		return dto;
	}
}
