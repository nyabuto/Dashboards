<%-- 
    Document   : register
    Created on : Oct 31, 2018, 11:31:38 AM
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
                        <strong>User Registration</strong>
                      </div>
                          <form action="save_user" method="post" class="form-horizontal">
                         <div class="card-body card-block col-md-9"> 
                          <div class="row form-group"  style="text-align: center;">
                              <label for="file-multiple-input" class=" form-control-label" style="text-align: center;">
                                  <b style="color:red">Note:</b> 
                            Fields Marked <b style="color:red">*</b> are required
                            </label>
                              
                            
                          </div>
                          <div class="row form-group">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Full Name</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-8">
                                <input type="text" name="fullname" id="fullname" minlength="7" maxlength="30" placeholder="Full Name" required="true" class="form-control-sm" style="width: 100%;">
                            </div>
                          </div>
                          
                          <div class="row form-group" id="year_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Phone Number</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-8">
                                <input type="number" name="phone" min="0700000000" id="phone" placeholder="Phone Number e.g. 0720000000"  maxlength="12" required="true" class="form-control-sm" style="width: 100%;">
                            </div>
                          </div>
                          
                          <div class="row form-group" id="semi_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Email Address</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-8">
                                 <input type="email" name="email" id="email" placeholder="Email Address"  maxlength="30" required="true" class="form-control-sm" style="width: 100%;">
                            </div>
                          </div>
                          <div class="row form-group" id="quarter_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Password</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-8">
                                <input type="password" name="password" id="password" placeholder="Password"  maxlength="30" required="true" class="form-control-sm" style="width: 100%;">
                            </div>
                          </div>
                          <div class="row form-group" id="quarter_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Confirm Password</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-8">
                                <input type="password" name="con_password" id="con_password" placeholder=" Confirm your password"  maxlength="30" required="true" class="form-control-sm" style="width: 100%;">
                            </div>
                          </div>
                          
                          <div class="row form-group" id="month_grp">
                            <div class="col col-md-3"><label for="file-multiple-input" class=" form-control-label"><b>Gender</b><b style="color:red">*</b></label></div>
                            <div class="col-12 col-md-8">
                                <select id="gender" name="gender" required="true"  data-placeholder="Choose Gender..." class="form-control" style="height: 32px;">
                                    <option value ="">Choose Gender</option>
                                    <option value ="F">Male</option>
                                    <option value ="M">Female</option>
                                    </select>
                            </div>
                          </div>
                     
                          <div class="card-footer" style="text-align: right;">
                         <button type="submit" class="btn btn-primary">
                          <i class="fa fa-dot-circle-o"></i> Register
                        </button>
                      </div>
                      </div>

              <div class="card-body card-block col-md-3" style="font-size: 20px;">  
               <%
                     if (session.getAttribute("response")!= null)  { 
                        out.print(session.getAttribute("response").toString());
                        session.removeAttribute("response");
                    }

               %>  
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
//     jQuery("#gender").chosen("destroy");
//             jQuery("#gender").chosen({
//                    disable_search_threshold: 10,
//                    no_results_text: "Oops, no gender found!",
//                    width: "100%"
//                });
    });       
    </script>
    
    <script>
          
    </script>

</body>
</html>
