package com.jixianxueyuan.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//type=4  typec="video"

@Entity
@DiscriminatorValue("4")
public class Video extends Topic
{
	
}
