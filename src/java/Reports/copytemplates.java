/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Reports;

import static Db.OSValidator.isUnix;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author SIXTYFOURBIT
 */
public class copytemplates extends HttpServlet {

   String host,dbase,user,password; 
  static   String dbsetup,masterdbsetup,tempdb;
 String url,dbconnpath;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      
        
        
       
      
      
//CREATE A PATH IN THE COMPUTER FOR STORING TEXT FILES
                            
    String sourcepath = getServletContext().getRealPath("/achievements.xlsm");
        String mydrive = sourcepath.substring(0, 1);
      dbconnpath=mydrive+":\\HSDSA\\Dashboards"; 
      String destpath=dbconnpath+"\\achievements.xlsm"; 
      if (isUnix()) {
      
       dbconnpath="/HSDSA/Dashboards/";   
       destpath=dbconnpath+"achievements.xlsm";    
          
      }
      
      //create a directory
      
      // new File(dbconnpath).mkdir();
     new File(dbconnpath).mkdirs();
        
        
        


    
        
    //dbsetup=ctx.getRealPath("/dbase.txt");
        
       
        
//try {
// System.out.println("===============================context "+getServletContext().getRealPath("/dbsettings.txt"));

 //dbsetup = getClass().getResource("dbase.txt").getFile();
      

  // } catch (IOException e) {}
   
   
    Path FROM = Paths.get(sourcepath);
    Path TO = Paths.get(destpath);
    //overwrite existing file, if exists
    CopyOption[] options = new CopyOption[]{
      StandardCopyOption.REPLACE_EXISTING,
      StandardCopyOption.COPY_ATTRIBUTES
    }; 
    Files.copy(FROM, TO, options);
    
    
  
    //overwrite existing file, if exists
    CopyOption[] options1 = new CopyOption[]{
      StandardCopyOption.REPLACE_EXISTING,
      StandardCopyOption.COPY_ATTRIBUTES
    }; 
    
    
    
   
//   System.out.println("Number of lines:========="+getLineCount(dbsetup));
   
   
     
        
        
        
        

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
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

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


 
 //=======================================================================
public void transfermacros(String src1,String desteepath ){
        try {
            
            //the source of the application is known and is fixed.
            //however, the destination may change and is different.
            
                  String mydrive = desteepath.substring(0, 1);
                  //dbconnpath=mydrive+":\\MNHC_SYSTEM_APHIA_PLUS\\"; 
                dbconnpath=mydrive+":\\HSDSA\\Dashboards\\"; 
                 
                 if (isUnix()) {
      
       dbconnpath="/HSDSA/Dashboards/";   
      
          
      }
                
                
                //create a directory
                // new File(dbconnpath).mkdir();
               new File(dbconnpath).mkdirs();
                  
          /*        
          String sourcepath=dbconnpath+"\\Partnerbasedkepms.xlsm";
          String sourcepath1=dbconnpath+"\\Females 15to24.xlsm";
            */  
                  
              //dbsetup=ctx.getRealPath("/dbase.txt");
                  
      
             
              Path FROM = Paths.get(src1);
              Path TO = Paths.get(desteepath);
              //overwrite existing file, if exists
              CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
              }; 
              Files.copy(FROM, TO, options);
              
         /**     
              Path FROM1 = Paths.get(sourcepath1);
              Path TO1 = Paths.get(desteepath1);
              //overwrite existing file, if exists
              CopyOption[] options1 = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
              }; 
              
               Files.copy(FROM1, TO1, options1); */
        }
        //=======================================================================
        catch (IOException ex) {
            Logger.getLogger(copytemplates.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
   



}
 
    
 
    
 //=======================================================================
  
 
    
public void copymacros(String sourcepath,String destpath ){
        try {
           
            System.out.println("~~~Passed dest path is "+destpath);
            
               
             
              Path FROM = Paths.get(sourcepath);
              Path TO = Paths.get(destpath);
              //overwrite existing file, if exists
              CopyOption[] options = new CopyOption[]{
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
              }; 
              Files.copy(FROM, TO, options);
              
              
            
        }
        //=======================================================================
        catch (IOException ex) {
            System.out.println("Error while tranferring"+ex);
            Logger.getLogger(copytemplates.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
   



}

 
 
 
 
}

