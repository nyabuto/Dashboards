/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package powerbi;


import Db.dbConn;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Emmanuel E
 */
public class alldata1 extends HttpServlet {

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        XSSFWorkbook workbook =  new XSSFWorkbook();
        
        SXSSFWorkbook wb = new SXSSFWorkbook(workbook, 100);


Sheet shet = wb.createSheet("Raw data");



        

   // response.setContentType("text/csv");
    

        try {
            
            dbConn conn= new dbConn();
            
           
            
               //conn.st.executeUpdate("SET GLOBAL max_allowed_packet = 209715200"); 
               
           Calendar cal = Calendar.getInstance(); 
           int year=cal.get(Calendar.YEAR);
           int month=cal.get(Calendar.MONTH)+1;
           //if month is october, get targets for the next year
           if(month>=10){year=year+1;}
            int ordinalDay = cal.get(Calendar.DAY_OF_YEAR);
    int weekDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // Sunday = 0
    int numberOfWeeks = (ordinalDay - weekDay + 10) / 7;
            JSONArray jarr=new JSONArray();
            //String getfacils="SELECT id,facility,year,hiv_pos_target_child,hiv_pos_target_adult,hiv_pos_target_total,new_art_target_child,new_art_target_adult,new_art_target_total,viral_load_target_child,viral_load_target_adult,viral_load_target_total,ipt_target_child,ipt_target_adult,ipt_target_total,testing_target_child,testing_target_adult,testing_target_total,pmtct_hiv_pos_target,eid_target,viral_load_mothers_target,timestamp FROM  targets where year ='"+year+"'order by id";
           
            //currently i am not validating targets that are being fetched 
            
            String getfacils="SELECT  * FROM `alldata` limit 100 ";
         
            String header="<table border='1'><tr>";
            String data="<tr>";
             
            int count1=0;
            
            conn.rs=conn.st.executeQuery(getfacils);
            
            
             ResultSetMetaData metaData = conn.rs.getMetaData();
        int columnCount = metaData.getColumnCount();

         metaData = conn.rs.getMetaData();
         columnCount = metaData.getColumnCount();
        int count = count1;
        ArrayList mycolumns = new ArrayList();
            
            
           
   
        OutputStream outputStream = response.getOutputStream();
        
      String outputResult = "";
        
        
            
              while (conn.rs.next()) {

            if (count == (count1)) {
//header rows
                Row rw = shet.createRow(count);
rw.setHeightInPoints(26);
                for (int i = 1; i <= columnCount; i++) {

                    mycolumns.add(metaData.getColumnLabel(i));
                    Cell cell0 = rw.createCell(i - 1);
                    cell0.setCellValue(metaData.getColumnLabel(i));
                   

                    //create row header
                }//end of for loop
                count++;
            }//end of if
            //data rows     
            Row rw = shet.createRow(count);

            for (int a = 0; a < columnCount; a++) {
                //System.out.print(mycolumns.get(a) + ":" + conn.rs.getString("" + mycolumns.get(a)));

                Cell cell0 = rw.createCell(a);
                 if(!isNumeric(conn.rs.getString("" + mycolumns.get(a)))){
               // if(1==1){
                
                    
                     cell0.setCellValue(conn.rs.getString("" + mycolumns.get(a)));
                   }
                else 
                {
                    //cell0.setCellValue(conn.rs.getString("" + mycolumns.get(a)));
                    cell0.setCellValue(conn.rs.getInt(mycolumns.get(a).toString()));
                }
            
               

            }

            // System.out.println("");
            count++;
        }
            //data+="</table>";
            
              if(conn.rs!=null){conn.rs.close();}
              if(conn.st!=null){conn.st.close();}
              if(conn.conn!=null){conn.conn.close();}
            
            
              
              
          //System.out.println(""+data); 
            
//            try (PrintWriter out = response.getWriter()) {
//                
//                
//               // out.println(header+data+"</table>");
//               // out.println(jarr);
//            }
            
        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
wb.write(outByteStream);
byte[] outArray = outByteStream.toByteArray();
response.setContentType("application/ms-excel");
response.setContentLength(outArray.length);
response.setHeader("Expires:", "0"); // eliminates browser caching
response.setHeader("Content-Disposition", "attachment; filename=" + "PBIREADER_.xlsx");
response.setHeader("Set-Cookie","fileDownload=true; path=/");
OutputStream outStream = response.getOutputStream();
outStream.write(outArray);
outStream.flush();

            
            
            
        }   catch (SQLException ex) {
            Logger.getLogger(alldata.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


   public static boolean isNumeric(String strNum) {
    try {
        double d = Double.parseDouble(strNum);
    } catch (NumberFormatException | NullPointerException nfe) {
        return false;
    }
    return true;
}

}
