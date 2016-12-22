package com.jixianxueyuan.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//type=3 typec="discuss"
@Entity
@DiscriminatorValue("3")
public class Discussion extends Topic{

}
