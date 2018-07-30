/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import static Db.OSValidator.isUnix;
import Db.dbConn;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;
/**
 *
 * @author GNyabuto
 */
public class Achievements extends HttpServlet {
HttpSession session;
String startyearmonth,endyearmonth,year;
String query_where1="",query_where2="",t_query="",query_where3="",query="",cum_query="",county_cum_query="";
String cum_query_where1="",cum_query_where2="",cum_query_where3="";
String table1,table2,table3;
int table1_elems,table2_elems,table3_elems;
int cum_table1_elems,cum_table2_elems,cum_table3_elems;
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
           JSONObject obj_targ = new JSONObject();
           JSONObject county_targets;

            year = request.getParameter("year");
            period = request.getParameter("period");

            switch (period) {
                case "1":
                    year = request.getParameter("year");
                    startyearmonth = (Integer.parseInt(year)-1)+"10";
                    endyearmonth = year+"09";
                    break;
                case "2":
                    semi = request.getParameter("semi_annual");
                    if(semi.equals("1")){
                        startyearmonth = (Integer.parseInt(year)-1)+"10";
                        endyearmonth = year+"03";
                    }
                    else{
                        startyearmonth = year+"04";
                        endyearmonth = year+"09";
                    }   break;
                case "3":
                    quarter = request.getParameter("quarter");
                    switch (quarter) {
                        case "1":
                            startyearmonth = (Integer.parseInt(year)-1)+"10";
                            endyearmonth = (Integer.parseInt(year)-1)+"12";
                            break;
                        case "2":
                            startyearmonth = year+"01";
                            endyearmonth = year+"03";
                            break;
                        case "3":
                            startyearmonth = year+"04";
                            endyearmonth = year+"06";
                            break;
                        case "4":
                            startyearmonth = year+"07";
                            endyearmonth = year+"09";
                            break;
                        default:
                            break;
                    }    break;
                case "4":
                    month = request.getParameter("month");
                    if(Integer.parseInt(month)>=10){
                        endyearmonth = startyearmonth = (Integer.parseInt(year)-1)+""+month;
                    }
                    else{
                        endyearmonth = startyearmonth = year+"0"+month;
                    }   break;
                default:
                    break;
            }
            
            System.out.println("start yearmonth : "+startyearmonth+" end year month : "+endyearmonth);

        cum_indicators.clear();   
         String allpath = getServletContext().getRealPath("/achievements_1.xlsx");
         String mydrive = allpath.substring(0, 1);
         
          Date da= new Date();
            String dat2 = da.toString().replace(" ", "_");
             dat2 = dat2.toString().replace(":", "_");

          String np=mydrive+":\\HSDSA\\Dashboards\\Achievements_"+dat2+".xlsx";
          
          
           if (isUnix()) {
            
            np="/HSDSA/Dashboards/Achievements_"+dat2+".xlsx";
           
           
                             }
          
            System.out.println("path:: "+np);
              String sr = getServletContext().getRealPath("/achievements_1.xlsx");
              
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
   XSSFWorkbook wb1 = new XSSFWorkbook(pkg);

    SXSSFWorkbook wb = new SXSSFWorkbook(wb1, 1000,true); 
    
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
    
        XSSFFont fontx = (XSSFFont) wb.createFont();
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
         query_where1=query_where2=query_where3=t_query="";query="";
         cum_query_where1=cum_query_where2=cum_query_where3="";
            table1_elems=table2_elems=table3_elems=0;
            cum_table1_elems=cum_table2_elems=cum_table3_elems=0;
            targetsquery=indicator="";
            county_cum_query = "";
            query="SELECT 0 AS is_cum,county,burdencategory,constituency,subcounty,ward,facility,mflcode,supporttype,ifnull(level1,'') AS 'Indicator Category',ifnull(level2,'') AS 'Main Indicator',CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS 'Indicator',"
                + "SUM(IFNULL(unknown_f,0)) AS 'Unknown F', SUM(IFNULL(unknown_m,0)) AS 'Unknown M', SUM(IFNULL(d60,0)) AS '< 60 Days', SUM(IFNULL(mn_0_2,0)) AS '0-2 Months', SUM(IFNULL(mn_2_12,0)) AS '2-12 Months', SUM(IFNULL(mn_2_4y,0)) AS '2Months - 4Years', SUM(IFNULL(f_1,0)) AS '<1Yr F', "
                + "SUM(IFNULL(m_1,0)) AS '<1Yr M', SUM(IFNULL(t_1,0)) AS '<1Yr Total', SUM(IFNULL(f_4,0)) AS '1-4 Yrs F', SUM(IFNULL(m_4,0)) AS '1-4 Yrs M', SUM(IFNULL(f_5_9,0)) AS '5-9 Yrs F' , SUM(IFNULL(m_5_9,0)) AS '5-9 Yrs M', SUM(IFNULL(f_1_9,0)) AS '1-9 Yrs F' , SUM(IFNULL(m_1_9,0)) AS '1-9 Yrs M', "
                + "SUM(IFNULL(t_1_9,0)) AS '1-9 Yrs Total', SUM(IFNULL(f_14,0)) AS '10-14 Yrs F', SUM(IFNULL(m_14,0)) AS '10-14 Yrs M', SUM(IFNULL(f_19,0)) AS '15-19 Yrs F', SUM(IFNULL(m_19,0)) AS '15-19 Yrs M', SUM(IFNULL(f_24,0)) AS '20-24 Yrs F', SUM(IFNULL(m_24,0)) AS '20-24 Yrs M', SUM(IFNULL(f_29,0)) AS '25-29 Yrs F', "
                + "SUM(IFNULL(m_29,0)) AS '25-29 Yrs M', SUM(IFNULL(f_34,0)) AS '30-34 Yrs F', SUM(IFNULL(m_34,0)) AS '30-34 Yrs M', SUM(IFNULL(f_39,0)) AS '35-39 Yrs F', SUM(IFNULL(m_39,0)) AS '35-39 Yrs M', SUM(IFNULL(f_49,0)) AS '40-49 Yrs F', SUM(IFNULL(m_49,0)) AS '40-49 Yrs', SUM(IFNULL(f_25_49,0)) AS '25-49 Yrs F', "
                + "SUM(IFNULL(m_25_49,0)) AS '25-49 Yrs M', SUM(IFNULL(f_50,0)) '50+ Yrs F' , SUM(IFNULL(m_50,0)) AS '50+ Yrs M', SUM(IFNULL(total,0)) AS 'Total Achieved', SUM(IFNULL(total_f,0)) AS 'Total F', SUM(IFNULL(total_m,0)) AS 'Total M', SUM(IFNULL(paeds_f,0)) AS 'Paeds F', SUM(IFNULL(paeds_m,0)) AS 'Paeds M', "
                + "SUM(IFNULL(paeds,0)) AS 'Total Paeds', SUM(IFNULL(adult_f,0)) AS 'Adult F', SUM(IFNULL(adult_m,0)) AS 'Adult M', SUM(IFNULL(adult,0)) AS 'Total Adult', SUM(IFNULL(f_15_24,0)) AS '15-24 Yrs F', SUM(IFNULL(m_15_24,0)) AS '15-24 Yrs M', SUM(IFNULL(t_15_24,0)) AS '15-24 Yrs Total', "
                + "year,semiannual,quarter,month,yearmonth,ownedby,facilitytype AS 'Facility Type',art_hv AS 'ART High Volume',htc_hv AS 'HTC High Volume',pmtct_hv AS 'PMTCT High Volume',activity_hv AS 'High Volume',latitude,longitude,maleclinic AS 'Male Clinic',"
                    + "adoleclinic AS 'Adolscent Clinic',viremiaclinic AS 'Viremia Clinic',emrsite AS 'EMR Site',linkdesk AS 'Link Desk',ordernumber FROM (";
            
            cum_query="SELECT  1 AS is_cum,county,burdencategory,constituency,subcounty,ward,facility,mflcode,supporttype,ifnull(level1,'') AS 'Indicator Category',ifnull(level2,'') AS 'Main Indicator',CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS 'Indicator',"
                + "SUM(IFNULL(unknown_f,0)) AS 'Unknown F', SUM(IFNULL(unknown_m,0)) AS 'Unknown M', SUM(IFNULL(d60,0)) AS '< 60 Days', SUM(IFNULL(mn_0_2,0)) AS '0-2 Months', SUM(IFNULL(mn_2_12,0)) AS '2-12 Months', SUM(IFNULL(mn_2_4y,0)) AS '2Months - 4Years', SUM(IFNULL(f_1,0)) AS '<1Yr F', "
                + "SUM(IFNULL(m_1,0)) AS '<1Yr M', SUM(IFNULL(t_1,0)) AS '<1Yr Total', SUM(IFNULL(f_4,0)) AS '1-4 Yrs F', SUM(IFNULL(m_4,0)) AS '1-4 Yrs M', SUM(IFNULL(f_5_9,0)) AS '5-9 Yrs F' , SUM(IFNULL(m_5_9,0)) AS '5-9 Yrs M', SUM(IFNULL(f_1_9,0)) AS '1-9 Yrs F' , SUM(IFNULL(m_1_9,0)) AS '1-9 Yrs M', "
                + "SUM(IFNULL(t_1_9,0)) AS '1-9 Yrs Total', SUM(IFNULL(f_14,0)) AS '10-14 Yrs F', SUM(IFNULL(m_14,0)) AS '10-14 Yrs M', SUM(IFNULL(f_19,0)) AS '15-19 Yrs F', SUM(IFNULL(m_19,0)) AS '15-19 Yrs M', SUM(IFNULL(f_24,0)) AS '20-24 Yrs F', SUM(IFNULL(m_24,0)) AS '20-24 Yrs M', SUM(IFNULL(f_29,0)) AS '25-29 Yrs F', "
                + "SUM(IFNULL(m_29,0)) AS '25-29 Yrs M', SUM(IFNULL(f_34,0)) AS '30-34 Yrs F', SUM(IFNULL(m_34,0)) AS '30-34 Yrs M', SUM(IFNULL(f_39,0)) AS '35-39 Yrs F', SUM(IFNULL(m_39,0)) AS '35-39 Yrs M', SUM(IFNULL(f_49,0)) AS '40-49 Yrs F', SUM(IFNULL(m_49,0)) AS '40-49 Yrs', SUM(IFNULL(f_25_49,0)) AS '25-49 Yrs F', "
                + "SUM(IFNULL(m_25_49,0)) AS '25-49 Yrs M', SUM(IFNULL(f_50,0)) '50+ Yrs F' , SUM(IFNULL(m_50,0)) AS '50+ Yrs M', SUM(IFNULL(total,0)) AS 'Total Achieved', SUM(IFNULL(total_f,0)) AS 'Total F', SUM(IFNULL(total_m,0)) AS 'Total M', SUM(IFNULL(paeds_f,0)) AS 'Paeds F', SUM(IFNULL(paeds_m,0)) AS 'Paeds M', "
                + "SUM(IFNULL(paeds,0)) AS 'Total Paeds', SUM(IFNULL(adult_f,0)) AS 'Adult F', SUM(IFNULL(adult_m,0)) AS 'Adult M', SUM(IFNULL(adult,0)) AS 'Total Adult', SUM(IFNULL(f_15_24,0)) AS '15-24 Yrs F', SUM(IFNULL(m_15_24,0)) AS '15-24 Yrs M', SUM(IFNULL(t_15_24,0)) AS '15-24 Yrs Total', "
                + "year,semiannual,quarter,month,yearmonth,ownedby,facilitytype AS 'Facility Type',art_hv AS 'ART High Volume',htc_hv AS 'HTC High Volume',pmtct_hv AS 'PMTCT High Volume',activity_hv AS 'High Volume',latitude,longitude,maleclinic AS 'Male Clinic',"
                    + "adoleclinic AS 'Adolscent Clinic',viremiaclinic AS 'Viremia Clinic',emrsite AS 'EMR Site',linkdesk AS 'Link Desk',ordernumber FROM (";
           
            String build_query = "SELECT table_name,level3,level4,IFNULL(is_cumulative,0) AS is_cumulative,facility_target_column from achievement_mapping WHERE is_active=1";
            conn.rs = conn.st.executeQuery(build_query);
            while(conn.rs.next()){
//                curryearmonth = endyearmonth;
               switch (conn.rs.getString(1)) {
                   case "table1":
                       if(conn.rs.getString(3)!=null){
                           if(conn.rs.getInt("is_cumulative")==1){
                           cum_query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           cum_table1_elems++;
                           }
                           else{
                           query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table1_elems++;
                           }
                       }
                       else{
                            if(conn.rs.getInt("is_cumulative")==1){
                           cum_query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           cum_table1_elems++;
                            }
                            else{
                           query_where1+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           table1_elems++;   
                            }
                       } 
                       
                       break;
                   case "table2":
                       if(conn.rs.getString(3)!=null){
                           if(conn.rs.getInt("is_cumulative")==1){
                           cum_query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           cum_table2_elems++;
                           }
                           else{
                           query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table2_elems++;    
                           }
                         
                       }
                       else{
                          if(conn.rs.getInt("is_cumulative")==1){ 
                           cum_query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           cum_table2_elems++;
                          }
                          else{
                            query_where2+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           table2_elems++;   
                          }
                       } 
                       
                       break;
                   case "table3":
                       if(conn.rs.getString(3)!=null){
                         if(conn.rs.getInt("is_cumulative")==1){ 
                           cum_query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           cum_table3_elems++;
                         }
                         else{
                            query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && "+conn.rs.getString(1)+".level4='"+conn.rs.getString(3)+"') OR ";
                           table3_elems++; 
                         }
                       }
                       else{
                            if(conn.rs.getInt("is_cumulative")==1){ 
                           cum_query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                           cum_table3_elems++;
                            }
                            else{
                             query_where3+="("+conn.rs.getString(1)+".level3='"+conn.rs.getString(2)+"' && ("+conn.rs.getString(1)+".level4 IS NULL OR "+conn.rs.getString(1)+".level4='')) OR ";
                             table3_elems++;    
                            }
                       } 
                       
                       break;
                   default:
                       break;
                        
               }
               //select cum indicators 
               if(conn.rs.getInt("is_cumulative")==1){
                cum_indicators.add(conn.rs.getString(1)+","+conn.rs.getString(2)+","+conn.rs.getString(3));
                
               }
               
               //build targets column
            if(conn.rs.getString(5)!=null && !conn.rs.getString(5).equals("")){
                if(conn.rs.getString(3)!=null){
                  indicator = conn.rs.getString(2)+" "+conn.rs.getString(3);  
                }
                else{
                indicator = conn.rs.getString(2)+" ";    
                } 
                
        t_query+=" (indicator='"+conn.rs.getString("facility_target_column")+"') OR ";

          targetscounter++;
             }
            }
           // start cum yearmonth
            ArrayList array_cum = cum_where(cum_indicators,conn,startyearmonth,endyearmonth);
         String cumulative_quarter_yearmonth = array_cum.get(0).toString();
         String cumulative_quarter_where = array_cum.get(1).toString();
         String max_yearmn_cum = array_cum.get(2).toString();
          
           //end yearmonth
            
           
            if(targetscounter>0){
             t_query = " AND ("+removeLastChars(t_query,3)+")";  
            }
             targetsquery=" SELECT targets.MFLCode AS mflcode,CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS Indicator,target " +
                          " FROM targets LEFT JOIN achievement_mapping ON achievement_mapping.facility_target_column=targets.indicator " +
                          " WHERE year='"+year+"' "+t_query+" "; 
             conn.rs = conn.st.executeQuery(targetsquery);
             while(conn.rs.next()){
                 String indickey = conn.rs.getString(1)+"_"+conn.rs.getString(2);
                 int target = conn.rs.getInt(3);
                 obj_targ.put(indickey, target);
             }
             System.out.println("Ended adding targets : "+obj_targ.size());
//            end of getting targets
                //GETTING COUNTY TARGETS FOR ACHIEVEMENTS REPORT
               county_targets =get_targets(t_query,conn);
                
                // END ACHIEVEMENTS REPORT

            System.out.println("target : "+targetsquery);
          if(table1_elems>0) {
              query_where1 = removeLastChars(query_where1,3);
              query+=" SELECT * FROM table1 WHERE ("+query_where1+") AND yearmonth BETWEEN "+startyearmonth+" AND "+endyearmonth+"  UNION ALL "; 
          }
         if(table2_elems>0) {
             query_where2 = removeLastChars(query_where2,3);
              query+=" SELECT * FROM table2 WHERE ("+query_where2+") AND yearmonth BETWEEN "+startyearmonth+" AND "+endyearmonth+"  UNION ALL ";
         }
         if(table3_elems>0) {
             query_where3 = removeLastChars(query_where3,3);
              query+=" SELECT * FROM table3 WHERE ("+query_where3+") AND yearmonth BETWEEN "+startyearmonth+" AND "+endyearmonth+"  UNION ALL ";
         }
         
          if(cum_table1_elems>0) {
              cum_query_where1 = removeLastChars(cum_query_where1,3); 
              cum_query+=" SELECT * FROM table1 WHERE ("+cum_query_where1+") AND "+max_yearmn_cum+" UNION ALL "; 
              
              county_cum_query+=" SELECT * FROM table1 WHERE ("+cum_query_where1+") UNION ALL "; 
          }
         if(cum_table2_elems>0) {
             cum_query_where2 = removeLastChars(cum_query_where2,3);
              cum_query+=" SELECT * FROM table2 WHERE ("+cum_query_where2+") AND "+max_yearmn_cum+" UNION ALL ";
              
              county_cum_query+=" SELECT * FROM table2 WHERE ("+cum_query_where2+")UNION ALL ";
         }
         if(cum_table3_elems>0) {
             cum_query_where3 = removeLastChars(cum_query_where3,3);
              cum_query+=" SELECT * FROM table3 WHERE ("+cum_query_where3+") AND "+max_yearmn_cum+" UNION ALL ";
              
              county_cum_query+=" SELECT * FROM table3 WHERE ("+cum_query_where3+")UNION ALL ";
         }
         
         query = removeLastChars(query,11);
         cum_query = removeLastChars(cum_query,11);
         county_cum_query = removeLastChars(county_cum_query,11);
         
         query+=") AS all_data  GROUP BY Indicator,mflcode,yearmonth  ";
         cum_query+=") AS all_data2  GROUP BY Indicator,mflcode,yearmonth ";
         county_cum_query="SELECT county,CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS 'Indicator', IFNULL(total,0) AS achieved,quarter,yearmonth FROM("+county_cum_query+")) AS all_data2 ";
         
            System.out.println("county cum : "+county_cum_query);
            System.out.println("query : "+query);
            System.out.println("cumulative query : "+cum_query);
         
         query = "SELECT * FROM ("+query+" UNION "+cum_query+") AS data_ GROUP BY Indicator,mflcode,yearmonth ORDER BY ordernumber ASC,Indicator ASC,county ASC,yearmonth DESC";
                   
           //get achieved per quarter for cumulatives
           
          JSONObject obj_cumulatives = get_cum_data(cumulative_quarter_yearmonth,cumulative_quarter_where,conn,county_cum_query);
           
            System.out.println("obj cumulatives : "+obj_cumulatives.size());
           //end of getting cumulatives
           
            System.out.println("data query : "+query);
            String prev_indic="";
        //output raw data
         conn.rs = conn.st.executeQuery(query);
          ResultSetMetaData metaData = conn.rs.getMetaData();
       int col_count = metaData.getColumnCount(); //number of column
       
         int row_num=2;
         
        while(conn.rs.next()){
          Row row=null;
          if(shet.getRow(row_num)!=null){
          row=shet.getRow(row_num);
          }
          else
          {
           row=shet.createRow(row_num);
          }
          for(int i=1;i<col_count;i++){
              String value=conn.rs.getString(i+1);
               Cell cell= null;
               
               if(row.getCell(i-1)!=null){
               cell=row.getCell(i-1);
               }
               else{cell= row.createCell(i-1);}
                
               
               
               if(isNumeric(value)){
               cell.setCellValue(Double.parseDouble(value));
               }
               else{
               cell.setCellValue(value);    
               }
          
String indic = conn.rs.getString("mflcode")+"_"+conn.rs.getString("Indicator");
        // last column output targets
        value="0"; // have a default of 0 for targets
          if((i+1)==col_count && !prev_indic.equals(indic)){
              if(obj_targ.containsKey(indic)){
             value = obj_targ.get(indic).toString(); // read targets from json object
             obj_targ.remove(indic);
               prev_indic=indic;
              }
            }
                cell= row.createCell(i);
                cell.setCellValue(Double.parseDouble(value));
           
          // end of targets
          }
         
//            System.out.println("targets size : "+obj_targ.size());
System.out.println("row number : "+row_num);
          row_num++;
         }
        // end of raw data output
        XSSFSheet shet1= wb.getXSSFWorkbook().getSheet("raw data");
        if(1==1){
    
        // tell your xssfsheet where its content begins and where it ends
((XSSFSheet)shet1).getCTWorksheet().getDimension().setRef("A1:BY" + (shet.getLastRowNum() + 1));

CTTable ctTable = ((XSSFSheet)shet1).getTables().get(0).getCTTable();

ctTable.setRef("A1:BY" + (shet.getLastRowNum() + 1)); // adjust reference as needed

//ctTable.unsetSortState(); // if you had sorted the data in Excel before reading the file,
                          // you may want an unsorted table in your output file

//CTTableColumns ctColumns = ctTable.getTableColumns(); // setting new table columns will
                                                      // muck everything up, 
                                                      // so adjust the existing ones

// remove the old columns first if you plan on expanding your table in the column direction
//for (int i = 0; i < ctColumns.getCount(); i++) {
//    ctColumns.removeTableColumn(0);
//}

// throw in your new columns
//for (int i = 0; i < tableHeaders.size(); i++) {
//    CTTableColumn column = ctColumns.addNewTableColumn();
//    column.setName(tableHeaders.get(i));
//    column.setId(i + 1);
//}

// for some reason this isn't being take care of when columns are modified,
// so fix the column count manually
//ctColumns.setCount(tableHeaders.size());
        
        }
        //remove the second row
        //shet1.removeRow(shet1.getRow(1));
        
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
         int achieved,target;
         Row row_ach = null;
         String county,indic,burden_cat;
         
         int cum;
         int scale_q1,scale_q2,scale_q3,scale_q4,scale_total,scale_targ;
         int sust_q1,sust_q2,sust_q3,sust_q4,sust_total,sust_targ;
         prev_indic="";
         String[] headers = new String[8] ;
         String q1="",q2="",q3="",q4="";
         
         
         String _q1="",_q2="",_q3="",_q4="",_achieved;
         
         int q1_v=0,q2_v=0,q3_v=0,q4_v=0;
         
         scale_q1=scale_q2=scale_q3=scale_q4=scale_total=scale_targ=0;
         sust_q1=sust_q2=sust_q3=sust_q4=sust_total=sust_targ=0;
         
               q1="Oct - Dec '"+(Integer.parseInt(year)-1);
               q2="Jan - Mar '"+year;
               q3="Apr - Jun '"+year;
               q4="Jul - Sep '"+year;
               
           headers = ("Country,Targets,"+q1+","+q2+","+q3+","+q4+",Total Achieved,%Achieved").split(",");       
           String final_query="";
             System.out.println("cum where-------------"+cumulative_quarter_where); 
             System.out.println("cum quarter-------------"+cumulative_quarter_yearmonth); 
             
             
        query = "SELECT is_cum,county,burdencategory,supporttype,`Indicator Category`,`Main Indicator`,Indicator,"
                +" SUM(`Total Achieved`) AS 'Total Achieved', "
                +" SUM( CASE WHEN quarter='1. Oct - Dec' THEN `Total Achieved` END) AS q1," +
                "  SUM( CASE WHEN quarter='2. Jan - Mar' THEN `Total Achieved` END) AS q2," +
                "  SUM( CASE WHEN quarter='3. Apr - Jun' THEN `Total Achieved` END) AS q3," +
                "  SUM( CASE WHEN quarter='4. Jul - Sep' THEN `Total Achieved` END) AS q4," +
                "  year,quarter,`ART High Volume`,`PMTCT High Volume`,`High Volume`," +
                "  ordernumber,0  AS 'Annual Target' " +
                " FROM ("+query+") AS data  GROUP BY Indicator,county";

      
         
            System.out.println("final query : "+query);
            
         conn.rs = conn.st.executeQuery(query);
         while(conn.rs.next()){
             q1_v=q2_v=q3_v=q4_v=0;
             _q1=_q2=_q3=_q4=_achieved="0";
              Cell cellcounty=null,cellq1=null,cellq2=null,cellq3=null,cellq4=null;
            
             cum = conn.rs.getInt("is_cum");
             indic = conn.rs.getString("Indicator");
             county = conn.rs.getString("county");
             burden_cat = conn.rs.getString("burdencategory");
             
             if(cum==1){
                 if(obj_cumulatives.containsKey(county+"_"+indic)){
                 JSONObject county_data = (JSONObject) obj_cumulatives.get(county+"_"+indic);
                     System.out.println("county data size : "+county_data.size()+"county keys : "+county_data.toJSONString());
                 if(county_data.containsKey("q1") && county_data.containsKey("q2") && county_data.containsKey("q3") && county_data.containsKey("q4")){
                 if(county_data.get("q1")!=null){_q1 = county_data.get("q1").toString();} else{_q1=null;}
                 if(county_data.get("q2")!=null){_q2 = county_data.get("q2").toString();} else{_q2=null;}
                 if(county_data.get("q3")!=null){_q3 = county_data.get("q3").toString();} else{_q3=null;}
                 if(county_data.get("q4")!=null){_q4 = county_data.get("q4").toString();} else{_q4=null;}

             }
                 }    
             }
             else{
              _q1=conn.rs.getString("q1");
                _q2=conn.rs.getString("q2");
                _q3=conn.rs.getString("q3");
                _q4=conn.rs.getString("q4");    
             }
             
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
            if(_q1!=null){
                q1_v=Integer.parseInt(_q1);
            cellq1.setCellValue(q1_v);
            }
            else{
                cellq1.setCellValue("");  
            }
            
              if(_q2!=null){
                  q2_v=Integer.parseInt(_q2);
              cellq2.setCellValue(q2_v);  
              }
              else{
                  cellq2.setCellValue("");  
              }
             
            if(_q3!=null){
                q3_v=Integer.parseInt(_q3);
            cellq3.setCellValue(q3_v);
            }
            else{
                cellq3.setCellValue("");  
            }
             
             if(_q4!=null){
                 q4_v=Integer.parseInt(_q4);
            cellq4.setCellValue(q4_v); 
             }
             else{
             cellq4.setCellValue("");    
             }
            //start of get county targets
            
              if(county_targets.containsKey(county+"_"+indic)){
             target = Integer.parseInt(county_targets.get(county+"_"+indic).toString());
             county_targets.remove(county+"_"+indic);
             }
              else{
                  target=0;
              }
             System.out.println("size of county targets : "+county_targets.size());
            
            //end targets
//             target = get_targets(county,indic,conn);
            if(burden_cat.equalsIgnoreCase("Scale-up")){
                scale_q1+=q1_v;
                scale_q2+=q2_v;
                scale_q3+=q3_v;
                scale_q4+=q4_v;
                if(cum==0){
                 scale_total = scale_q1+scale_q2+scale_q3+scale_q4;   
                }
                scale_targ+=target;
                   System.out.println("scaleup : "+burden_cat);
             }
             else if(burden_cat.equals("Sustainance")){
              System.out.println("sus : "+burden_cat);
                sust_q1+=q1_v;
                sust_q2+=q2_v;
                sust_q3+=q3_v;
                sust_q4+=q4_v;
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
            if(_q1!=null){achieved+=q1_v;} 
            if(_q2!=null){achieved+=q2_v;} 
            if(_q3!=null){achieved+=q3_v;} 
            if(_q4!=null){achieved+=q4_v;} 
          }
           else{
             achieved=0;
            if(_q1!=null){
            achieved=q1_v;
            scale_total = scale_q1;
            sust_total = sust_q1;
            } 
            if(_q2!=null){
            achieved=q2_v;
            scale_total = scale_q2;
            sust_total = sust_q2;
            } 
            if(_q3!=null){
            achieved=q3_v;
            scale_total = scale_q3;
            sust_total = sust_q3;
            } 
            if(_q4!=null){
            achieved=q4_v;
            scale_total = scale_q4;
            sust_total = sust_q4;
            }   
           }
          
              
             Cell celltarg =  row_ach.createCell(1);
             Cell  celltotal = row_ach.createCell(6);
             Cell  cellperc =row_ach.createCell(7);
             
            
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
             Cell celltarg = row_ach.createCell(1);
             Cell  celltotal =row_ach.createCell(6);
             Cell  cellperc =row_ach.createCell(7);
     
              celltotal.setCellValue(prev_sum_county);
              celltarg.setCellValue(prev_targets);
              celltotal.setCellStyle(stborder);
              celltarg.setCellStyle(stborder);
            
              cellperc.setCellValue(getperachieved(prev_sum_county, prev_targets));
              cellperc.setCellStyle(perStyle);
              
               String[][] scale_sust = {{"Scale-Up",""+scale_targ,""+scale_q1,""+scale_q2,""+scale_q3,""+scale_q4,""+scale_total,""+getperachieved(scale_total, scale_targ)},{"Sustainance",""+sust_targ,""+sust_q1,""+sust_q2,""+sust_q3,""+sust_q4,""+sust_total,""+getperachieved(sust_total, sust_targ)}};
               totals_sus_scale(scale_sust,i,shetachievements,perStyle,bordert);
            
         //end of county quarterly achievements
         
            System.out.println("final data : "+query);
            System.out.println("final query : "+final_query);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        wb.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();
        response.setContentType("application/ms-excel");
        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=Achievements_Report_for_"+startyearmonth+"_to_"+endyearmonth+".xlsx");
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
                Cell  cellperc =rowheader.createCell(cellpos);
                cellperc.setCellValue(header);
                cellperc.setCellStyle(stborder);
                cellpos++;
            }
      return sheet;   
     }
     
     public ArrayList cum_where(ArrayList indicators,dbConn conn,String startym,String endym) throws SQLException{
     String qr="",where_ym=" (",where_cum="(", max_ym="(";
     int counter=0;
      ArrayList data = new ArrayList();
      data.clear();

      for(int i=0;i<indicators.size();i++){
            String table = indicators.get(i).toString().split(",")[0];
            String level3 = indicators.get(i).toString().split(",")[1];
            String level4 = indicators.get(i).toString().split(",")[2];
         if(level4!=null && !level4.equals("null") && !level4.equals("")){
                           qr+="SELECT yearmonth,quarter,level3,level4  FROM "+table+" WHERE ("+table+".level3='"+level3+"' && "+table+".level4='"+level4+"') UNION ALL ";
                           counter++;
                        where_cum+=" (Indicator="+level3+" "+level4+") OR ";
                       }
                       else{
                           qr+="SELECT yearmonth,quarter,level3,level4  FROM "+table+" WHERE ("+table+".level3='"+level3+"' && ("+table+".level4 IS NULL OR "+table+".level4=''))  UNION ALL ";
                           counter++;
                         where_cum+=" (Indicator='"+level3+"') OR ";
                       }   
                }
      if(counter>0){
      qr = removeLastChars(qr, 11);
      where_cum = removeLastChars(where_cum, 3)+")";
      
      String final_query = "SELECT MAX(yearmonth),level3,level4,CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS 'Indicator' FROM ("+qr+") AS ymdata GROUP BY Indicator,quarter ORDER BY Indicator,quarter DESC";
          System.out.println("query : max yms for quarters : "+final_query);
      conn.rs2 = conn.st2.executeQuery(final_query);
      String prev_indic="";
      while(conn.rs2.next()){
          if(!prev_indic.equals(conn.rs2.getString("Indicator"))){
              if(conn.rs2.getString("level4")!=null){
              max_ym+=" (yearmonth="+conn.rs2.getString(1)+" && level3='"+conn.rs2.getString("level3")+"' && level4='"+conn.rs2.getString("level4")+"' ) OR ";
              }
              else{
              max_ym+=" (yearmonth="+conn.rs2.getString(1)+" && level3='"+conn.rs2.getString("level3")+"' && level4 IS NULL ) OR ";    
              }
          }
        if(conn.rs2.getString("level4")!=null){   
      where_ym += " (yearmonth="+conn.rs2.getString(1)+" && Indicator='"+conn.rs2.getString("Indicator")+"' ) OR ";
        }
        else{
       where_ym+= " (yearmonth="+conn.rs2.getString(1)+" && Indicator='"+conn.rs2.getString("Indicator")+"' ) OR ";    
        }
      prev_indic = conn.rs2.getString("Indicator");
      }
     
         where_ym = removeLastChars(where_ym, 3)+")";
         max_ym = removeLastChars(max_ym, 3)+")";
  }
      else{
        where_ym = max_ym = where_cum = " 0=1 ";  
      }
          ArrayList dt = new ArrayList();
            dt.clear();

            System.out.println("max ym : "+max_ym);
            dt.add(where_ym);
            dt.add(where_cum);
            dt.add(max_ym);
            
      return dt;
  }  
     public JSONObject get_targets(String indicators,dbConn conn) throws SQLException{
     JSONObject obj_targets = new JSONObject();
          
          int target;
          String indic,county;
          String gettargets = "SELECT SUM(IFNULL(target,0)),CONCAT(IFNULL(level3,''),' ',IFNULL(level4,'')) AS indic,county FROM targets " +
                               " LEFT JOIN achievement_mapping ON targets.indicator = achievement_mapping.facility_target_column "+
                               " WHERE 1=1 "+indicators+""+
                               " GROUP BY county,indic";
          conn.rs1  = conn.st1.executeQuery(gettargets);
         while(conn.rs1.next()){
              target = conn.rs1.getInt(1);
              indic = conn.rs1.getString(2);
              county = conn.rs1.getString(3);
              
              obj_targets.put(county+"_"+indic, target);
          }
     
      return obj_targets;
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
     public JSONObject get_cum_data(String cumulative_quarter_yearmonth,String cumulative_quarter_where,dbConn conn,String query) throws SQLException{
         JSONObject obj = new JSONObject();
         
         String query_cumulatives = "SELECT county,Indicator,"
                +" "
                +" SUM( CASE WHEN quarter='1. Oct - Dec' THEN achieved END) AS q1," +
                "  SUM( CASE WHEN quarter='2. Jan - Mar' THEN achieved END) AS q2," +
                "  SUM( CASE WHEN quarter='3. Apr - Jun' THEN achieved END) AS q3," +
                "  SUM( CASE WHEN quarter='4. Jul - Sep' THEN achieved END) AS q4"
                
                + " FROM ("+query+") AS data WHERE "+cumulative_quarter_yearmonth+"  GROUP BY Indicator,county";  
         System.out.println("cum quer"+query_cumulatives);
         conn.rs2 = conn.st2.executeQuery(query_cumulatives);
         while(conn.rs2.next()){
             JSONObject obj_data = new JSONObject();
             obj_data.put("q1", conn.rs2.getString("q1"));
             obj_data.put("q2", conn.rs2.getString("q2"));
             obj_data.put("q3", conn.rs2.getString("q3"));
             obj_data.put("q4", conn.rs2.getString("q4"));
             
             obj.put(conn.rs2.getString("county")+"_"+conn.rs2.getString("Indicator"), obj_data);
         }
         return obj;
     }
}
