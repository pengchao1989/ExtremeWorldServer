package com.jixianxueyuan.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//type=2  typec="mood"
@Entity
@DiscriminatorValue("2")
public class Mood extends Topic{

}
