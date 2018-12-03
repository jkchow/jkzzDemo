package com.jkzz.demo;

import com.maimaicn.utils.controller.BaseController;
import com.maimaicn.utils.permission.Subject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
public class CaptchaController extends BaseController {

    @RequestMapping("captcha/img")
    public String captchaImg(HttpServletRequest request) {
        try {
            Map<String, Object> map = new HashMap<>();

            int x = (int) (Math.random() * (320 - 125) + 50);
            int y = (int) (Math.random() * 65 + 10);

            //int num = (int) (Math.random() * 8 + 1);//选择随机图片
            //String captchaPath = "E:/test/"+num+".jpg";
            String captchaPath = "E:/test/9.jpg";

            byte[] cut = this.cut(x, y, 45, 45, captchaPath);
            // 加密
            BASE64Encoder encoder = new BASE64Encoder();
            String patch = encoder.encode(cut);
            patch = patch.replaceAll("\r\n", "");
            map.put("patch", "data:img/jpg;base64," + patch);//补丁
            byte[] water = this.water(x, y, 45, 45, captchaPath);
            String bg = encoder.encode(water);//背景
            map.put("bg", "data:img/jpg;base64," + bg.replaceAll("\r\n", ""));
            map.put("y", y);
            map.put("imgx", 320);
            map.put("imgy", 130);
            HttpSession session = request.getSession();
            System.out.println(session);

            // session.setAttribute("x", x);
            return jsonData(request, 0, map);
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request, 110, "服务忙");
        }
    }

    @RequestMapping("/captcha/check")
    public String checkCaptcha(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int x = (int) request.getSession().getAttribute("x");
            Integer point = (Integer) map.get("point");
            Integer times = (Integer) map.get("timespan");
            // 位置限制像素
            if (point < x - 3 || point > x + 3) {
                return jsonData(request, 110, "校验失败，请重试！");
            }
            // 时间限制
            if (times < 100) {
                return jsonData(request, 110, "校验失败，请重试！");
            }
            request.getSession().setAttribute("x", 1);
            return jsonData(request, 0, "成功");
        } catch (Exception e) {
            log.error("服务忙", e);
            return jsonData(request, 110, "服务忙");
        }
    }

    // 水印
    public static byte[] water(int x, int y, int width, int height, String sourcePath) {
        BufferedImage bi = null;
        BufferedImage image = null;
        try {
            bi = ImageIO.read(new File(sourcePath));
            image = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = image.createGraphics();
            g.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);

            BufferedImage water = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
            g.drawImage(water, x, y, width, height, null);
            g.dispose();

            ImageIO.write(image, "jpg", new File("E:/test/beta/test1.jpg"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", out);
            byte[] byteArray = out.toByteArray();
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bi != null) {
                bi.flush();
            }
            if (image != null) {
                image.flush();
            }

        }
        return null;
    }

    /**
     * 图片裁切
     *
     * @param x1         选择区域左上角的x坐标
     * @param y1         选择区域左上角的y坐标
     * @param width      选择区域的宽度
     * @param height     选择区域的高度
     * @param sourcePath 源图片路径
     */


    public static byte[] cut(int x1, int y1, int width, int height, String sourcePath) {
        FileInputStream is = null;
        ImageInputStream iis = null;

        try {
            is = new FileInputStream(sourcePath);
            String fileSuffix = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(fileSuffix);
            ImageReader reader = it.next();
            iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x1, y1, width, height);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, "jpg", new File("E:/test/beta/test2.jpg"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bi, fileSuffix, out);
            byte[] byteArray = out.toByteArray();
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (iis != null) {
                    iis.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();

        int x = (int) (Math.random() * (320 - 125) + 50);
        int y = (int) (Math.random() * 65 + 10);

        //int num = (int) (Math.random() * 8 + 1);//选择随机图片
        //String captchaPath = "E:/test/"+num+".jpg";
        String captchaPath = "E:/test/9.jpg";

        byte[] cut = cut(x, y, 45, 45, captchaPath);
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        String patch = encoder.encode(cut);
        patch = patch.replaceAll("\r\n", "");
        map.put("patch", "data:img/jpg;base64," + patch);//补丁
        byte[] water = water(x, y, 45, 45, captchaPath);
        String bg = encoder.encode(water);//背景
        map.put("bg", "data:img/jpg;base64," + bg.replaceAll("\r\n", ""));
        map.put("y", y);
        map.put("imgx", 320);
        map.put("imgy", 130);


    }
}
