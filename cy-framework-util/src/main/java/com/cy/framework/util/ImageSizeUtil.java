package com.cy.framework.util;/**
						* Created by WIN7 on 2017/5/10.
						*/

/**
 * 描述：
 *
 * @author yangchengfu
 * @dataTime 2017/5/10 11:22
 */
public class ImageSizeUtil {
	private int height = 200;
	private int width = 200;
	private String f = "?imageView2/1/w/";
	private String hh = "h";

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getImageSize(String imageName) {
		String image = getImageSize(imageName, getHeight(), getWidth());
		return image;
	}

	public String getImageSize(String imageName, int h, int w) {
		setHeight(h);
		setWidth(w);
		if (imageName != null && !imageName.isEmpty() && imageName.contains("?")) {
			String[] imageNames = imageName.split("\\?");
			imageName = imageNames[0];
		}
		StringBuilder image = new StringBuilder(imageName + f);
		image.append(getHeight()).append("/" + hh + "/").append(getWidth());
		return image.toString();
	}
}
