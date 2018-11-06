/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dashboards;

import Db.dbConn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author GNyabuto
 */
public class load_users extends HttpServlet {
HttpSession session;
String output="";
int pos;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
          session = request.getSession();
            dbConn conn = new dbConn();
            
            output="<table class=\"table\"><thead><tr><th>No.</th><th>Full Name</th><th>Email</th><th>Phone Number</th><th>Gender</th><th>Action</th></thead><tbody>";
            pos=0;
            
            String get_users = "SELECT id,fullname,email,phone,gender FROM user WHERE status=0";
            conn.rs = conn.st.executeQuery(get_users);
            while(conn.rs.next()){
                pos++;
            output+="<tr>"
                    + "<td>"+pos+"</td>"
                    + "<td>"+conn.rs.getString(2)+"</td>"
                    + "<td>"+conn.rs.getString(3)+"</td>"
                    + "<td>"+conn.rs.getString(4)+"</td>"
                    + "<td>"+conn.rs.getString(5)+"</td>"
                    + "<td><table><tr><td><button  onclick=\"return approve("+conn.rs.getString(1)+");\" class=\"btn btn-success\">Approve</button></td> <td><button onclick=\"return decline("+conn.rs.getString(1)+");\" class=\"btn btn-danger\">Decline</button></td></tr></table></td>"
                    + "</tr>";    
            }
            
            if(pos==0){
             output+="<tr>"
                    + "<td colspan=\"6\" style=\"font-size: 30px; text-align:center; font-weight:bolder; color:red;\">No User Request is Pending</td>"
                    + "</tr>";    
            }
            
            output+="</tbody></table>";
            
            out.println(output);
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
        Logger.getLogger(load_users.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(load_users.class.getName()).log(Level.SEVERE, null, ex);
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
