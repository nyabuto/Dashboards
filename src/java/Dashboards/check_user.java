/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dashboards;

import Db.dbConn;
import Emails.SendEmails;
import java.io.IOException;
import java.io.PrintWriter;
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
public class check_user extends HttpServlet {
HttpSession session;
String id,status,message;
String msg,title,email;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, MessagingException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           session = request.getSession();
           dbConn conn = new dbConn();
            SendEmails sm=new SendEmails();
//           if(session.getAttribute("level")!=null){
//             
//               if(session.getAttribute("level").toString().equals("1")){
                   //admin with rights to approve
                 id = request.getParameter("pos");
                 status = request.getParameter("state");
                 
                 //update database
                 
                 String update="UPDATE user SET status="+status+" WHERE id="+id+"";
                 conn.st.executeUpdate(update);
                  message = "Status Updated Successfully";
              
                  // get user email and name
                  String getdets = "select email,fullname FROM user WHERE id='"+id+"'";
                  conn.rs = conn.st1.executeQuery(getdets);
                  if(conn.rs.next()){
                  email = conn.rs.getString(1);
                  if(status.equals("1")){
                  msg = "Hi "+conn.rs.getString(2)+",\n\n"
                          + "GOOD NEWS\n\n"
                          + "Your Acount Access request has been approved. Click on the link below to access the system and login.\n\n"
                          + "http://hsdsacluster2.fhi360.org:8080/Dashboards\n\n"
                          + "THANK YOU";  
                  title="Dashboards Account Activation Approved";
                  
                  }
                  else{
                   
                      msg = "Hi "+conn.rs.getString(2)+",\n\n"
                          + "Your Acount Access request has been declined. Contact Data management team for further help.\n\n"
                          + ""
                          + "THANK YOU";  
                  title="Dashboards Account Activation Declined";
                  }
                      System.out.println("email: "+email);
                   sm.Sendemail(title,msg,title,email);
                   
                  }
                  else{
                   message = "";    
                  }
//               }
//           }
//           else{
//               message = "Error. You are not authorised to view this page";
//           }
           
            out.println(message);
        }
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
    } catch (SQLException ex) {
        Logger.getLogger(check_user.class.getName()).log(Level.SEVERE, null, ex);
    } catch (MessagingException ex) {
        Logger.getLogger(check_user.class.getName()).log(Level.SEVERE, null, ex);
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
    } catch (SQLException ex) {
        Logger.getLogger(check_user.class.getName()).log(Level.SEVERE, null, ex);
    } catch (MessagingException ex) {
        Logger.getLogger(check_user.class.getName()).log(Level.SEVERE, null, ex);
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

}
