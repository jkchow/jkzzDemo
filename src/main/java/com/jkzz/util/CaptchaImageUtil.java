package com.jkzz.util;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class CaptchaImageUtil {
    private static int WIDTH;  //模板图宽度
    private static int HEIGHT;  //模板图高度
    private static BufferedImage imageTemplate;  //模板流 缓存
    private static BufferedImage watermark;  //白边水印

    static {
        //String templateFilePath = "static/templates/template.png"; //模板位置
        //File templateFile = new File(templateFilePath);  //模板图片
        InputStream stream = (CaptchaImageUtil.class).getClassLoader().getResourceAsStream("static/templates/tempfinal.png");//项目中相对模板位置
        InputStream whiteStream = (CaptchaImageUtil.class).getClassLoader().getResourceAsStream("static/templates/pure.png");//项目中相对水印模板位置
        File templateFile = new File( "tempfinal.png");
        File whiteFile = new File( "pure.png");
        try {
            FileUtils.copyInputStreamToFile(stream, templateFile);
            imageTemplate = ImageIO.read(templateFile);
            FileUtils.copyInputStreamToFile(whiteStream, whiteFile);
            watermark = ImageIO.read(whiteFile);
            WIDTH = imageTemplate.getWidth();
            HEIGHT = imageTemplate.getHeight();
            if(templateFile.exists()){
                templateFile.delete();
            }
            if(whiteFile.exists()){
                whiteFile.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 返回base64 背景：bg  补丁：cut  X轴距离：x  Y轴距离：y
     * @param originImage 原图流
     * @param X;  //抠图坐标x
     * @param Y;  //抠图坐标y
     * @return
     */

    public static Map<String, String> getBgandCut(BufferedImage originImage, int X, int Y) {
        Map<String, String> map = new HashMap<>();
        ByteArrayOutputStream out = new ByteArrayOutputStream();//新建流。
        InputStream oriis =null;
        try {
            ImageIO.write(originImage, "jpg", out);
            oriis= new ByteArrayInputStream(out.toByteArray());
            out.reset();
            // 最终图像 切出来的图像
            BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, imageTemplate.getType());
            Graphics2D graphics = newImage.createGraphics();
            graphics.setBackground(Color.white);
            int bold = 6;
            // 获取感兴趣的目标区域
            BufferedImage targetImageNoDeal = getTargetArea(X, Y, WIDTH, HEIGHT, oriis, "jpg");
            // 根据模板图片抠图
            newImage = DealCutPictureByTemplate(targetImageNoDeal, imageTemplate, newImage);
            // 设置“抗锯齿”的属性
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawImage(newImage, 0, 0, null);
            graphics.dispose();

            //ImageIO.write(newImage, "png", new File("E:/test/nap/cut.png"));//写到文件中
            ImageIO.write(newImage, "png", new File("E:/test/zz/cut1.png"));//写到文件中
            //增加白色描边
            Graphics2D watercutG = newImage.createGraphics();
            watercutG.drawImage(newImage, 0, 0, newImage.getWidth(), newImage.getHeight(), null);
            watercutG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f));
            watercutG.drawImage(watermark, -1, 0,  null);//水印
            watercutG.dispose();
            //ImageIO.write(newImage, "png", new File("E:/test/nap/cut2.jpg"));
            ImageIO.write(newImage, "png", new File("E:/test/zz/cut2.jpg"));

            ImageIO.write(newImage, "png", out);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            byte[] newImages = out.toByteArray();//byte 切割出来的图片的 数组
            out.reset();
            // 加密
            BASE64Encoder encoder = new BASE64Encoder();
            String patch = encoder.encode(newImages);
            patch = patch.replaceAll("\r\n", "");
            map.put("patch", "data:img/jpg;base64," + patch);//补丁

            Graphics2D g = originImage.createGraphics();
            g.drawImage(originImage, 0, 0, originImage.getWidth(), originImage.getHeight(), null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
            g.drawImage(imageTemplate, X, Y, WIDTH, HEIGHT, null);
            g.dispose();
            //ImageIO.write(originImage, "jpg", new File("E:/test/nap/bg1.jpg"));
            ImageIO.write(originImage, "jpg", new File("E:/test/zz/bg1.jpg"));

            //增加白色描边
            Graphics2D waterbgG = originImage.createGraphics();
            waterbgG.drawImage(originImage, 0, 0, originImage.getWidth(), originImage.getHeight(), null);
            waterbgG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.95f));
            waterbgG.drawImage(watermark, X+2, Y, WIDTH, HEIGHT, null);//水印
            waterbgG.dispose();
            //ImageIO.write(originImage, "jpg", new File("E:/test/nap/bg2.jpg"));
            ImageIO.write(originImage, "jpg", new File("E:/test/zz/bg2.jpg"));

            ImageIO.write(originImage, "jpg", out);
            byte[] originImages = out.toByteArray();//背景图
            out.reset();
            String bg = encoder.encode(originImages);//背景
            map.put("bg", "data:img/jpg;base64," + bg.replaceAll("\r\n", ""));

            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oriis!=null) {
                try {
                    oriis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 获取目标区域
     *
     * @param x            随机切图坐标x轴位置
     * @param y            随机切图坐标y轴位置
     * @param targetWidth  切图后目标宽度
     * @param targetHeight 切图后目标高度
     * @param ois          源文件输入流
     * @return
     * @throws Exception
     */
    private static BufferedImage getTargetArea(int x, int y, int targetWidth, int targetHeight, InputStream ois,
                                               String filetype) throws Exception {
        Iterator<ImageReader> imageReaderList = ImageIO.getImageReadersByFormatName(filetype);
        ImageReader imageReader = imageReaderList.next();
        // 获取图片流
        ImageInputStream iis = ImageIO.createImageInputStream(ois);
        // 输入源中的图像将只按顺序读取
        imageReader.setInput(iis, true);

        ImageReadParam param = imageReader.getDefaultReadParam();
        Rectangle rec = new Rectangle(x, y, targetWidth, targetHeight);
        param.setSourceRegion(rec);
        BufferedImage targetImage = imageReader.read(0, param);
        return targetImage;
    }

    /**
     * 根据模板图片抠图
     *
     * @param oriImage
     * @param templateImage
     * @return
     */
    private static BufferedImage DealCutPictureByTemplate(BufferedImage oriImage, BufferedImage templateImage,
                                                          BufferedImage targetImage) throws Exception {
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);
        // 模板图像宽度

        for (int i = 0; i < templateImageData.length; i++) {
            // 模板图片高度
            for (int j = 0; j < templateImageData[0].length; j++) {
                // 如果模板图像当前像素点不是白色 copy源文件信息到目标图片中
                int rgb = templateImageData[i][j];
                if (rgb != 16777215 && rgb < 0) {
                    targetImage.setRGB(i, j, oriImageData[i][j]);
                }
            }
        }
        return targetImage;
    }

    /**
     * 生成图像矩阵
     *
     * @param
     * @return
     * @throws Exception
     */
    private static int[][] getData(BufferedImage bimg) throws Exception {
        int[][] data = new int[bimg.getWidth()][bimg.getHeight()];
        for (int i = 0; i < bimg.getWidth(); i++) {
            for (int j = 0; j < bimg.getHeight(); j++) {
                data[i][j] = bimg.getRGB(i, j);
            }
        }
        return data;
    }

    public static void main(String[] args) {
        int x = (int) (Math.random() * (320 - 120) + 60); //距离左上角x的位置
        int y = (int) (Math.random() * 55 + 10);//距离左上角y的位置
        //String targetFilePath = "E:/test/103.jpg"; //原图位置
        System.out.println(x);
        System.out.println(y);
       // Random random = new Random();
        int targetNo = (int) (Math.random() * 6 + 1);
        //File targetFile = new File(targetFilePath);  //原图
        InputStream  stream = (CaptchaImageUtil.class).getClassLoader().getResourceAsStream("static/origin/" + targetNo + ".jpg");
        File targetFile = new File(targetNo + ".jpg");
        BufferedImage originImage=null ;
        try {
            FileUtils.copyInputStreamToFile(stream, targetFile);
            originImage = ImageIO.read(targetFile);//原图
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, String> bgandCut = CaptchaImageUtil.getBgandCut(originImage, x, y);
        //System.out.println(bgandCut);
        if(targetFile.exists()){
            targetFile.delete();
        }
    }
}
