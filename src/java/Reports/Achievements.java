/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

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
public class Achievements extends HttpServlet {
HttpSession session;
String startyearmonth,endyearmonth;
String query_where1="",query_where2="",query_where3="",query="";
String query_where1_cum="",query_where2_cum="",query_where3_cum="",query_cum="";
String table1,table2,table3;
int table1_elems,table2_elems,table3_elems;
int table1_elems_cum,table2_elems_cum,table3_elems_cum;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           dbConn conn = new dbConn();
           
//            startyearmonth = request.getParameter("start_date");
//            endyearmonth = request.getParameter("end_date");
            
            startyearmonth = "201710";
            endyearmonth = "201803";
            
            query_where1=query_where2=query_where3="";query="";
            table1_elems=table2_elems=table3_elems=0;
            table1_elems_cum=table2_elems_cum=table3_elems_cum=0;
            query="SELECT county,burdencategory,constituency,subcounty,ward,facility,mflcode,supporttype,ifnull(level1,'') AS 'Indicator Category',ifnull(level2,'') AS 'Main Indicator',CONCAT(IFNULL(level3,''),IFNULL(level4,'')) AS 'Indicator',"
                + "IFNULL(unknown_f,0) AS 'Unknown F', IFNULL(unknown_m,0) AS 'Unknown M', IFNULL(d60,0) AS '< 60 Days', IFNULL(mn_0_2,0) AS '0-2 Months', IFNULL(mn_2_12,0) AS '2-12 Months', IFNULL(mn_2_4y,0) AS '2Months - 4Years', IFNULL(f_1,0) AS '<1Yr F', "
                + "IFNULL(m_1,0) AS '<1Yr M', IFNULL(t_1,0) AS '<1Yr Total', IFNULL(f_4,0) AS '1-4 Yrs F', IFNULL(m_4,0) AS '1-4 Yrs M', IFNULL(f_5_9,0) AS '5-9 Yrs F' , IFNULL(m_5_9,0) AS '5-9 Yrs M', IFNULL(f_1_9,0) AS '1-9 Yrs F' , IFNULL(m_1_9,0) AS '1-9 Yrs M', "
                + "IFNULL(t_1_9,0) AS '1-9 Yrs Total', IFNULL(f_14,0) AS '10-14 Yrs F', IFNULL(m_14,0) AS '10-14 Yrs M', IFNULL(f_19,0) AS '15-19 Yrs F', IFNULL(m_19,0) AS '15-19 Yrs M', IFNULL(f_24,0) AS '20-24 Yrs F', IFNULL(m_24,0) AS '20-24 Yrs M', IFNULL(f_29,0) AS '25-29 Yrs F', "
                + "IFNULL(m_29,0) AS '25-29 Yrs M', IFNULL(f_34,0) AS '30-34 Yrs F', IFNULL(m_34,0) AS '30-34 Yrs M', IFNULL(f_39,0) AS '35-39 Yrs F', IFNULL(m_39,0) AS '35-39 Yrs M', IFNULL(f_49,0) AS '40-49 Yrs F', IFNULL(m_49,0) AS '40-49 Yrs', IFNULL(f_25_49,0) AS '25-49 Yrs F', "
                + "IFNULL(m_25_49,0) AS '25-49 Yrs M ', IFNULL(f_50,0) '50+ Yrs F' , IFNULL(m_50,0) AS '50+ Yrs M', IFNULL(total,0) AS 'Total', IFNULL(total_f,0) AS 'Total F', IFNULL(total_m,0) AS 'Total M', IFNULL(paeds_f,0) AS 'Paeds F', IFNULL(paeds_m,0) AS 'Paeds M', "
                + "IFNULL(paeds,0) AS 'Total Paeds', IFNULL(adult_f,0) AS 'Adult F', IFNULL(adult_m,0) AS 'Adult M', IFNULL(adult,0) AS 'Total Adult', IFNULL(f_15_24,0) AS '15-24 Yrs F', IFNULL(m_15_24,0) AS '15-24 Yrs M', IFNULL(t_15_24,0) AS '15-24 Yrs Total', "
                + "year,semiannual,quarter,month,yearmonth,ownedby,facilitytype AS 'Facility Type',art_hv AS 'ART High Volume',htc_hv AS 'HTC High Volume',pmtct_hv AS 'PMTCT High Volume',activity_hv AS 'High Volume',latitude,longitude,maleclinic AS 'Male Clinic',"
                    + "adoleclinic AS 'Adolscent Clinic',viremiaclinic AS 'Viremia Clinic',emrsite AS 'EMR Site',linkdesk AS 'Link Desk',ordernumber FROM (";
           
            String build_query = "SELECT table_name,level3,level4,is_cumulative from achievement_mapping";
            conn.rs = conn.st.executeQuery(build_query);
            while(conn.rs.next()){
               switch (conn.rs.getString(1)) {
                   case "table1":
                       if(conn.rs.getString(3)!=null){
                           if(conn.rs.getInt(4)==0){
                           query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table1_elems++;
                           }
                           else{
                         query_where1_cum+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR "; 
                         table1_elems_cum++;
                       }
                       }
                       else{
                        if(conn.rs.getInt(4)==0){
                           query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4 IS NULL) OR ";
                           table1_elems++;
                           }
                           else{
                         query_where1_cum+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4 IS NULL) OR "; 
                         table1_elems_cum++;
                       }
                       } 
                       
                       break;
                   case "table2":
                       if(conn.rs.getString(3)!=null){
                           if(conn.rs.getInt(4)==0){
                           query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table2_elems++;
                           }
                           else{
                         query_where2_cum+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR "; 
                         table2_elems_cum++;
                       }
                       }
                       else{
                        if(conn.rs.getInt(4)==0){
                           query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4 IS NULL) OR ";
                           table2_elems++;
                           }
                           else{
                         query_where2_cum+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4 IS NULL) OR "; 
                         table2_elems_cum++;
                       }
                       } 
                       
                       break;
                   case "table3":
                       if(conn.rs.getString(3)!=null){
                           if(conn.rs.getInt(4)==0){
                           query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table3_elems++;
                           }
                           else{
                         query_where3_cum+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR "; 
                         table3_elems_cum++;
                       }
                       }
                       else{
                        if(conn.rs.getInt(4)==0){
                           query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4 IS NULL) OR ";
                           table3_elems++;
                           }
                           else{
                         query_where3_cum+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4 IS NULL) OR "; 
                         table3_elems_cum++;
                       }
                       } 
                       
                       break;
                   default:
                       break;
               } 
            }
           
            
           query_cum=query; 
            
            
          if(table1_elems>0) {
              query_where1 = removeLastChars(query_where1,3);
              query+=" SELECT * FROM table1 WHERE ("+query_where1+") AND yearmonth BETWEEN "+startyearmonth+" AND "+endyearmonth+" "
              + " UNION ALL "; 
          }
         if(table2_elems>0) {
             query_where2 = removeLastChars(query_where2,3);
              query+=" SELECT * FROM table2 WHERE ("+query_where2+") AND yearmonth BETWEEN "+startyearmonth+" AND "+endyearmonth+" "
              + " UNION ALL ";
         }
         if(table3_elems>0) {
             query_where3 = removeLastChars(query_where3,3);
              query+=" SELECT * FROM table3 WHERE ("+query_where3+") AND yearmonth BETWEEN "+startyearmonth+" AND "+endyearmonth+" "
              + " UNION ALL ";
         }
          if(table1_elems_cum>0) {
              query_where1_cum = removeLastChars(query_where1_cum,3);
              query_cum+=" SELECT * FROM table1 WHERE ("+query_where1_cum+") AND yearmonth BETWEEN "+endyearmonth+" AND "+endyearmonth+" "
              + " UNION ALL "; 
          }
         if(table2_elems_cum>0) {
             query_where2_cum = removeLastChars(query_where2_cum,3);
              query_cum+=" SELECT * FROM table2 WHERE ("+query_where2_cum+") AND yearmonth BETWEEN "+endyearmonth+" AND "+endyearmonth+" "
              + " UNION ALL ";
         }
         if(table3_elems_cum>0) {
             query_where3_cum = removeLastChars(query_where3_cum,3);
              query_cum+=" SELECT * FROM table3 WHERE ("+query_where3_cum+") AND yearmonth BETWEEN "+endyearmonth+" AND "+endyearmonth+" "
              + " UNION ALL ";
         }
         query = removeLastChars(query,11);
         query_cum = removeLastChars(query_cum,11);
         
         query+=") AS all_data";
         query_cum+=") AS all_data";
         
         String final_query = "SELECT * FROM ("+query+" UNION ALL "+query_cum+") AS allinfo ORDER BY ordernumber";
         
         
         System.out.println("final query : "+final_query);
            
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
        Logger.getLogger(Achievements.class.getName()).log(Level.SEVERE, null, ex);
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
        Logger.getLogger(Achievements.class.getName()).log(Level.SEVERE, null, ex);
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

     private static String removeLastChars(String str, int num) {
    return str.substring(0, str.length() - num);
}
  
}
