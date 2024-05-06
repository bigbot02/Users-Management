package com.xadmin.usermanagment.web;

import com.xadmin.usermanagment.bean.User;
import com.xadmin.usermanagment.dao.UserDao;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 private UserDao userDao;
	 
	// @see Servlet#init(ServletConfig)
	  public void init() throws ServletException {
	    userDao = new UserDao();
	  }
	 


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String action = request.getServletPath();
	    switch (action) {
	      case "/new":
	        showNewForm(request,response);
	        break;
	        case "/insert":
	        insertUser(request , response);
	        break;
	        case "/delete":
	        deleteUser(request , response);
	        break;
	        case "/edit":
	        showEditForm(request , response);
	        break;
	        case "/update":
	        updateUser(request , response);
	        break;
	        default :
	        listUser(request , response);

	        break; 
	    }
	}
	      private void showNewForm(HttpServletRequest request, HttpServletResponse response)throws ServletException , IOException{
	        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
	        dispatcher.forward(request , response);

	      }

	      private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	    try {
	    	        String name = request.getParameter("name");
	    	        String email = request.getParameter("email");
	    	        String country = request.getParameter("country");
	    	        User newUser = new User(name, email, country);

	    	        userDao.insertUser(newUser);
	    	        response.sendRedirect("list");
	    	    } catch (SQLException e) {
	    	        e.printStackTrace();
	    	        request.getRequestDispatcher("/Error.jsp").forward(request, response);
	    	    }
	    	}


	      private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException{
	        int id = Integer.parseInt(request.getParameter("id"));
	        try{
	          userDao.deleteUser(id);
	        }catch(Exception e){
	          e.printStackTrace();
	        }
	        response.sendRedirect("list");
	      }

	      private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	    int id = Integer.parseInt(request.getParameter("id"));
	    	    User existingUser;
	    	    try {
	    	        existingUser = userDao.selectUser(id);
	    	        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
	    	        request.setAttribute("user", existingUser);
	    	        dispatcher.forward(request, response);
	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	    	    }
	    	}

	      private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	    try {
	    	        int id = Integer.parseInt(request.getParameter("id"));
	    	        String name = request.getParameter("name");
	    	        String email = request.getParameter("email");
	    	        String country = request.getParameter("country");
	    	        User newUser = new User(id, name, email, country);

	    	        userDao.updateUser(newUser);
	    	        response.sendRedirect("list");
	    	    } catch (SQLException e) {
	    	        e.printStackTrace(); 
	    	        request.getRequestDispatcher("/Error.jsp").forward(request, response);
	    	    }
	    	}

	      
	      private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException{
	        
	          try{
	            List<User> listUser = userDao.SelectAllUsers();
	            request.setAttribute("listUser", listUser);
	            RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
	            dispatcher.forward(request, response);  
	          }catch(Exception e){
	            e.printStackTrace();
	          }
	      }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
