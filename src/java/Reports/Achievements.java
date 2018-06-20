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
import java.util.ArrayList;
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
import org.apache.poi.ss.usermodel.FontFamily;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

/**
 *
 * @author GNyabuto
 */
public class Achievements extends HttpServlet {
HttpSession session;
String startyearmonth,endyearmonth,curryearmonth,year;
String query_where1="",query_where2="",query_where3="",query="";
String table1,table2,table3;
int table1_elems,table2_elems,table3_elems;
String targetsquery="",indicator;
int targetscounter=0;
String pathtodelete=null;
String period,semi,quarter,month;
    ArrayList cum_indicators = new ArrayList();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, InvalidFormatException {
        response.setContentType("text/html;charset=UTF-8");
        try {
           dbConn conn = new dbConn();
           

            year = request.getParameter("year");
            period = request.getParameter("period");

            if(period.equals("1")){
             year = request.getParameter("year"); 
             startyearmonth = (Integer.parseInt(year)-1)+"10";
             endyearmonth = year+"09";
            }
            
            else if(period.equals("2")){
                semi = request.getParameter("semi_annual");
                if(semi.equals("1")){
                 startyearmonth = (Integer.parseInt(year)-1)+"10";
                 endyearmonth = year+"03";    
                }
                else{
                   startyearmonth = year+"04";
                   endyearmonth = year+"09";  
                }
            }
            
            
            else if(period.equals("3")){
                quarter = request.getParameter("quarter");
                if(quarter.equals("1")){
                 startyearmonth = (Integer.parseInt(year)-1)+"10";
                 endyearmonth = (Integer.parseInt(year)-1)+"12";    
                }
                else if(quarter.equals("2")){
                   startyearmonth = year+"01";
                   endyearmonth = year+"03";  
                }
                else if(quarter.equals("3")){
                   startyearmonth = year+"04";
                   endyearmonth = year+"06";  
                }
                else if(quarter.equals("4")){
                   startyearmonth = year+"07";
                   endyearmonth = year+"09";  
                }
            }
            
            else if(period.equals("4")){
                month = request.getParameter("month");
                if(Integer.parseInt(month)>=10){
                  endyearmonth = startyearmonth = (Integer.parseInt(year)-1)+""+month;      
                }
                else{
                 endyearmonth = startyearmonth = year+"0"+month;    
                }
            }
            
            System.out.println("start yearmonth : "+startyearmonth+" end year month : "+endyearmonth);

        cum_indicators.clear();
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
        
        CellStyle bordermainheader = wb.createCellStyle();
        bordermainheader.setBorderTop(CellStyle.BORDER_THIN);
        bordermainheader.setBorderBottom(CellStyle.BORDER_THIN);
        bordermainheader.setBorderLeft(CellStyle.BORDER_THIN);
        bordermainheader.setBorderRight(CellStyle.BORDER_THIN);
        bordermainheader.setAlignment(CellStyle.ALIGN_CENTER);
        bordermainheader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        bordermainheader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
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
        
        CellStyle bordert = wb.createCellStyle();
        bordert.setBorderTop(CellStyle.BORDER_THIN);
        bordert.setBorderBottom(CellStyle.BORDER_THIN);
        bordert.setBorderLeft(CellStyle.BORDER_THIN);
        bordert.setBorderRight(CellStyle.BORDER_THIN);
        bordert.setAlignment(CellStyle.ALIGN_CENTER);
        bordert.setFillForegroundColor(HSSFColor.LEMON_CHIFFON.index);
        bordert.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    
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
        
        
        bordert.setFont(fontx);
        bordert.setWrapText(true);
        
        
        bordermainheader.setFont(fontx);
        bordermainheader.setWrapText(true);
    
    
        
        
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
        
            query_where1=query_where2=query_where3="";query="";
            table1_elems=table2_elems=table3_elems=0;
            targetsquery=indicator="";
            
            query="SELECT  CONCAT(CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')),MFLCode,yearmonth) AS uniqueID,county,burdencategory,constituency,subcounty,ward,facility,mflcode,supporttype,ifnull(level1,'') AS 'Indicator Category',ifnull(level2,'') AS 'Main Indicator',CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS 'Indicator',"
                + "IFNULL(unknown_f,0) AS 'Unknown F', IFNULL(unknown_m,0) AS 'Unknown M', IFNULL(d60,0) AS '< 60 Days', IFNULL(mn_0_2,0) AS '0-2 Months', IFNULL(mn_2_12,0) AS '2-12 Months', IFNULL(mn_2_4y,0) AS '2Months - 4Years', IFNULL(f_1,0) AS '<1Yr F', "
                + "IFNULL(m_1,0) AS '<1Yr M', IFNULL(t_1,0) AS '<1Yr Total', IFNULL(f_4,0) AS '1-4 Yrs F', IFNULL(m_4,0) AS '1-4 Yrs M', IFNULL(f_5_9,0) AS '5-9 Yrs F' , IFNULL(m_5_9,0) AS '5-9 Yrs M', IFNULL(f_1_9,0) AS '1-9 Yrs F' , IFNULL(m_1_9,0) AS '1-9 Yrs M', "
                + "IFNULL(t_1_9,0) AS '1-9 Yrs Total', IFNULL(f_14,0) AS '10-14 Yrs F', IFNULL(m_14,0) AS '10-14 Yrs M', IFNULL(f_19,0) AS '15-19 Yrs F', IFNULL(m_19,0) AS '15-19 Yrs M', IFNULL(f_24,0) AS '20-24 Yrs F', IFNULL(m_24,0) AS '20-24 Yrs M', IFNULL(f_29,0) AS '25-29 Yrs F', "
                + "IFNULL(m_29,0) AS '25-29 Yrs M', IFNULL(f_34,0) AS '30-34 Yrs F', IFNULL(m_34,0) AS '30-34 Yrs M', IFNULL(f_39,0) AS '35-39 Yrs F', IFNULL(m_39,0) AS '35-39 Yrs M', IFNULL(f_49,0) AS '40-49 Yrs F', IFNULL(m_49,0) AS '40-49 Yrs', IFNULL(f_25_49,0) AS '25-49 Yrs F', "
                + "IFNULL(m_25_49,0) AS '25-49 Yrs M', IFNULL(f_50,0) '50+ Yrs F' , IFNULL(m_50,0) AS '50+ Yrs M', IFNULL(total,0) AS 'Total Achieved', IFNULL(total_f,0) AS 'Total F', IFNULL(total_m,0) AS 'Total M', IFNULL(paeds_f,0) AS 'Paeds F', IFNULL(paeds_m,0) AS 'Paeds M', "
                + "IFNULL(paeds,0) AS 'Total Paeds', IFNULL(adult_f,0) AS 'Adult F', IFNULL(adult_m,0) AS 'Adult M', IFNULL(adult,0) AS 'Total Adult', IFNULL(f_15_24,0) AS '15-24 Yrs F', IFNULL(m_15_24,0) AS '15-24 Yrs M', IFNULL(t_15_24,0) AS '15-24 Yrs Total', "
                + "year,semiannual,quarter,month,yearmonth,ownedby,facilitytype AS 'Facility Type',art_hv AS 'ART High Volume',htc_hv AS 'HTC High Volume',pmtct_hv AS 'PMTCT High Volume',activity_hv AS 'High Volume',latitude,longitude,maleclinic AS 'Male Clinic',"
                    + "adoleclinic AS 'Adolscent Clinic',viremiaclinic AS 'Viremia Clinic',emrsite AS 'EMR Site',linkdesk AS 'Link Desk',ordernumber,0 AS 'Annual Target' FROM (";
           
            String build_query = "SELECT table_name,level3,level4,is_cumulative,facility_target_column from achievement_mapping WHERE is_active=1";
            conn.rs = conn.st.executeQuery(build_query);
            while(conn.rs.next()){
//                curryearmonth = endyearmonth;
               switch (conn.rs.getString(1)) {
                   case "table1":
                       if(conn.rs.getString(3)!=null){
                           query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table1_elems++;
                        
                       }
                       else{
                           query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           table1_elems++;
                        
                       } 
                       
                       break;
                   case "table2":
                       if(conn.rs.getString(3)!=null){
                           query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table2_elems++;
                         
                       }
                       else{
                           query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           table2_elems++;
                        
                       } 
                       
                       break;
                   case "table3":
                       if(conn.rs.getString(3)!=null){
                           query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table3_elems++;
                          
                       }
                       else{
                           query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           table3_elems++;
                           
                       } 
                       
                       break;
                   default:
                       break;
                        
               }
               String target_column = conn.rs.getString(5);
               String cum_ym="";
               //select cum indicators 
               if(conn.rs.getInt("is_cumulative")==1){
                cum_indicators.add(conn.rs.getString(1)+","+conn.rs.getString(2)+","+conn.rs.getString(3));
                
               cum_ym = cum_where(cum_indicators,conn,startyearmonth,endyearmonth).get(0).toString();
               }
               
               //build targets column
            if(conn.rs.getString(5)!=null && !conn.rs.getString(5).equals("")){
                if(conn.rs.getString(3)!=null){
                  indicator = conn.rs.getString(2)+" "+conn.rs.getString(3);  
                }
                else{
                indicator = conn.rs.getString(2)+" ";    
                } 
                
        curryearmonth = getmaxyearmonth(conn,conn.rs.getString(1),conn.rs.getString(2),conn.rs.getString(3),conn.rs.getInt("is_cumulative"),cum_ym);
        targetsquery+=" SELECT CONCAT('"+indicator+"',MFLCode,'"+curryearmonth+"') AS uniqueID, County,SubCounty,Ward,Facility,MFLCode AS mflcode ,'"+indicator+"' AS Indicator,"+target_column+" As target FROM facilitytarget WHERE year='"+year+"' "+
                " UNION ALL ";

          targetscounter++;
             }
            }
           // start cum yearmonth
           
         String cumulative_quarter_yearmonth = cum_where(cum_indicators,conn,startyearmonth,endyearmonth).get(0).toString();
         String cumulative_quarter_where = cum_where(cum_indicators,conn,startyearmonth,endyearmonth).get(1).toString();
          
           //end yearmonth
            
            
            if(targetscounter>0){
             targetsquery = removeLastChars(targetsquery,11);   
            }
            
            
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
         
         query = removeLastChars(query,11);
         
         query+=") AS all_data";
         
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
                + " FROM ("+query+" UNION "+final_query_targets+") AS allinfo GROUP BY uniqueID HAVING county!=''";
         }
         else{
           final_query =   query;
         }
            System.out.println("final raw data query : "+final_query);
        //output raw data
         conn.rs = conn.st.executeQuery(final_query);
          ResultSetMetaData metaData = conn.rs.getMetaData();
       int col_count = metaData.getColumnCount(); //number of column
       
         int row_num=1;
        while(conn.rs.next()){
          Row row=shet.createRow(row_num);
          for(int i=1;i<col_count;i++){
              String value=conn.rs.getString(i+1);
               Cell cell= row.createCell(i-1);
               if(isNumeric(value)){
               cell.setCellValue(Double.parseDouble(value));
               }
               else{
               cell.setCellValue(value);    
               }
          }
          
          row_num++;
         }
        // end of raw data output
        
        
        
         // county quarterly achievements
         Row row = shetachievements.createRow(0);
         row.setHeightInPoints(35);
         for(int i=0;i<8;i++){
          Cell cell = row.createCell(i);
          cell.setCellValue("Quarterly Achievements");
          cell.setCellStyle(bordermainheader);
         }
           shetachievements.addMergedRegion(new CellRangeAddress(0,0,0,7));
         //end of header
         
         
         int i=0,prev_sum_county=0,prev_targets=0;
         int new_county,sum_scaleup,sum_sustainance,achieved,target;
         Row row_ach = null;
         String county,indic,burden_cat;
         String prev_indic;
         sum_scaleup=sum_sustainance=0;
         
         int cum;
         int scale_q1,scale_q2,scale_q3,scale_q4,scale_total,scale_targ;
         int sust_q1,sust_q2,sust_q3,sust_q4,sust_total,sust_targ;
         prev_indic="";
         String[] headers = new String[8] ;
         String q1="",q2="",q3="",q4="";
         
         scale_q1=scale_q2=scale_q3=scale_q4=scale_total=scale_targ=0;
         sust_q1=sust_q2=sust_q3=sust_q4=sust_total=sust_targ=0;
         
               q1="Oct - Dec '"+(Integer.parseInt(year)-1);
               q2="Jan - Mar '"+year;
               q3="Apr - Jun '"+year;
               q4="Jul - Sep '"+year;
           headers = ("Country,Targets,"+q1+","+q2+","+q3+","+q4+",Total Achieved,%Achieved").split(",");       
               
        String  county_quarterly_query = "SELECT 0 AS cum, county,burdencategory,supporttype,`Indicator Category`,`Main Indicator`,Indicator,"
                + "SUM(`Unknown F`) AS 'Unknown F', SUM(`Unknown M`) AS 'Unknown M',SUM(`< 60 Days`) AS '< 60 Days', SUM(`0-2 Months`) AS '0-2 Months',SUM(`2-12 Months`) AS '2-12 Months', SUM(`2Months - 4Years`) AS '2Months - 4Years', SUM(`<1Yr F`) AS '<1Yr F', "
                + "SUM(`<1Yr M`) AS '<1Yr M', SUM(`<1Yr Total`) AS '<1Yr Total', SUM(`1-4 Yrs F`) AS '1-4 Yrs F', SUM(`1-4 Yrs M`) AS '1-4 Yrs M', SUM(`5-9 Yrs F`) AS '5-9 Yrs F' , SUM(`5-9 Yrs M`) AS '5-9 Yrs M', SUM(`1-9 Yrs F`) AS '1-9 Yrs F' , SUM(`1-9 Yrs M`) AS '1-9 Yrs M', "
                + "SUM(`1-9 Yrs Total`) AS '1-9 Yrs Total', SUM(`10-14 Yrs F`) AS '10-14 Yrs F', SUM(`10-14 Yrs M`) AS '10-14 Yrs M',SUM(`15-19 Yrs F`) AS '15-19 Yrs F', SUM(`15-19 Yrs M`) AS '15-19 Yrs M', SUM(`20-24 Yrs F`) AS '20-24 Yrs F', SUM(`20-24 Yrs M`) AS '20-24 Yrs M', SUM(`25-29 Yrs F`) AS '25-29 Yrs F', "
                + "SUM(`25-29 Yrs M`) AS '25-29 Yrs M', SUM(`30-34 Yrs F`) AS '30-34 Yrs F', SUM(`30-34 Yrs M`) AS '30-34 Yrs M', SUM(`35-39 Yrs F`) AS '35-39 Yrs F', SUM(`35-39 Yrs M`) AS '35-39 Yrs M', SUM(`40-49 Yrs F`) AS '40-49 Yrs F', SUM(`40-49 Yrs`) AS '40-49 Yrs', SUM(`25-49 Yrs F`) AS '25-49 Yrs F', "
                + "SUM(`25-49 Yrs M`) AS '25-49 Yrs M', SUM(`50+ Yrs F`) AS '50+ Yrs F' ,SUM(`50+ Yrs M`) AS '50+ Yrs M', SUM(`Total Achieved`) AS 'Total Achieved', SUM(`Total F`) AS 'Total F', SUM(`Total M`) AS 'Total M', SUM(`Paeds F`) AS 'Paeds F', SUM(`Paeds M`) AS 'Paeds M', "
                + "SUM(`Total Paeds`) AS 'Total Paeds', SUM(`Adult F`) AS 'Adult F', SUM(`Adult M`) AS 'Adult M', SUM(`Total Adult`) AS 'Total Adult',SUM(`15-24 Yrs F`) AS '15-24 Yrs F', SUM(`15-24 Yrs M`) AS '15-24 Yrs M', SUM(`15-24 Yrs Total`) AS '15-24 Yrs Total', "
                + "year,quarter,`ART High Volume`,`PMTCT High Volume`,`High Volume`,"
                + "ordernumber,SUM(IFNULL(`Annual Target`,0))  AS 'Annual Target'"
                + " FROM ("+final_query+") AS data WHERE !("+cumulative_quarter_where+") GROUP BY Indicator,county,quarter,yearmonth ";

        if(cum_indicators.size()>0){
          String county_quarterly_query_cum = "SELECT 1 AS cum, county,burdencategory,supporttype,`Indicator Category`,`Main Indicator`,Indicator,"
                + "SUM(`Unknown F`) AS 'Unknown F', SUM(`Unknown M`) AS 'Unknown M',SUM(`< 60 Days`) AS '< 60 Days', SUM(`0-2 Months`) AS '0-2 Months',SUM(`2-12 Months`) AS '2-12 Months', SUM(`2Months - 4Years`) AS '2Months - 4Years', SUM(`<1Yr F`) AS '<1Yr F', "
                + "SUM(`<1Yr M`) AS '<1Yr M', SUM(`<1Yr Total`) AS '<1Yr Total', SUM(`1-4 Yrs F`) AS '1-4 Yrs F', SUM(`1-4 Yrs M`) AS '1-4 Yrs M', SUM(`5-9 Yrs F`) AS '5-9 Yrs F' , SUM(`5-9 Yrs M`) AS '5-9 Yrs M', SUM(`1-9 Yrs F`) AS '1-9 Yrs F' , SUM(`1-9 Yrs M`) AS '1-9 Yrs M', "
                + "SUM(`1-9 Yrs Total`) AS '1-9 Yrs Total', SUM(`10-14 Yrs F`) AS '10-14 Yrs F', SUM(`10-14 Yrs M`) AS '10-14 Yrs M',SUM(`15-19 Yrs F`) AS '15-19 Yrs F', SUM(`15-19 Yrs M`) AS '15-19 Yrs M', SUM(`20-24 Yrs F`) AS '20-24 Yrs F', SUM(`20-24 Yrs M`) AS '20-24 Yrs M', SUM(`25-29 Yrs F`) AS '25-29 Yrs F', "
                + "SUM(`25-29 Yrs M`) AS '25-29 Yrs M', SUM(`30-34 Yrs F`) AS '30-34 Yrs F', SUM(`30-34 Yrs M`) AS '30-34 Yrs M', SUM(`35-39 Yrs F`) AS '35-39 Yrs F', SUM(`35-39 Yrs M`) AS '35-39 Yrs M', SUM(`40-49 Yrs F`) AS '40-49 Yrs F', SUM(`40-49 Yrs`) AS '40-49 Yrs', SUM(`25-49 Yrs F`) AS '25-49 Yrs F', "
                + "SUM(`25-49 Yrs M`) AS '25-49 Yrs M', SUM(`50+ Yrs F`) AS '50+ Yrs F' ,SUM(`50+ Yrs M`) AS '50+ Yrs M', SUM(`Total Achieved`) AS 'Total Achieved', SUM(`Total F`) AS 'Total F', SUM(`Total M`) AS 'Total M', SUM(`Paeds F`) AS 'Paeds F', SUM(`Paeds M`) AS 'Paeds M', "
                + "SUM(`Total Paeds`) AS 'Total Paeds', SUM(`Adult F`) AS 'Adult F', SUM(`Adult M`) AS 'Adult M', SUM(`Total Adult`) AS 'Total Adult',SUM(`15-24 Yrs F`) AS '15-24 Yrs F', SUM(`15-24 Yrs M`) AS '15-24 Yrs M', SUM(`15-24 Yrs Total`) AS '15-24 Yrs Total', "
                + "year,quarter,`ART High Volume`,`PMTCT High Volume`,`High Volume`,"
                + "ordernumber,SUM(IFNULL(`Annual Target`,0))  AS 'Annual Target'"
                + " FROM ("+final_query+") AS data WHERE "+cumulative_quarter_yearmonth+" AND "+cumulative_quarter_where+"  GROUP BY Indicator,county,quarter,yearmonth ";  

          
         county_quarterly_query=" FROM ("+county_quarterly_query+" UNION ALL "+county_quarterly_query_cum+") AS county_qr GROUP BY Indicator,county ORDER BY ordernumber,Indicator,county"; 
        }
        else{
        county_quarterly_query=" FROM ("+county_quarterly_query+") AS county_qr GROUP BY Indicator,county ORDER BY ordernumber,Indicator,county";     
        }
        
        county_quarterly_query="SELECT cum,county,burdencategory,supporttype,`Indicator Category`,`Main Indicator`,Indicator,"
                +" SUM(`Total Achieved`) AS 'Total Achieved', "
                +" SUM( CASE WHEN quarter='1. Oct - Dec' THEN `Total Achieved` END) AS q1," +
                "  SUM( CASE WHEN quarter='2. Jan - Mar' THEN `Total Achieved` END) AS q2," +
                "  SUM( CASE WHEN quarter='3. Apr - Jun' THEN `Total Achieved` END) AS q3," +
                "  SUM( CASE WHEN quarter='4. Jul - Sep' THEN `Total Achieved` END) AS q4,"
                + "year,quarter,`ART High Volume`,`PMTCT High Volume`,`High Volume`,"
                + "ordernumber,SUM(IFNULL(`Annual Target`,0))  AS 'Annual Target'"
                +county_quarterly_query;   
        
         conn.rs = conn.st.executeQuery(county_quarterly_query);
         while(conn.rs.next()){
             
              Cell cellcounty=null,cellq1=null,cellq2=null,cellq3=null,cellq4=null;
            
             cum = conn.rs.getInt("cum");
             indic = conn.rs.getString("Indicator");
             county = conn.rs.getString("county");
             burden_cat = conn.rs.getString("burdencategory");
            if(!prev_indic.equals(indic)){
               if(i==0){
                i++;
                // for the first indic
                
               }
               else{
//                 go_up+=3;
 //for the next indic
 
 String[][] scale_sust = {{"Scale-Up",""+scale_targ,""+scale_q1,""+scale_q2,""+scale_q3,""+scale_q4,""+scale_total,""+getperachieved(scale_total, scale_targ)},{"Sustainance",""+sust_targ,""+sust_q1,""+sust_q2,""+sust_q3,""+sust_q4,""+sust_total,""+getperachieved(sust_total, sust_targ)}};
JSONObject obj = totals_sus_scale(scale_sust,i,shetachievements,perStyle,bordert);
 scale_q1=scale_q2=scale_q3=scale_q4=scale_total=scale_targ=0;
 sust_q1=sust_q2=sust_q3=sust_q4=sust_total=sust_targ=0;

i=Integer.parseInt(obj.get("i").toString());
shetachievements = (Sheet) obj.get("sheet");
        i+=2;
               }
               
             row_ach = shetachievements.createRow(i);
              for (int m=0;m<8;m++){
               Cell cell = row_ach.createCell(m); 
               cell.setCellValue(indic);
               cell.setCellStyle(borderh1);
              
              }
             shetachievements.addMergedRegion(new CellRangeAddress(i,i,0,7));
              i++; 
           //headers
            shetachievements =  addheaders(shetachievements,headers,i,borderh2);

            }
                i++;
              row_ach = shetachievements.createRow(i);
              
              cellcounty = row_ach.createCell(0);
              cellq1 = row_ach.createCell(2);
              cellq2 = row_ach.createCell(3);
              cellq3 = row_ach.createCell(4);
              cellq4 = row_ach.createCell(5);
            
               
            cellcounty.setCellStyle(stborder);
            cellq1.setCellStyle(stborder);
            cellq2.setCellStyle(stborder);
            cellq3.setCellStyle(stborder);
            cellq4.setCellStyle(stborder);
            
            cellcounty.setCellValue(county);
            if(conn.rs.getString("q1")!=null){
            cellq1.setCellValue(conn.rs.getInt("q1"));
            }
            else{
                cellq1.setCellValue("");  
            }
            
              if(conn.rs.getString("q2")!=null){
              cellq2.setCellValue(conn.rs.getInt("q2"));  
              }
              else{
                  cellq2.setCellValue("");  
              }
             
            if(conn.rs.getString("q3")!=null){
            cellq3.setCellValue(conn.rs.getInt("q3"));
            }
            else{
                cellq3.setCellValue("");  
            }
             
             if(conn.rs.getString("q4")!=null){
            cellq4.setCellValue(conn.rs.getInt("q4")); 
             }
             else{
             cellq4.setCellValue("");    
             }
            //
             target = get_targets(county,indic,conn);
            if(burden_cat.equalsIgnoreCase("Scale-up")){
                scale_q1+=conn.rs.getInt("q1");
                scale_q2+=conn.rs.getInt("q2");
                scale_q3+=conn.rs.getInt("q3");
                scale_q4+=conn.rs.getInt("q4");
                if(cum==0){
                 scale_total = scale_q1+scale_q2+scale_q3+scale_q4;   
                }
                scale_targ+=target;
                   System.out.println("scaleup : "+burden_cat);
             }
             else if(burden_cat.equals("Sustainance")){
              System.out.println("sus : "+burden_cat);
                sust_q1+=conn.rs.getInt("q1");
                sust_q2+=conn.rs.getInt("q2");
                sust_q3+=conn.rs.getInt("q3");
                sust_q4+=conn.rs.getInt("q4");
                if(cum==0){
                sust_total = sust_q1+sust_q2+sust_q3+sust_q4;
                }
                sust_targ+=target;
             }
             else{
                 System.out.println("Nothing found");
             }
            
            //
            
           if(cum==0){
            achieved=0;
            if(conn.rs.getString("q1")!=null){achieved+=conn.rs.getInt("q1");} 
            if(conn.rs.getString("q2")!=null){achieved+=conn.rs.getInt("q2");} 
            if(conn.rs.getString("q3")!=null){achieved+=conn.rs.getInt("q3");} 
            if(conn.rs.getString("q4")!=null){achieved+=conn.rs.getInt("q4");} 
          }
           else{
             achieved=0;
            if(conn.rs.getString("q1")!=null){achieved=conn.rs.getInt("q1");
            scale_total = scale_q1;
            sust_total = sust_q1;
            } 
            if(conn.rs.getString("q2")!=null){achieved=conn.rs.getInt("q2");
            scale_total = scale_q2;
            sust_total = sust_q2;
            } 
            if(conn.rs.getString("q3")!=null){achieved=conn.rs.getInt("q3");
            scale_total = scale_q3;
            sust_total = sust_q3;
            } 
            if(conn.rs.getString("q4")!=null){achieved=conn.rs.getInt("q4");
            scale_total = scale_q4;
            sust_total = sust_q4;
            }   
           }
          
              
             XSSFCell celltarg = (XSSFCell) row_ach.createCell(1);
             XSSFCell  celltotal =(XSSFCell) row_ach.createCell(6);
             XSSFCell  cellperc =(XSSFCell) row_ach.createCell(7);
             
            
              celltotal.setCellValue(achieved);
              celltarg.setCellValue(target);
              celltotal.setCellStyle(stborder);
              celltarg.setCellStyle(stborder);
            
              cellperc.setCellValue(getperachieved(achieved, target));
              cellperc.setCellStyle(perStyle);
          

          prev_indic = indic;
          prev_targets = target;
          prev_sum_county = achieved;
         }
         //FOR THE LAST COUNTY
            row_ach = shetachievements.getRow(i);   
             XSSFCell celltarg = (XSSFCell) row_ach.createCell(1);
             XSSFCell  celltotal =(XSSFCell) row_ach.createCell(6);
             XSSFCell  cellperc =(XSSFCell) row_ach.createCell(7);
     
              celltotal.setCellValue(prev_sum_county);
              celltarg.setCellValue(prev_targets);
              celltotal.setCellStyle(stborder);
              celltarg.setCellStyle(stborder);
            
              cellperc.setCellValue(getperachieved(prev_sum_county, prev_targets));
              cellperc.setCellStyle(perStyle);
              
               String[][] scale_sust = {{"Scale-Up",""+scale_targ,""+scale_q1,""+scale_q2,""+scale_q3,""+scale_q4,""+scale_total,""+getperachieved(scale_total, scale_targ)},{"Sustainance",""+sust_targ,""+sust_q1,""+sust_q2,""+sust_q3,""+sust_q4,""+sust_total,""+getperachieved(sust_total, sust_targ)}};
               totals_sus_scale(scale_sust,i,shetachievements,perStyle,bordert);
            
//                headers = ("Country,Targets,"+q1+","+q2+","+q3+","+q4+",Total Achieved,%Achieved").split(",");
//                shetachievements =  addheaders(shetachievements,headers,i-(datarows+3),borderh2);

            
         //end of county quarterly achievements
         
            System.out.println("final targets : "+final_query_targets);
            System.out.println("final data : "+query);
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
    public ArrayList cum_where(ArrayList indicators,dbConn conn,String startym,String endym) throws SQLException{
     String qr="",where_ym=" (",where_cum="(";
     int counter=0;
      ArrayList data = new ArrayList();
      data.clear();
//      String getcums = "SELECT  table_name,level3,level4,is_cumulative,facility_target_column  FROM achievement_mapping WHERE is_cumulative=1";
//      conn.rs = conn.st.executeQuery(getcums);
//      
      for(int i=0;i<indicators.size();i++){
String table = indicators.get(i).toString().split(",")[0];
String level3 = indicators.get(i).toString().split(",")[1];
String level4 = indicators.get(i).toString().split(",")[2];
         if(level4!=null && !level4.equals("null") && !level4.equals("")){
                           qr+="SELECT yearmonth,quarter FROM "+table+" WHERE ("+table+".level3='"+level3+"' && "+table+".level4='"+level4+"') UNION ALL ";
                           counter++;
                        where_cum+=" (Indicator="+level3+" "+level4+") OR ";
                       }
                       else{
                           qr+="SELECT yearmonth,quarter FROM "+table+" WHERE ("+table+".level3='"+level3+"' && ("+table+".level4 IS NULL OR "+table+".level4=''))  UNION ALL ";
                           counter++;
                         where_cum+=" (Indicator='"+level3+"') OR ";
                       }   
                }
      if(counter>0){
      qr = removeLastChars(qr, 11);
      where_cum = removeLastChars(where_cum, 3)+")";
      
      String final_query = "SELECT MAX(yearmonth) FROM ("+qr+") AS ymdata GROUP BY quarter ORDER BY quarter";
      conn.rs2 = conn.st2.executeQuery(final_query);
      
      while(conn.rs2.next()){
      where_ym += "yearmonth="+conn.rs2.getString(1)+" OR ";
      }
     
         where_ym = removeLastChars(where_ym, 3)+")";
  }
      else{
        where_ym = where_cum = " 0=1 ";  
      }
          ArrayList dt = new ArrayList();
            dt.clear();

            dt.add(where_ym);
            dt.add(where_cum);
            
      return dt;
  }  
    public String getmaxyearmonth(dbConn conn,String table, String level3,String level4,int iscum,String cum_yearmonth) throws SQLException{
          String sele="",ym="";
          
         if(iscum==1) {
              if(level4!=null && !level4.equals("null") && !level4.equals("")){
          sele = "SELECT COUNT(id) as occur,yearmonth,quarter  FROM "+table+" WHERE level3='"+level3+"' AND level4='"+level4+"' AND "+cum_yearmonth+"  group by yearmonth ORDER BY occur DESC ,yearmonth DESC LIMIT 1";      
              }
              else{
              sele = "SELECT COUNT(id) as occur,yearmonth,quarter  FROM "+table+" WHERE level3='"+level3+"' AND (level4 IS NULL OR level4='') AND "+cum_yearmonth+" group by yearmonth ORDER BY occur DESC ,yearmonth DESC LIMIT 1";      
          
              }
              }
         else{  
      if(level4!=null && !level4.equals("null") && !level4.equals("")){
    sele = "SELECT COUNT(id) as occur,yearmonth,quarter  FROM "+table+" WHERE level3='"+level3+"' AND level4='"+level4+"'  group by yearmonth ORDER BY occur DESC ,yearmonth DESC LIMIT 1";  
      }
      
      else{
      sele = "SELECT COUNT(id) as occur,yearmonth,quarter  FROM "+table+" WHERE level3='"+level3+"' AND (level4 IS NULL OR level4='')  group by yearmonth ORDER BY occur DESC ,yearmonth DESC LIMIT 1";      
      }
         }
        System.out.println("max yearmonth query : "+sele);       
    conn.rs2 = conn.st2.executeQuery(sele);
    if(conn.rs2.next()){
        ym = conn.rs2.getString(2);
    }
        System.out.println(level3+":"+ym);
    
    return ym;
  }
  
  public int get_targets(String county,String indic,dbConn conn) throws SQLException{
      int tar = 0;
      String gettarg="SELECT CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS indicator,facility_target_column FROM achievement_mapping WHERE facility_target_column!='' AND facility_target_column IS NOT NULL HAVING indicator='"+indic+"'";
      conn.rs2 = conn.st2.executeQuery(gettarg);
      if(conn.rs2.next()){
          String gettargets = "SELECT SUM(IFNULL("+conn.rs2.getString("facility_target_column")+",0)) FROM facilitytarget WHERE county='"+county+"'";
          conn.rs1  = conn.st1.executeQuery(gettargets);
          if(conn.rs1.next()){
              tar = conn.rs1.getInt(1);
          }
      }
      return tar;
  }





public JSONObject totals_sus_scale(String [][]scale_sust,int i,Sheet shetachievements,CellStyle perStyle, CellStyle bordert){
    JSONObject obj = new JSONObject();
    
    Row row_ach = null;
     for(int m=0;m<scale_sust.length;m++){
         i++;
        row_ach = shetachievements.createRow(i);   
        String [] indiv = scale_sust[m];
        for(int n=0;n<indiv.length;n++){

            String value = indiv[n];
            Cell cell = row_ach.createCell(n); 
            if(isNumeric(value)){
              cell.setCellValue(Double.parseDouble(value));       
            }
            else{
             cell.setCellValue(value);    
            }
           
            if((n+1)==indiv.length){
             cell.setCellStyle(perStyle);   
            }
            else{
                cell.setCellStyle(bordert);
            }

        }
     }
        obj.put("i", i);
        obj.put("sheet", shetachievements);
   
        
        return obj;
}
}
