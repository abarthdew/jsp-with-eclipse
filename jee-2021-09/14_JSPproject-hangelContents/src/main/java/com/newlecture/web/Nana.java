package com.newlecture.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hangul")
public class Nana extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws IOException {
    res.setCharacterEncoding("UTF-8");
    PrintWriter out = res.getWriter();
    for (int i = 0; i < 100; i++) {
      out.println(i + " : �ȳ� Servlet !! </br>");
    }
  }
}
