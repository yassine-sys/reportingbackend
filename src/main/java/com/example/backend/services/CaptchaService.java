package com.example.backend.services;
import org.springframework.stereotype.Service;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class CaptchaService {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;

    public BufferedImage generateCaptchaImage(String captchaText) {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        Font font = new Font("Arial", Font.BOLD, 20);
        g2d.setFont(font);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        GradientPaint gp = new GradientPaint(0, 0,
                Color.CYAN, 0, HEIGHT / 2, Color.BLUE, true);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        g2d.setColor(new Color(255, 153, 0));

        Random r = new Random();
        int x = 0;
        int y;
        for (char c : captchaText.toCharArray()) {
            x += 10 + (Math.abs(r.nextInt()) % 15);
            y = 20 + Math.abs(r.nextInt()) % 20;
            g2d.drawChars(new char[]{c}, 0, 1, x, y);
        }

        g2d.dispose();

        return bufferedImage;
    }
}

