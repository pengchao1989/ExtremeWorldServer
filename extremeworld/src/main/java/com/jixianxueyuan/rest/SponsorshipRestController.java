package com.jixianxueyuan.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springside.modules.beanvalidator.BeanValidators;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.web.MediaTypes;

import com.jixianxueyuan.config.HobbyPathConfig;
import com.jixianxueyuan.config.TradePayType;
import com.jixianxueyuan.config.TradeStutas;
import com.jixianxueyuan.config.TradeType;
import com.jixianxueyuan.entity.Sponsorship;
import com.jixianxueyuan.entity.Trade;
import com.jixianxueyuan.rest.dto.MyPage;
import com.jixianxueyuan.rest.dto.MyResponse;
import com.jixianxueyuan.rest.dto.SponsorshipDTO;
import com.jixianxueyuan.rest.dto.SponsorshipTradeDTO;
import com.jixianxueyuan.service.SponsorshipService;
import com.jixianxueyuan.service.TradeService;

@RestController
@RequestMapping(value = "/api/secure/v1/{hobby}/sponsorship")
public class SponsorshipRestController {
	private static final String PAGE_SIZE = "30";
	
	@Autowired
	SponsorshipService sponsorshipService;
	@Autowired
	TradeService tradeService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public MyResponse list(
			@PathVariable String hobby,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType){
		
		long hobbyId = HobbyPathConfig.getHobbyId(hobby);
		Page<Sponsorship> sponsorshipPage = sponsorshipService.getAllByHobbyId(hobbyId, pageNumber, pageSize, sortType);
		
		MyPage<SponsorshipDTO, Sponsorship> mySponsorshipPage = new MyPage<SponsorshipDTO, Sponsorship>(SponsorshipDTO.class, sponsorshipPage);
		
		return MyResponse.ok(mySponsorshipPage, true);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.JSON)
	public MyResponse create(@RequestBody Sponsorship sponsorship, UriComponentsBuilder uriBuilder){

		Trade trade = new Trade();
		trade.setInternalTradeNo(buildInternalTradeNo());
		trade.setTradeType(TradeType.SPONSORSHIP);
		trade.setPayType(TradePayType.ALIPAY);
		trade.setTradeStatus(TradeStutas.WAIT_BUYER_PAY);
		tradeService.save(trade);
		
		sponsorship.setTrade(trade);
		
		sponsorshipService.save(sponsorship);
		
		Sponsorship result = sponsorshipService.get(sponsorship.getId());
		SponsorshipTradeDTO dto = BeanMapper.map(result, SponsorshipTradeDTO.class);
		
		return MyResponse.ok(dto);
	}
	
    public String buildInternalTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 19);
        return key;
    }
}
