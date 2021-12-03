package com.newlecture.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// �˻�
		String field = request.getParameter("f") != null && !request.getParameter("f").equals("") ? request.getParameter("f") : "title"; // select option
		String query = request.getParameter("q") != null && !request.getParameter("q").equals("") ? request.getParameter("q") : ""; // �˻���
		
		// ������
		int page = request.getParameter("p") != null && !request.getParameter("p").equals("") ? Integer.parseInt(request.getParameter("p")) : 1; 
		
		NoticeService service = new NoticeService();
		List<NoticeView> list = service.getNoticeList(field, query, page);
		
		// �˻��� ���� ī��Ʈ
		int count = service.getNoticeCount(field, query);
		
		request.setAttribute("list", list);
		request.setAttribute("count", count);
		
		request.getRequestDispatcher("/WEB-INF/view/notice/list.jsp").forward(request, response);
	}
}