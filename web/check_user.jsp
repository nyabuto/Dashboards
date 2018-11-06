<%-- 
    Document   : check_user
    Created on : Nov 5, 2018, 12:55:33 PM
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
    
    <!-- Left Panel -->

    <!-- Right Panel -->

    <div id="right-panel" class="right-panel">

        <!-- Header-->
        <!-- Header-->

        <div class="content mt-3">
            <div class="animated fadeIn">


                <div class="row">

                  <div class="col-lg-12">
                    <div class="card">
                        <div class="card-header" style="text-align: center;">
                        <strong>Pending User Registration Requests</strong>
                      </div>
                         <div class="card-body card-block col-md-12" id="users"> 
                         
                      </div>

              <div class="card-body card-block col-md-3" style="font-size: 20px;">  
               <%
                     if (session.getAttribute("response")!= null)  { 
                        out.print(session.getAttribute("response").toString());
                        session.removeAttribute("response");
                    }

               %>  
                      </div>
                          
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
        load_users();
    }); 
    
          function load_users(){
       jQuery.ajax({
        url:'load_users',
        type:"post",
        dataType:"html",
        success:function(output){
         // ouput
         jQuery("#users").html(output);
        }
    });   

   }
    </script>
    
    <script>
      function approve(pos){
       jQuery.ajax({
        url:'check_user?pos='+pos+"&&state=1",
        type:"post",
        dataType:"html",
        success:function(output){
         // ouput
       //refresh
       load_users();
//       show notification
        }
    }); 
      } 
      
      function decline(pos){
 jQuery.ajax({
        url:'check_user?pos='+pos+"&&state=0",
        type:"post",
        dataType:"html",
        success:function(output){
         // ouput
       //refresh
       load_users();
//       show notification
        }
    }); 
  }
    </script>

</body>
</html>
