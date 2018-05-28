/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Db;

import static Db.dbConn.issetdbcalled_exception;
import static Db.dbConn.issetdbcalled_file_exists;
import static Db.dbConn.issetdbcalled_wrongpword;
import com.mysql.jdbc.CallableStatement;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GNyabuto
 */
public final class dbConnFetch {
    
    public ResultSet rs0,rs, rs1, rs2, rs3, rs4, rs_1, rs_2, rs_3, rs_4, rs_5, rs_6, anc_sch_rs;
    public Statement st0,st, st1, st2, st3, st4, st_1, st_2, st_3, st_4, st_5, st_6, anc_scheduling_st;
  public  PreparedStatement pst,pst1,pst2,pst3,pst4,pst5;
  public  PreparedStatement prest,prest1,prest2,prest3,prest4,prest5;
  public  CallableStatement csmt,csmt1,csmt2,csmt3,csmt4;
    String mydrive = "";
    public static int issetdbcalled_file_exists = 2;
    public static int issetdbcalled_exception = 2;
    public static int issetdbcalled_wrongpword = 2;
   public  String dbsetup[] = new String[4];
public  Connection conn = null;

    public dbConnFetch() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
             conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dashboards","root", "admin");
           
            //if the saved host name is less than 2 letters long, then thats not a genuine host name

            URL location = dbConn.class.getProtectionDomain().getCodeSource().getLocation();


            mydrive = location.getFile().substring(1, 2);


                //initialize this three values
                issetdbcalled_exception = 2;
                issetdbcalled_file_exists = 2;
                issetdbcalled_wrongpword = 2;

                st0 = conn.createStatement();
                st = conn.createStatement();
                st1 = conn.createStatement();
                st2 = conn.createStatement();
                st3 = conn.createStatement();
                st4 = conn.createStatement();


                st_1 = conn.createStatement();
                st_2 = conn.createStatement();
                st_3 = conn.createStatement();
                st_4 = conn.createStatement();
                st_5 = conn.createStatement();
                st_6 = conn.createStatement();
                anc_scheduling_st = conn.createStatement();



        } catch (Exception ex) {
            Logger.getLogger(dbConn.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR WHILE CONNECTING TO DATABASE. CHECK YOUR CONNECTION CREDENTIALS SETTINGS in dbConn.java");

        }
    }
}
