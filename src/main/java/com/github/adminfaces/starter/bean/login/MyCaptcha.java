/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author test
 */
public class MyCaptcha extends HttpServlet {

    private int height = 0;

    private int width = 0;

    public static final String CAPTCHA_KEY = "captcha_key_name";

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);

        height = Integer.parseInt(getServletConfig().getInitParameter("height"));
        width = Integer.parseInt(getServletConfig().getInitParameter("width"));
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Max-Age", 0);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        Color c = new Color(0.6662f, 0.4569f, 0.3232f);
        //graphics2D.setColor(Color.red);
        graphics2D.setBackground(c);
        graphics2D.clearRect(0,0 , image.getWidth(), image.getHeight());
        //Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
        Random r = new Random();
        String token = Long.toString(Math.abs(r.nextLong()), 36);
        String ch = token.substring(0, 6);
        //Color c = new Color(0.6662f, 0.4569f, 0.3232f);
        //GradientPaint gp = new GradientPaint(30, 30, c, 15, 25, Color.white, true);
        GradientPaint gp = new GradientPaint(30, 30, Color.black, 15, 25, Color.lightGray, true);
        graphics2D.setPaint(gp);
        
        //graphics2D.setColor(Color.red);
        Font font = new Font("Verdana", Font.CENTER_BASELINE, 26);        
        graphics2D.setFont(font);
        graphics2D.drawString(ch, 2, 20);
        graphics2D.dispose();

        HttpSession session = request.getSession(true);
        session.setAttribute(CAPTCHA_KEY, ch);

        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpeg", outputStream);
        outputStream.close();
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
