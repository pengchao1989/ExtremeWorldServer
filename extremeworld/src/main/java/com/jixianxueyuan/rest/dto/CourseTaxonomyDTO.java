package com.jixianxueyuan.rest.dto;

import java.util.Date;
import java.util.List;

public class CourseTaxonomyDTO
{
	private Long id;
	private String name;
	private String des;
	private Date createTime;
	private String background;
	
	private List<CourseMinDTO> courses;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public Date getCreateTime()
	{
		return createTime;
	}
	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public List<CourseMinDTO> getCourses() {
		return courses;
	}

	public void setCourses(List<CourseMinDTO> courses) {
		this.courses = courses;
	}
}
