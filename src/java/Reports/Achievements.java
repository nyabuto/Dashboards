/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import Db.dbConn;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author GNyabuto
 */
public class Achievements extends HttpServlet {
HttpSession session;
String startyearmonth,endyearmonth,curryearmonth;
String query_where1="",query_where2="",query_where3="",query="";
String query_where1_cum="",query_where2_cum="",query_where3_cum="",query_cum="";
String table1,table2,table3;
int table1_elems,table2_elems,table3_elems;
int table1_elems_cum,table2_elems_cum,table3_elems_cum;
String targetsquery="",indicator;
int targetscounter=0,year;
String pathtodelete=null;
            int j=1;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, InvalidFormatException {
        response.setContentType("text/html;charset=UTF-8");
        try {
           dbConn conn = new dbConn();
           
//            startyearmonth = request.getParameter("start_date");
//            endyearmonth = request.getParameter("end_date");
            

         XSSFWorkbook wb;    
         String allpath = getServletContext().getRealPath("/achievements_1.xlsm");
         String mydrive = allpath.substring(0, 1);
         
          Date da= new Date();
            String dat2 = da.toString().replace(" ", "_");
             dat2 = dat2.toString().replace(":", "_");

          String np=mydrive+":\\HSDSA\\Dashboards\\Achievements_"+dat2+".xlsm";
            System.out.println("path:: "+np);
              String sr = getServletContext().getRealPath("/achievements_1.xlsm");
              
                  File f = new File(np);
    if(!f.exists()&& !f.isDirectory() ) { /* do something */
    copytemplates ct= new copytemplates();
    ct.transfermacros(sr,np);
 //rem np is the destination file name  
   
    System.out.println("Copying macro template first time ..");

    }
    else 
      //copy the file alone  
    {
    copytemplates ct= new copytemplates();
    //copy the agebased file only
    ct.copymacros(sr,np);

    }

    String filepth=np;      

    File allpathfile= new File(filepth);
     
   OPCPackage pkg = OPCPackage.open(allpathfile);

    pathtodelete=filepth;
    wb = new XSSFWorkbook(pkg);

    
    Sheet shet= wb.getSheet("raw data");
    Sheet shetachievements= wb.createSheet("Quarterly Achievements");
    
        CellStyle stborder = wb.createCellStyle();
        stborder.setBorderTop(CellStyle.BORDER_THIN);
        stborder.setBorderBottom(CellStyle.BORDER_THIN);
        stborder.setBorderLeft(CellStyle.BORDER_THIN);
        stborder.setBorderRight(CellStyle.BORDER_THIN);
        stborder.setAlignment(CellStyle.ALIGN_CENTER);
        
        CellStyle borderh1 = wb.createCellStyle();
        borderh1.setBorderTop(CellStyle.BORDER_THIN);
        borderh1.setBorderBottom(CellStyle.BORDER_THIN);
        borderh1.setBorderLeft(CellStyle.BORDER_THIN);
        borderh1.setBorderRight(CellStyle.BORDER_THIN);
        borderh1.setAlignment(CellStyle.ALIGN_CENTER);
        borderh1.setFillForegroundColor(HSSFColor.GOLD.index);
        borderh1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        CellStyle borderh2 = wb.createCellStyle();
        borderh2.setBorderTop(CellStyle.BORDER_THIN);
        borderh2.setBorderBottom(CellStyle.BORDER_THIN);
        borderh2.setBorderLeft(CellStyle.BORDER_THIN);
        borderh2.setBorderRight(CellStyle.BORDER_THIN);
        borderh2.setAlignment(CellStyle.ALIGN_CENTER);
        borderh2.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        borderh2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    
        XSSFFont fontx = wb.createFont();
        fontx.setColor(HSSFColor.BLACK.index);
        fontx.setBold(true);
        fontx.setFamily(FontFamily.MODERN);
        fontx.setFontName("Cambria");
        
        stborder.setFont(fontx);
        stborder.setWrapText(true);
        
        borderh1.setFont(fontx);
        borderh1.setWrapText(true);
    
        borderh2.setFont(fontx);
        borderh2.setWrapText(true);
    
    
        
        
    CellStyle perStyle=wb.createCellStyle();
    perStyle.setBorderTop(CellStyle.BORDER_THIN);
    perStyle.setBorderBottom(CellStyle.BORDER_THIN);
    perStyle.setBorderLeft(CellStyle.BORDER_THIN);
    perStyle.setBorderRight(CellStyle.BORDER_THIN);
    
    
    perStyle.setFont(fontx);
    perStyle.setWrapText(true);
        
     DataFormat df = wb.createDataFormat();
     
    perStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
    
    
    
       for (int i=0;i<8;i++){
            shetachievements.setColumnWidth(i, 5000);
       }
    
    
            startyearmonth = "201710";
            endyearmonth = "201803";
            
            
            curryearmonth = endyearmonth;
            year = getYear(endyearmonth);
            
            query_where1=query_where2=query_where3="";query="";
            query_where1_cum=query_where2_cum=query_where3_cum="";query_cum="";
            table1_elems=table2_elems=table3_elems=0;
            table1_elems_cum=table2_elems_cum=table3_elems_cum=0;
            targetsquery=indicator="";
            
            query="SELECT  CONCAT(CONCAT(IFNULL(level3,''),IFNULL(level4,'')),MFLCode,yearmonth) AS uniqueID,county,burdencategory,constituency,subcounty,ward,facility,mflcode,supporttype,ifnull(level1,'') AS 'Indicator Category',ifnull(level2,'') AS 'Main Indicator',CONCAT(IFNULL(level3,''),IFNULL(level4,'')) AS 'Indicator',"
                + "IFNULL(unknown_f,0) AS 'Unknown F', IFNULL(unknown_m,0) AS 'Unknown M', IFNULL(d60,0) AS '< 60 Days', IFNULL(mn_0_2,0) AS '0-2 Months', IFNULL(mn_2_12,0) AS '2-12 Months', IFNULL(mn_2_4y,0) AS '2Months - 4Years', IFNULL(f_1,0) AS '<1Yr F', "
                + "IFNULL(m_1,0) AS '<1Yr M', IFNULL(t_1,0) AS '<1Yr Total', IFNULL(f_4,0) AS '1-4 Yrs F', IFNULL(m_4,0) AS '1-4 Yrs M', IFNULL(f_5_9,0) AS '5-9 Yrs F' , IFNULL(m_5_9,0) AS '5-9 Yrs M', IFNULL(f_1_9,0) AS '1-9 Yrs F' , IFNULL(m_1_9,0) AS '1-9 Yrs M', "
                + "IFNULL(t_1_9,0) AS '1-9 Yrs Total', IFNULL(f_14,0) AS '10-14 Yrs F', IFNULL(m_14,0) AS '10-14 Yrs M', IFNULL(f_19,0) AS '15-19 Yrs F', IFNULL(m_19,0) AS '15-19 Yrs M', IFNULL(f_24,0) AS '20-24 Yrs F', IFNULL(m_24,0) AS '20-24 Yrs M', IFNULL(f_29,0) AS '25-29 Yrs F', "
                + "IFNULL(m_29,0) AS '25-29 Yrs M', IFNULL(f_34,0) AS '30-34 Yrs F', IFNULL(m_34,0) AS '30-34 Yrs M', IFNULL(f_39,0) AS '35-39 Yrs F', IFNULL(m_39,0) AS '35-39 Yrs M', IFNULL(f_49,0) AS '40-49 Yrs F', IFNULL(m_49,0) AS '40-49 Yrs', IFNULL(f_25_49,0) AS '25-49 Yrs F', "
                + "IFNULL(m_25_49,0) AS '25-49 Yrs M', IFNULL(f_50,0) '50+ Yrs F' , IFNULL(m_50,0) AS '50+ Yrs M', IFNULL(total,0) AS 'Total Achieved', IFNULL(total_f,0) AS 'Total F', IFNULL(total_m,0) AS 'Total M', IFNULL(paeds_f,0) AS 'Paeds F', IFNULL(paeds_m,0) AS 'Paeds M', "
                + "IFNULL(paeds,0) AS 'Total Paeds', IFNULL(adult_f,0) AS 'Adult F', IFNULL(adult_m,0) AS 'Adult M', IFNULL(adult,0) AS 'Total Adult', IFNULL(f_15_24,0) AS '15-24 Yrs F', IFNULL(m_15_24,0) AS '15-24 Yrs M', IFNULL(t_15_24,0) AS '15-24 Yrs Total', "
                + "year,semiannual,quarter,month,yearmonth,ownedby,facilitytype AS 'Facility Type',art_hv AS 'ART High Volume',htc_hv AS 'HTC High Volume',pmtct_hv AS 'PMTCT High Volume',activity_hv AS 'High Volume',latitude,longitude,maleclinic AS 'Male Clinic',"
                    + "adoleclinic AS 'Adolscent Clinic',viremiaclinic AS 'Viremia Clinic',emrsite AS 'EMR Site',linkdesk AS 'Link Desk',ordernumber,0 AS 'Annual Target' FROM (";
           
            String build_query = "SELECT table_name,level3,level4,is_cumulative,facility_target_column from achievement_mapping";
            conn.rs = conn.st.executeQuery(build_query);
            while(conn.rs.next()){
//                curryearmonth = endyearmonth;
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
               //build targets column
            if(conn.rs.getString(5)!=null && !conn.rs.getString(5).equals("")){
                if(conn.rs.getString(3)!=null){
                  indicator = conn.rs.getString(2)+""+conn.rs.getString(3);  
                }
                else{
                indicator = conn.rs.getString(2);    
                }
               j=1;  
//            if(j<2){
//              while(j>0){ 
//        String[] arraydata = curryearmonth.split("");
//        int yr = Integer.parseInt(arraydata[0]+""+arraydata[1]+""+arraydata[2]+""+arraydata[3]);
//        int mn = Integer.parseInt(arraydata[4]+""+arraydata[5]); 
//                  
//       System.out.println(j+"--current ym : "+curryearmonth+" start yearmn : "+startyearmonth+" end yearmn : "+endyearmonth);        
        targetsquery+=" SELECT CONCAT('"+indicator+"',MFLCode,'"+curryearmonth+"') AS uniqueID, County,SubCounty,Ward,Facility,MFLCode AS mflcode ,'"+indicator+"' AS Indicator,"+conn.rs.getString(5)+" As target FROM facilitytarget WHERE year='"+year+"' "+
                " UNION ALL ";
//        if(curryearmonth.equals(endyearmonth)){
//            break;
//        }
//        else{
//        if(mn==12){
//            yr++;
//            mn=1;
//        }
//        else{
//         mn++;   
//        }
//        if(mn<10){curryearmonth=yr+"0"+mn;}
//        else{curryearmonth=yr+""+mn;}
//        }
//        j++;
//        }
//        }
          targetscounter++;
             }
            }
           
            if(targetscounter>0){
             targetsquery = removeLastChars(targetsquery,11);   
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
         
         String final_query_data = "SELECT * FROM ("+query+" UNION ALL "+query_cum+") AS allinfo ";
         String final_query_targets = "SELECT uniqueID,'' AS county,'' AS burdencategory,'' AS constituency,'' AS subcounty,'' AS ward,'' AS facility,mflcode,'' AS supporttype,'' AS 'Indicator Category','' AS 'Main Indicator',Indicator,"
                + "0 AS 'Unknown F', 0 AS 'Unknown M',0 AS '< 60 Days', 0 AS '0-2 Months', 0 AS '2-12 Months', 0 AS '2Months - 4Years', 0 AS '<1Yr F', "
                + "0 AS '<1Yr M', 0 AS '<1Yr Total', 0 AS '1-4 Yrs F', 0 AS '1-4 Yrs M', 0 AS '5-9 Yrs F' , 0 AS '5-9 Yrs M', 0 AS '1-9 Yrs F' , 0 AS '1-9 Yrs M', "
                + "0 AS '1-9 Yrs Total', 0 AS '10-14 Yrs F', 0 AS '10-14 Yrs M',0 AS '15-19 Yrs F', 0 AS '15-19 Yrs M', 0 AS '20-24 Yrs F', 0 AS '20-24 Yrs M', 0 AS '25-29 Yrs F', "
                + "0 AS '25-29 Yrs M', 0 AS '30-34 Yrs F', 0 AS '30-34 Yrs M', 0 AS '35-39 Yrs F', 0 AS '35-39 Yrs M', 0 AS '40-49 Yrs F', 0 AS '40-49 Yrs', 0 AS '25-49 Yrs F', "
                + "0 AS '25-49 Yrs M', 0 AS '50+ Yrs F' ,0 AS '50+ Yrs M', 0 AS 'Total Achieved', 0 AS 'Total F', 0 AS 'Total M', 0 AS 'Paeds F', 0 AS 'Paeds M', "
                + "0 AS 'Total Paeds', 0 AS 'Adult F', 0 AS 'Adult M', 0 AS 'Total Adult', 0 AS '15-24 Yrs F', 0 AS '15-24 Yrs M', 0 AS '15-24 Yrs Total', "
                + "0 AS year,'' AS semiannual,'' AS quarter,'' AS month,0 AS yearmonth,'' AS ownedby,'' AS 'Facility Type',0 AS 'ART High Volume',0 AS 'HTC High Volume',0 AS 'PMTCT High Volume',0 AS 'High Volume',0 AS latitude,0 AS longitude,0 AS 'Male Clinic',"
                    + "0 AS 'Adolscent Clinic',0 AS 'Viremia Clinic',0 AS 'EMR Site',0 AS 'Link Desk',0 AS ordernumber,target AS 'Annual Target' FROM ("+targetsquery+") AS alltarget ";
         
         String final_query="";
         if(targetscounter>0){
        final_query = "SELECT uniqueID,group_concat(county separator '') AS county,group_concat(burdencategory separator '') AS burdencategory,group_concat(constituency separator '') AS constituency,group_concat(subcounty separator '') AS subcounty,group_concat(ward separator '') AS  ward,group_concat(facility separator '') AS facility,"
                + "mflcode,group_concat(supporttype separator '') AS supporttype,group_concat(`Indicator Category` separator '') AS 'Indicator Category',group_concat(`Main Indicator` separator '') AS 'Main Indicator',Indicator,"
                + "SUM(`Unknown F`) AS 'Unknown F', SUM(`Unknown M`) AS 'Unknown M',SUM(`< 60 Days`) AS '< 60 Days', SUM(`0-2 Months`) AS '0-2 Months',SUM(`2-12 Months`) AS '2-12 Months', SUM(`2Months - 4Years`) AS '2Months - 4Years', SUM(`<1Yr F`) AS '<1Yr F', "
                + "SUM(`<1Yr M`) AS '<1Yr M', SUM(`<1Yr Total`) AS '<1Yr Total', SUM(`1-4 Yrs F`) AS '1-4 Yrs F', SUM(`1-4 Yrs M`) AS '1-4 Yrs M', SUM(`5-9 Yrs F`) AS '5-9 Yrs F' , SUM(`5-9 Yrs M`) AS '5-9 Yrs M', SUM(`1-9 Yrs F`) AS '1-9 Yrs F' , SUM(`1-9 Yrs M`) AS '1-9 Yrs M', "
                + "SUM(`1-9 Yrs Total`) AS '1-9 Yrs Total', SUM(`10-14 Yrs F`) AS '10-14 Yrs F', SUM(`10-14 Yrs M`) AS '10-14 Yrs M',SUM(`15-19 Yrs F`) AS '15-19 Yrs F', SUM(`15-19 Yrs M`) AS '15-19 Yrs M', SUM(`20-24 Yrs F`) AS '20-24 Yrs F', SUM(`20-24 Yrs M`) AS '20-24 Yrs M', SUM(`25-29 Yrs F`) AS '25-29 Yrs F', "
                + "SUM(`25-29 Yrs M`) AS '25-29 Yrs M', SUM(`30-34 Yrs F`) AS '30-34 Yrs F', SUM(`30-34 Yrs M`) AS '30-34 Yrs M', SUM(`35-39 Yrs F`) AS '35-39 Yrs F', SUM(`35-39 Yrs M`) AS '35-39 Yrs M', SUM(`40-49 Yrs F`) AS '40-49 Yrs F', SUM(`40-49 Yrs`) AS '40-49 Yrs', SUM(`25-49 Yrs F`) AS '25-49 Yrs F', "
                + "SUM(`25-49 Yrs M`) AS '25-49 Yrs M', SUM(`50+ Yrs F`) AS '50+ Yrs F' ,SUM(`50+ Yrs M`) AS '50+ Yrs M', SUM(`Total Achieved`) AS 'Total Achieved', SUM(`Total F`) AS 'Total F', SUM(`Total M`) AS 'Total M', SUM(`Paeds F`) AS 'Paeds F', SUM(`Paeds M`) AS 'Paeds M', "
                + "SUM(`Total Paeds`) AS 'Total Paeds', SUM(`Adult F`) AS 'Adult F', SUM(`Adult M`) AS 'Adult M', SUM(`Total Adult`) AS 'Total Adult',SUM(`15-24 Yrs F`) AS '15-24 Yrs F', SUM(`15-24 Yrs M`) AS '15-24 Yrs M', SUM(`15-24 Yrs Total`) AS '15-24 Yrs Total', "
                + "SUM(year) AS year,group_concat(semiannual separator '') AS semiannual,group_concat(quarter separator '') AS quarter,group_concat(month separator '') AS month,yearmonth,group_concat(ownedby separator '') AS Owner,group_concat(`Facility Type` separator '') AS 'Facility Type',"
                + "`ART High Volume`,`HTC High Volume`,`PMTCT High Volume`,`High Volume`,sum(latitude) AS latitude,SUM(longitude) AS longitude,SUM(`Male Clinic`) AS 'Male Clinic',"
                + "SUM(`Adolscent Clinic`) AS 'Adolscent Clinic',SUM(`Viremia Clinic`) AS 'Viremia Clinic',SUM(`EMR Site`) AS 'EMR Site',SUM(`Link Desk`) AS 'Link Desk',SUM(`ordernumber`) AS ordernumber,SUM(IFNULL(`Annual Target`,0))  AS 'Annual Target'"
                + " FROM ("+final_query_data+" UNION "+final_query_targets+") AS allinfo GROUP BY uniqueID  HAVING county!=''";
         }
         else{
           final_query =   final_query_data;
         }
//        
//         conn.rs = conn.st.executeQuery(final_query);
//          ResultSetMetaData metaData = conn.rs.getMetaData();
//       int col_count = metaData.getColumnCount(); //number of column
//       
//         int row_num=1;
//        while(conn.rs.next()){
//          Row row=shet.createRow(row_num);
//          System.out.println("current row is : "+row_num);
//          for(int i=1;i<col_count;i++){
//              String value=conn.rs.getString(i+1);
//               Cell cell= row.createCell(i-1);
//               if(isNumeric(value)){
//               cell.setCellValue(Double.parseDouble(value));
//               }
//               else{
//               cell.setCellValue(value);    
//               }
//          }
//          
//          row_num++;
//         }
         // county quarterly achievements
         int i=0,prev_sum_county=0,prev_targets=0,go_up=0;
         int new_indic,new_county,sum_scaleup,sum_sustainance,achieved,target;
         Row row_ach = null;
         String county,indic,quarter,burden_cat;
         String prev_county,prev_indic;
         sum_scaleup=sum_sustainance=0;
         
         int datarows=-1,addedheader=0;
         prev_county=prev_indic="";
         String[] headers = new String[8] ;
         String q1="",q2="",q3="",q4="";
        String  county_quarterly_query = "SELECT county,burdencategory,supporttype,`Indicator Category`,`Main Indicator`,Indicator,"
                + "SUM(`Unknown F`) AS 'Unknown F', SUM(`Unknown M`) AS 'Unknown M',SUM(`< 60 Days`) AS '< 60 Days', SUM(`0-2 Months`) AS '0-2 Months',SUM(`2-12 Months`) AS '2-12 Months', SUM(`2Months - 4Years`) AS '2Months - 4Years', SUM(`<1Yr F`) AS '<1Yr F', "
                + "SUM(`<1Yr M`) AS '<1Yr M', SUM(`<1Yr Total`) AS '<1Yr Total', SUM(`1-4 Yrs F`) AS '1-4 Yrs F', SUM(`1-4 Yrs M`) AS '1-4 Yrs M', SUM(`5-9 Yrs F`) AS '5-9 Yrs F' , SUM(`5-9 Yrs M`) AS '5-9 Yrs M', SUM(`1-9 Yrs F`) AS '1-9 Yrs F' , SUM(`1-9 Yrs M`) AS '1-9 Yrs M', "
                + "SUM(`1-9 Yrs Total`) AS '1-9 Yrs Total', SUM(`10-14 Yrs F`) AS '10-14 Yrs F', SUM(`10-14 Yrs M`) AS '10-14 Yrs M',SUM(`15-19 Yrs F`) AS '15-19 Yrs F', SUM(`15-19 Yrs M`) AS '15-19 Yrs M', SUM(`20-24 Yrs F`) AS '20-24 Yrs F', SUM(`20-24 Yrs M`) AS '20-24 Yrs M', SUM(`25-29 Yrs F`) AS '25-29 Yrs F', "
                + "SUM(`25-29 Yrs M`) AS '25-29 Yrs M', SUM(`30-34 Yrs F`) AS '30-34 Yrs F', SUM(`30-34 Yrs M`) AS '30-34 Yrs M', SUM(`35-39 Yrs F`) AS '35-39 Yrs F', SUM(`35-39 Yrs M`) AS '35-39 Yrs M', SUM(`40-49 Yrs F`) AS '40-49 Yrs F', SUM(`40-49 Yrs`) AS '40-49 Yrs', SUM(`25-49 Yrs F`) AS '25-49 Yrs F', "
                + "SUM(`25-49 Yrs M`) AS '25-49 Yrs M', SUM(`50+ Yrs F`) AS '50+ Yrs F' ,SUM(`50+ Yrs M`) AS '50+ Yrs M', SUM(`Total Achieved`) AS 'Total Achieved', SUM(`Total F`) AS 'Total F', SUM(`Total M`) AS 'Total M', SUM(`Paeds F`) AS 'Paeds F', SUM(`Paeds M`) AS 'Paeds M', "
                + "SUM(`Total Paeds`) AS 'Total Paeds', SUM(`Adult F`) AS 'Adult F', SUM(`Adult M`) AS 'Adult M', SUM(`Total Adult`) AS 'Total Adult',SUM(`15-24 Yrs F`) AS '15-24 Yrs F', SUM(`15-24 Yrs M`) AS '15-24 Yrs M', SUM(`15-24 Yrs Total`) AS '15-24 Yrs Total', "
                + "year,quarter,`ART High Volume`,`PMTCT High Volume`,`High Volume`,"
                + "ordernumber,SUM(IFNULL(`Annual Target`,0))  AS 'Annual Target'"
                + " FROM ("+final_query+") AS data GROUP BY Indicator,county,quarter ORDER BY ordernumber,county,quarter";
         
//            System.out.println("quarter : "+county_quarterly_query);

         conn.rs = conn.st.executeQuery(county_quarterly_query);
         while(conn.rs.next()){
             go_up=0;
             
              Cell cellcounty=null,cellq1=null,cellq2=null,cellq3=null,cellq4=null;
             
             indic = conn.rs.getString("Indicator");
             county = conn.rs.getString("county");
             burden_cat = conn.rs.getString("burdencategory");
             quarter = conn.rs.getString("quarter");
             achieved = conn.rs.getInt("Total Achieved");
             target = conn.rs.getInt("Annual Target");
             String yr=conn.rs.getString("year");
            if(!prev_indic.equals(indic)){
               if(i==0){
                i++;
                go_up++;
               }
               else{
                i+=2;
                 go_up+=3;
               }
             
              
              row_ach = shetachievements.createRow(i);
              for (int m=0;m<8;m++){
               Cell cell = row_ach.createCell(m); 
               cell.setCellValue(indic);
               cell.setCellStyle(borderh1);
              
              }
              
             shetachievements.addMergedRegion(new CellRangeAddress(i,i,0,7));
              i++;  
               
            }
            if(!prev_county.equals(county)){
                datarows++;
                i++;
                go_up++;
              row_ach = shetachievements.createRow(i);
              
              
              cellcounty = row_ach.createCell(0);
              cellq1 = row_ach.createCell(2);
              cellq2 = row_ach.createCell(3);
              cellq3 = row_ach.createCell(4);
              cellq4 = row_ach.createCell(5);
            }
            else{
             row_ach = shetachievements.getRow(i); 
             cellcounty = row_ach.getCell(0);
              cellq1 = row_ach.getCell(2);
              cellq2 = row_ach.getCell(3);
              cellq3 = row_ach.getCell(4);
              cellq4 = row_ach.getCell(5);
            }
            cellcounty.setCellStyle(stborder);
            cellq1.setCellStyle(stborder);
            cellq2.setCellStyle(stborder);
            cellq3.setCellStyle(stborder);
            cellq4.setCellStyle(stborder);
              
              cellcounty.setCellValue(county);
            if(quarter.startsWith("1.")){
              cellq1.setCellValue(achieved);
              q1=quarter+"'"+yr;
            }
            else if(quarter.startsWith("2.")){
              cellq2.setCellValue(achieved);  
            q2=quarter+"'"+yr;
            }
            
            else if(quarter.startsWith("3.")){
             cellq3.setCellValue(achieved);
             q3=quarter+"'"+yr;
            }
            else{
             cellq4.setCellValue(achieved);   
            q4=quarter+"'"+yr;
            }
           
            if(!prev_county.equals(county)){
             
                 row_ach = shetachievements.getRow(i-go_up);   
                  System.out.println("sheet pos : "+(i-go_up)+" q2 : "+q2);
             XSSFCell celltarg = (XSSFCell) row_ach.createCell(1);
             XSSFCell  celltotal =(XSSFCell) row_ach.createCell(6);
             XSSFCell  cellperc =(XSSFCell) row_ach.createCell(7);
             
              
              celltotal.setCellValue(prev_sum_county);
              celltarg.setCellValue(prev_targets);
              celltotal.setCellStyle(stborder);
              celltarg.setCellStyle(stborder);
            
              cellperc.setCellValue(getperachieved(prev_sum_county, prev_targets));
              cellperc.setCellStyle(perStyle);
            
         }
            
//             System.out.println("prev indic : "+prev_indic+" curr indic : "+indic+" prev county : "+prev_county+" curr county : "+county);
            if(!prev_indic.equals(indic)&& !prev_county.equals("")){
                System.out.println(i+"----Country,Targets,"+q1+","+q2+","+q3+","+q4+", header at : "+(i-(datarows+4)));
            headers = ("Country,Targets,"+q1+","+q2+","+q3+","+q4+",Total Achieved,%Achieved").split(",");
             shetachievements =  addheaders(shetachievements,headers,i-(datarows+4),borderh2);
                         datarows=0;
            }
            
            
             if(burden_cat.equals("Scale-up")){
                sum_scaleup=+achieved; 
             }
             else{
                 sum_sustainance=+achieved;
             }
             
//          System.out.println("indicator : "+indic+" county :"+county+" burden_cat : "+burden_cat+" quarter : "+quarter+" total achievement: "+achieved+" Annual targeted : "+target+" PREV T"+prev_sum_county+" year : "+yr);
          if(prev_county.equals(county)){prev_sum_county+=achieved; }
          else{prev_sum_county=achieved; }
          prev_targets= target; 
          prev_county = county;
          prev_indic = indic;
         }
         //FO THE LAST COUNTY
            row_ach = shetachievements.getRow(i);   
//            System.out.println("i : "+i);
             XSSFCell celltarg = (XSSFCell) row_ach.createCell(1);
             XSSFCell  celltotal =(XSSFCell) row_ach.createCell(6);
             XSSFCell  cellperc =(XSSFCell) row_ach.createCell(7);
     
              celltotal.setCellValue(prev_sum_county);
              celltarg.setCellValue(prev_targets);
              celltotal.setCellStyle(stborder);
              celltarg.setCellStyle(stborder);
            
              cellperc.setCellValue(getperachieved(prev_sum_county, prev_targets));
              cellperc.setCellStyle(perStyle);
              
            
                System.out.println(i+"----Country,Targets,"+q1+","+q2+","+q3+","+q4+", header at : "+(i-(datarows+1)));
                headers = ("Country,Targets,"+q1+","+q2+","+q3+","+q4+",Total Achieved,%Achieved").split(",");
                shetachievements =  addheaders(shetachievements,headers,i-(datarows+1),borderh2);

            
         //end of county quarterly achievements
         
         
         
            System.out.println("final targets : "+final_query_targets);
            System.out.println("final data : "+final_query_data);
            System.out.println("final query : "+final_query);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=Achievements_Report_for_"+startyearmonth+"_to_"+endyearmonth+".xlsm");
        OutputStream outStream = response.getOutputStream();
        outStream.write(outArray);
        outStream.flush();
        outStream.close(); 
           pkg.close();

   if(conn.rs!=null){conn.rs.close();}
   if(conn.rs1!=null){conn.rs1.close();}
   if(conn.st1!=null){conn.st1.close();}
   if(conn.st!=null){conn.st.close();}
       
         File file= new File(pathtodelete);
            System.out.println("path: 2"+pathtodelete);
           
        if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation  failed.");
    		}
        } 
        catch (SQLException ex) { 
            Logger.getLogger(Achievements.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(Achievements.class.getName()).log(Level.SEVERE, null, ex);
        }        finally 
        {
           
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
    } catch (InvalidFormatException ex) {
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
    } catch (InvalidFormatException ex) {
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

     public int getYear(String yearmonth){
       int year=0;
        String[] arraydata = yearmonth.split("");
        String year_st = arraydata[0]+""+arraydata[1]+""+arraydata[2]+""+arraydata[3];
        String month = arraydata[4]+""+arraydata[5];
        
        if(Integer.parseInt(month)>=10)
        {
         year = Integer.parseInt(year_st)-1;
        }
        else
        {
          year = Integer.parseInt(year_st);   
        }
        
        return year;
     }
     public boolean isNumeric(String s) {  
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
    }
     public double getperachieved(int achieved,int target){
         double perc_achieved=0;
         
         if(target!=0){
         perc_achieved = ((double)achieved/(double)target);
         }
        return perc_achieved;
     }
   
     
     public Sheet addheaders(Sheet sheet,String[] headers,int i,CellStyle stborder){
            Row rowheader = sheet.createRow(i);
            int cellpos=0;
         for(String header:headers){
                XSSFCell  cellperc =(XSSFCell) rowheader.createCell(cellpos);
                cellperc.setCellValue(header);
                cellperc.setCellStyle(stborder);
                cellpos++;
            }
      return sheet;   
     }
     
     
     
}
