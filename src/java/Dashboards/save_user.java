/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dashboards;

import Db.dbConn;
import Emails.SendEmails;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author GNyabuto
 */
public class save_user extends HttpServlet {
    HttpSession session;
    MessageDigest m;
    String fullname,phone,email,password,con_password,gender;
    int error_count=0;
    String errors="",message;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, NoSuchAlgorithmException, MessagingException {
         session = request.getSession();
         dbConn conn = new dbConn();
         
         fullname = request.getParameter("fullname");
         phone = request.getParameter("phone");
         email = request.getParameter("email");
         password = request.getParameter("password");
         con_password = request.getParameter("con_password");
         
         gender = request.getParameter("gender");
         
         if(phone.startsWith("7")){
             phone = "0"+phone;
         }
         message = "";
         error_count=0;
         if(fullname.length()<7 || fullname.length()>30){
             error_count++;
         errors+=error_count+". Enter you Fullname<br>";    
         }
         
//         if(!(email.contains("afyanyota.org") || email.contains("fhi360.org"))){
//             error_count++;
//             errors+=error_count+". Unauthenticated email entered. Enter the correct organizational email. <br>";
//         }
         if(!(email.contains("@") || email.contains("."))){
             error_count++;
             errors+=error_count+". Wrong email address entered. <br>";
         }
         
         if(phone.length()<10 || phone.length()>12 || !isNumeric(phone) || !phone.startsWith("07")){
             error_count++;
             errors+=error_count+". Wrong phone Number entered. It must be 07.......<br>";
         }
         
      if(password.length()<5){
         error_count++;
             errors+=error_count+".Enter a strong password<br>"; 
      } 
      
      if(!password.equals(con_password)){
        error_count++;
          errors+=error_count+". Password and Confirm passwords do not match<br>";    
      }
      
      
      if(error_count==0){
          // check and save user
          
          String checker = "SELECT id FROM user WHERE email=? || phone=?";
          conn.pst = conn.conn.prepareStatement(checker);
          conn.pst.setString(1, email);
          conn.pst.setString(2, phone);
          
          conn.rs = conn.pst.executeQuery();
          if(conn.rs.next()){
              error_count++;
              errors="Email or Phone already exist in our system. Contact system developers for help";
          }
          else{
              //add user
              
                //encrypt the password
             m = MessageDigest.getInstance("MD5");
            m.update(password.getBytes(), 0, password.length());
            password = new BigInteger(1, m.digest()).toString(16);
            
              String add_user = "INSERT INTO user (fullname,email,phone,password,gender,level,status) VALUES (?,?,?,?,?,?,?)";
              conn.pst = conn.conn.prepareStatement(add_user);
              conn.pst.setString(1, fullname);
              conn.pst.setString(2, email);
              conn.pst.setString(3, phone);
              conn.pst.setString(4, password);
              conn.pst.setString(5, gender);
              conn.pst.setString(6, "2");
              conn.pst.setString(7, "0");
              
              conn.pst.executeUpdate();
              message = "<font color=\"green\"><b>User registration was successful. You will be notified once your request has been approved.</b></font>";
              
              //send email
              SendEmails sm=new SendEmails();
              String title="Dashboard Account Activation Request";
              String msg="Hi Admin,\n"
                      + "Kindly Approve my dashboards account. \n"
                      + "Name: "+fullname+"\n"
                      + "Email: "+email+"\n"
                      + "Phone Number: "+phone+" \n"
                      + "http://hsdsacluster2.fhi360.org:8080/Dashboards/check_user.jsp\n\n"
                      + "THANK YOU";
//             String title2="Email Activation title2";
              sm.Sendemail(title,msg,title," GNyabuto@fhi360.org");
//              sm.Sendemail(title,msg,title2," GNyabuto@fhi360.org,EKaunda@fhi360.org");
             
          }
      }
      
      if(error_count>0){
          message = "<font color=\"red\"><b>Correct the following errors:</b></font><br>"+errors+"<br><b style=\"margin-left:37%;\">THANK YOU</b>";
      }
      
     
      session.setAttribute("response",message);
      
      response.sendRedirect("register.jsp");
         
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
        try {
            processRequest(request, response);
        } catch (SQLException | NoSuchAlgorithmException | MessagingException ex) {
            Logger.getLogger(save_user.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException | NoSuchAlgorithmException | MessagingException ex) {
            Logger.getLogger(save_user.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
}
