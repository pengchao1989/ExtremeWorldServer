package com.jixianxueyuan.web.topic;

import com.jixianxueyuan.entity.Reply;
import com.jixianxueyuan.entity.Topic;
import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.entity.Video;
import com.jixianxueyuan.service.ReplyService;
import com.jixianxueyuan.service.TopicService;
import com.jixianxueyuan.service.UserService;
import com.jixianxueyuan.service.account.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.jsoup.nodes.Document;

import javax.servlet.ServletRequest;

/**
 * Created by 23653 on 2017/2/12.
 */
@Controller
@RequestMapping(value = "{hobby}/topic")
public class TopicController {
    private static final String PAGE_SIZE = "10";

    @Autowired
    private TopicService topicService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detail(
            @PathVariable String hobby,
            @PathVariable("id") Long id,
            @RequestParam(value = "page", defaultValue = "1") int pageNumber,
            @RequestParam(value = "page.size", defaultValue = PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortType", defaultValue = "auto") String sortType,
            @RequestParam(value = "inviterId", defaultValue = "0") long inviterId,
            Model model, ServletRequest request) {

        //第一页显示主题内容
        if (pageNumber == 1) {

        }
        Topic topic = topicService.getTopic(id);


        //在此处理content中图片地址，以更适合在web端显示
        String newContent = replaceImageUrl(topic.getContent());

        System.out.print(newContent);

        //topic.setContent(newContent);


        Page<Reply> replys = replyService.getAll(id, pageNumber, pageSize);

        //邀请人
        if (inviterId > 0){
            User inviter = userService.getUser(inviterId);
            model.addAttribute("inviter", inviter);
        }

        if (topic instanceof Video) {
            model.addAttribute("type", "video");
            Long userId = getCurrentUserId();
            model.addAttribute("userId", userId);
        } else if (topic instanceof Topic) {
            model.addAttribute("type", "topic");
        }


        model.addAttribute("topic", topic);
        model.addAttribute("topicContent", newContent);
        model.addAttribute("replys", replys);

        model.addAttribute("hobby", hobby);

        return "topic/topicDetail";
    }

    private String replaceImageUrl(String content) {
        if (StringUtils.isNoneEmpty(content)) {
            String regexPIC_URL = "http://img\\.jixianxueyuan\\.com.*";

            Document contnetDocument = Jsoup.parseBodyFragment(content);

            Elements images = contnetDocument.getElementsByTag("img");
            for (Element img : images) {
                String src = img.attr("src");

                if (src.matches(regexPIC_URL) && (!src.contains("!webContentImg"))) {
                    img.attr("src", src + "!webContentImg");
                }

            }


            return contnetDocument.body().html();
        }

        return "";
    }

    private Long getCurrentUserId() {
        SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return securityUser.getId();
    }
}
