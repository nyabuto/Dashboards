<%-- 
    Document   : ExcelReport
    Created on : Jun 19, 2018, 9:39:46 AM
    Author     : GNyabuto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Quarterly Achievements Report and Pivot</title>
    <meta name="description" content="Sufee Admin - HTML5 Admin Template">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="apple-touch-icon" href="images/logo.png">
    <link rel="shortcut icon" href="images/logo.png">

    <link rel="stylesheet" href="assets/css/normalize.css">
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/themify-icons.css">
    <link rel="stylesheet" href="assets/css/flag-icon.min.css">
    <link rel="stylesheet" href="assets/css/cs-skin-elastic.css">
    <link rel="stylesheet" href="assets/scss/style.css">
<link rel="stylesheet" href="assets/css/lib/chosen/chosen.min.css">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,600,700,800' rel='stylesheet' type='text/css'>

</head>
<body>
        <!-- Left Panel -->
    <%@include file="sidebar.jsp" %>

    <!-- Left Panel -->

    <!-- Right Panel -->

    <div id="right-panel" class="right-panel">

        <!-- Header-->
       <%@include file="header.jsp" %>
        <!-- Header-->

        <div class="content mt-3">
            <div class="animated fadeIn">


                <div class="row">

                  <div class="col-lg-12">
                    <div class="card">
                      <div class="card-header">
                        <strong>Achievements Report & Pivot</strong>
                      </div>
                      
                          <form action="Achievements" method="post" class="form-horizontal">
                         <div class="card-body card-block"> 
                          <div class="row form-group"  style="text-align: center;">
                              <label for="file-multiple-input" class=" form-control-label" style="text-align: center;">
                                  <b style="color:red">Note:</b> 
                            Fields Marked <b style="color:red">*</b> are required
                            </label>
                            
                          </div>
                          <div class="row form-group">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Choose Period</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-6">
                                
                                <select id="period" name="period"  data-placeholder="Choose Year..." class="standardSelect form-control" required="true" style="height: 15px;">
                                    <option value ="">Choose Period</option>
                                    <option value ="1">Annual</option>
                                    <option value ="2">Semi - Annual</option>
                                    <option value ="3">Quarterly</option>
                                    <option value ="4">Monthly</option>
                                    </select>
                            </div>
                          </div>
                          
                          <div class="row form-group" id="year_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Select Year</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-6">
                                
                                <select id="year" name="year"  data-placeholder="Choose Year..." required class="standardSelect form-control" style="height: 15px;">
                                    <option value =""> Choose Reporting Year</option>
                                    </select>
                            </div>
                          </div>
                          
                          <div class="row form-group" id="semi_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Select Semi Annual</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-6">
                                
                                <select id="semi_annual" name="semi_annual"  data-placeholder="Choose semi annual..." class="standardSelect form-control" style="height: 15px;">
                                    <option value =""> Choose Reporting Semi Annual</option>
                                    </select>
                            </div>
                          </div>
                          
                          <div class="row form-group" id="quarter_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Select Quarter</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-6">
                                
                                <select id="quarter" name="quarter"  data-placeholder="Choose quarter..." class="standardSelect form-control" style="height: 15px;">
                                    <option value =""> Choose Reporting Quarter</option>
                                    </select>
                            </div>
                          </div>
                          
                          <div class="row form-group" id="month_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Select Month</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-6">
                                
                                <select id="month" name="month"  data-placeholder="Choose month..." class="standardSelect form-control" style="height: 15px;">
                                    <option value =""> Choose Reporting Month</option>
                                    </select>
                            </div>
                          </div>
                          
                    
                      </div>
                        <div class="card-footer" style="text-align: right;">
                        
                            <button type="submit" class="btn btn-primary">
                          <i class="fa fa-dot-circle-o"></i> Generate Report
                        </button>
                      </div>
                          </form>
                    </div>
                  </div>


                </div>


            </div><!-- .animated -->
        </div><!-- .content -->


    </div><!-- /#right-panel -->

    <!-- Right Panel -->


    <script src="assets/js/vendor/jquery-2.1.4.min.js"></script>
    <script src="assets/js/popper.min.js"></script>
    <script src="assets/js/plugins.js"></script>
    <script src="assets/js/main.js"></script>
 <script src="assets/js/lib/chosen/chosen.jquery.min.js"></script>

    <script>
        jQuery(document).ready(function() {
            jQuery("#year_grp").hide();
            jQuery("#semi_grp").hide();
            jQuery("#quarter_grp").hide();
            jQuery("#month_grp").hide();
            
            jQuery("#period").change(function(){
             jQuery("#year_grp").show(); 
             var period_id = jQuery("#period").val();
             if(period_id==1){
               jQuery("#quarter_grp").hide();
                jQuery("#month_grp").hide();
                 jQuery("#semi_grp").hide();
             }
             else if(period_id==2){
              jQuery("#semi_grp").show(); 
              jQuery("#quarter_grp").hide();
               jQuery("#month_grp").hide();
               load_semis();
             }
             else if(period_id==3){
             jQuery("#semi_grp").hide();
             jQuery("#quarter_grp").show(); 
              jQuery("#month_grp").hide();
              load_quarters();
             }
             else if(period_id==4){
                  jQuery("#semi_grp").hide();
                  jQuery("#quarter_grp").hide();
                  jQuery("#month_grp").show();
                  load_months();
             }
             else{
                  jQuery("#year_grp").hide(); 
             }
            });
            
            jQuery("#year").change(function(){
               load_semis();
               load_quarters();
               load_months();
            });
            
            
            
            
            
            jQuery(".standardSelect").chosen({
                disable_search_threshold: 10,
                no_results_text: "Oops, nothing found!",
                width: "100%"
            });
            
            
        });
    </script>
    
    <script>
        jQuery(document).ready(function() {
         load_years();
        });
       function load_years(){
       jQuery.ajax({
        url:'load_years',
        type:"post",
        dataType:"json",
        success:function(raw_data){
         var year,output="";
         var data = raw_data.data;
          year="";
             for (var i=0; i<data.length;i++){
            if( data[i].year!=null){year = data[i].year;}
            output+="<option value='"+year+"'>"+year+"</option>"; 
         }
         // ouput
         jQuery("#year").html(output);
         jQuery("#year").chosen("destroy");
         jQuery("#year").chosen({
                disable_search_threshold: 10,
                no_results_text: "Oops, no year found!",
                width: "100%"
            });
        }
  });   
        
 }  
       function load_semis(){
        var year = jQuery("#year").val();
       jQuery.ajax({
        url:'load_semi_annual?year='+year,
        type:"post",
        dataType:"json",
        success:function(raw_data){
         var id,name,output="";
         var data = raw_data.data;
             for (var i=0; i<data.length;i++){
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].name!=null){name = data[i].name;}
            output+="<option value='"+id+"'>"+name+"</option>"; 
         }
         // ouput
         jQuery("#semi_annual").html(output);
         jQuery("#semi_annual").chosen("destroy");
         jQuery("#semi_annual").chosen({
                disable_search_threshold: 10,
                no_results_text: "Oops, no semi-annual found!",
                width: "100%"
            });
        }
  });   
        
 }  
       function load_quarters(){
        var year = jQuery("#year").val();
       jQuery.ajax({
        url:'load_quarters?year='+year,
        type:"post",
        dataType:"json",
        success:function(raw_data){
         var id,name,output="";
         var data = raw_data.data;
             for (var i=0; i<data.length;i++){
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].name!=null){name = data[i].name;}
            output+="<option value='"+id+"'>"+name+"</option>"; 
         }
         // ouput
         jQuery("#quarter").html(output);
         jQuery("#quarter").chosen("destroy");
         jQuery("#quarter").chosen({
                disable_search_threshold: 10,
                no_results_text: "Oops, no semi-annual found!",
                width: "100%"
            });
        }
  });   
        
 }  
       function load_months(){
       var year = jQuery("#year").val();
       jQuery.ajax({
        url:'load_months?year='+year,
        type:"post",
        dataType:"json",
        success:function(raw_data){
         var id,name,output="";
         var data = raw_data.data;
             for (var i=0; i<data.length;i++){
            if( data[i].id!=null){id = data[i].id;}
            if( data[i].name!=null){name = data[i].name;}
            output+="<option value='"+id+"'>"+name+"</option>"; 
         }
         // ouput
         jQuery("#month").html(output);
         jQuery("#month").chosen("destroy");
         jQuery("#month").chosen({
                disable_search_threshold: 10,
                no_results_text: "Oops, no month found!",
                width: "100%"
            });
        }
  });   
  
 }     
    </script>

</body>
</html>
