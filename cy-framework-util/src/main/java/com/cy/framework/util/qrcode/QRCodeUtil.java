package com.cy.framework.util.qrcode;

import com.cy.framework.util.json.JSONUtils;
import com.cy.framework.util.safe.DesUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码工具类
 *
 */
public class QRCodeUtil {

	private static final String CHARSET = "utf-8";
	private static final String FORMAT_NAME = "JPG";
	// 二维码尺寸
	private static final int size = 300;
	// LOGO宽度
	private static final int WIDTH = 60;
	// LOGO高度
	private static final int HEIGHT = 60;
	private static Logger log = Logger.getLogger(QRCodeUtil.class);

	private static BufferedImage createImage(String content, String imgPath, boolean needCompress) {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (imgPath != null && !imgPath.isEmpty()) {
			// 插入图片
			QRCodeUtil.insertImage(image, imgPath, needCompress);
		}
		return image;
	}

	/**
	 * 插入LOGO
	 *
	 * @param source
	 *            二维码图片
	 * @param imgPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @throws Exception
	 */
	private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) {
		File file = new File(imgPath);
		if (!file.exists()) {
			log.warn("" + imgPath + "   该文件不存在！");
			return;
		}
		Image src = null;
		try {
			src = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > WIDTH) {
				width = WIDTH;
			}
			if (height > HEIGHT) {
				height = HEIGHT;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (size - width) / 2;
		int y = (size - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 *
	 * @param content
	 *            内容
	 * @param imgPath
	 *            LOGO地址
	 * @param destPath
	 *            存放目录
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static boolean encode(String content, String imgPath, String destPath, boolean needCompress) {
		boolean ret = false;
		BufferedImage image = null;
		try {
			image = QRCodeUtil.createImage(content, imgPath, needCompress);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String dir = null;
		if (destPath.contains("/")) {
			dir = destPath.substring(0, destPath.lastIndexOf("/"));
		} else if (destPath.contains("\\")) {
			dir = destPath.substring(0, destPath.lastIndexOf("\\"));
		} else {
			return ret;
		}
		if (mkdirs(dir)) {
			try {
				ret = ImageIO.write(image, FORMAT_NAME, new File(destPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
	 *
	 * @date 2013-12-11 上午10:16:36
	 * @param destPath
	 *            存放目录
	 */
	public static boolean mkdirs(String destPath) {
		File file = new File(destPath);
		boolean ret = true;
		// 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
		if (!file.exists() && !file.isDirectory()) {
			ret = file.mkdirs();
		}
		return ret;
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 *
	 * @param content
	 *            内容
	 * @param imgPath
	 *            LOGO地址
	 * @param destPath
	 *            存储地址
	 * @throws Exception
	 */
	public static boolean encode(String content, String imgPath, String destPath) throws Exception {
		return QRCodeUtil.encode(content, imgPath, destPath, false);
	}

	/**
	 * 生成二维码
	 *
	 * @param content
	 *            内容
	 * @param destPath
	 *            存储地址
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static boolean encode(String content, String destPath, boolean needCompress) throws Exception {
		return QRCodeUtil.encode(content, null, destPath, needCompress);
	}

	/**
	 * 生成二维码
	 *
	 * @param content
	 *            内容
	 * @param destPath
	 *            存储地址
	 * @throws Exception
	 */
	public static boolean encode(String content, String destPath) throws Exception {
		return QRCodeUtil.encode(content, null, destPath, false);
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 *
	 * @param content
	 *            内容
	 * @param imgPath
	 *            LOGO地址
	 * @param output
	 *            输出流
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public static boolean encode(String content, String imgPath, OutputStream output, boolean needCompress)
			throws Exception {
		BufferedImage image = QRCodeUtil.createImage(content, imgPath, needCompress);
		return ImageIO.write(image, FORMAT_NAME, output);
	}

	/**
	 * 生成二维码
	 *
	 * @param content
	 *            内容
	 * @param output
	 *            输出流
	 * @throws Exception
	 */
	public static boolean encode(String content, OutputStream output) throws Exception {
		return QRCodeUtil.encode(content, null, output, false);
	}

	/**
	 * 解析二维码
	 *
	 * @param file
	 *            二维码图片
	 * @return
	 * @throws Exception
	 */
	public static String decode(File file) throws Exception {
		BufferedImage image;
		image = ImageIO.read(file);
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	/**
	 * 解析二维码
	 *
	 * @param path
	 *            二维码图片地址
	 * @return
	 * @throws Exception
	 */
	public static String decode(String path) throws Exception {
		return QRCodeUtil.decode(new File(path));
	}

	public static boolean encodeMap(Object obj, String logo, String imagePath) throws Exception {
		String content = null;
		if (obj != null) {
			if (obj instanceof Map) {
				content = JSONUtils.obj2Json(obj).toString();
			} else if (obj instanceof String) {
				content = obj.toString();
			}
			if (content != null) {
				content = DesUtil.encrypt(content);
			}
		}
		return QRCodeUtil.encode(content, logo, imagePath, true);
	}

	public static void main(String[] args) throws Exception {
		String text = "http://local.wanhengmall.com/view/around/aroundDetail/aroundStore.html?store_id=6";
		System.out.println(QRCodeUtil.encode(text, "d://img/images.jpg",
				"D://tomcat/webapps/localservicesys/uploadfile/8ce6790c-c6a9-3e65-b17f-908f462fae85.jpg", true));
	}
}
