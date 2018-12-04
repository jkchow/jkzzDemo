package com.jkzz.captch;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

public class Captcha {
    private static int WIDTH;  //模板图宽度
    private static int X;  //抠图坐标x
    private static int Y;  //抠图坐标y
    private static int HEIGHT;  //模板图高度

    public static void main(String[] args) {
        try {
            X = (int) (Math.random() * (320-120) + 60); //距离左上角x的位置
           // X=319;
            System.out.println(X);
            //int num = (int) (Math.random() * 8 + 1);//选择随机原图图片
            //String captchaPath = "E:/test/"+num+".jpg";
            String targetFilePath = "E:/test/9.jpg"; //原图位置
            String  templateFilePath= "E:/test/beta/999.png"; //模板位置
            File templateFile = new File(templateFilePath);  //模板图片
            File targetFile = new File(targetFilePath);  //原图
            InputStream oriis = new FileInputStream(targetFile);// 源文件流
            // 模板图
            BufferedImage imageTemplate = ImageIO.read(templateFile);
            WIDTH = imageTemplate.getWidth();
            HEIGHT = imageTemplate.getHeight();
            // 最终图像 切出来的图像
            BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, imageTemplate.getType());
            Graphics2D graphics = newImage.createGraphics();
            graphics.setBackground(Color.white);
            int bold = 5;
            String oriFiletype = "jpg";
            // 获取感兴趣的目标区域
            BufferedImage targetImageNoDeal = getTargetArea(X, Y, WIDTH, HEIGHT, oriis, oriFiletype);
            // 根据模板图片抠图
            newImage = DealCutPictureByTemplate(targetImageNoDeal, imageTemplate, newImage);
            // 设置“抗锯齿”的属性
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            graphics.drawImage(newImage, 0, 0, null);
            graphics.dispose();

            //ImageIO.write(newImage, "png", new File("E:/test/ap/cut.png"));//写到文件中
            ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
            ImageIO.write(newImage, "png", os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
            byte[] newImages = os.toByteArray();//byte 切割出来的图片的 数组


            BufferedImage originImage = ImageIO.read(targetFile);//原图
            Graphics2D g = originImage.createGraphics();
            g.drawImage(originImage, 0, 0, originImage.getWidth(), originImage.getHeight(), null);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            g.drawImage(imageTemplate, X, Y, imageTemplate.getWidth(), imageTemplate.getHeight(), null);
            g.dispose();
            //ImageIO.write(originImage, "jpg", new File("E:/test/ap/bg.jpg"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(originImage, "jpg", out);
            byte[] originImages = out.toByteArray();//背景图

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }


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
                if (rgb != 16777215 && rgb <= 0) {
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
}
