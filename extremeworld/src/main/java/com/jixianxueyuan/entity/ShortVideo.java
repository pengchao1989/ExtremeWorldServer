package com.jixianxueyuan.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("5")
public class ShortVideo extends Topic{

}
