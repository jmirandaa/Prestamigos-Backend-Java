<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!doctype html>
<!--
  Material Design Lite
  Copyright 2015 Google Inc. All rights reserved.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License
-->
<html lang="en">
  <head>
    <meta name="description" content="A front-end template that helps you build fast, modern mobile web apps.">
    <title>Nueva deuda</title>

	<!-- CSS y JS -->
	<%@include file="fragJSP/estilos.jspf"%>
    <%@include file="fragJSP/javascript.jspf"%> 
        
    <style>
    #view-source {
      position: fixed;
      display: block;
      right: 0;
      bottom: 0;
      margin-right: 40px;
      margin-bottom: 40px;
      z-index: 900;
    }
    </style>
  </head>
  <body>
      
    <!-- Navegación -->
    <div class="mdl-layout mdl-js-layout mdl-layout--fixed-header">
      <header class="mdl-layout__header">
           <div id="buttonBack" aria-expanded="false" role="button" tabindex="0" class="mdl-layout__drawer-button"><i class="material-icons">keyboard_backspace</i></div>
        <div class="mdl-layout__header-row">
          <span class="mdl-layout-title">Nueva deuda</span>
          <!-- Add spacer, to align navigation to the right -->
          <div class="mdl-layout-spacer"></div>
          <nav>
            <label class="mdl-button mdl-js-button mdl-button--icon">
                <i id="nuevaDeudaAccion" class="material-icons">send</i>
            </label>
          </nav>          
        </div>
      </header>

      
      <!-- Contenido-->
      <main class="mdl-layout__content">
        <div class="page-content mdl-grid">
        
            <!-- Tarjeta 1 -->
           <div class="mdl-cell mdl-cell--12-col">
            <div class="demo-card-wide mdl-card mdl-shadow--2dp">
              <div class="mdl-card__supporting-text">
                <!-- Formulario nueva deuda-->
                <form id="formNuevaDeuda" action="nuevaDeudaAccion.do" method="POST">
                 <input type="hidden" name="tipo" value="${requestScope.tipo}"> 
                 <div class="mdl-grid">
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input required minlength=3 class="mdl-textfield__input" type="text" name="campo_amigo" id="campo_amigo">
                    <label class="mdl-textfield__label" for="campo_amigo">Nombre del amigo</label>
                  </div>
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input required minlength=3 class="mdl-textfield__input" type="text" name="campo_concepto" id="campo_concepto">
                    <label class="mdl-textfield__label" for="campo_concepto">Concepto</label>
                  </div>
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input required min="0" class="mdl-textfield__input" type="number" name="campo_cantidad" id="campo_cantidad">
                    <label class="mdl-textfield__label" for="campo_cantidad">Cantidad</label>
                    <input type="submit" value="" id="submitButton" />
                  </div>                                             
                </div>    
                </form> 
              </div>
  
            </div> 
          </div>
       </div>
        
      <script>
      $(document).ready(function() {
    	  $("#submitButton").hide();
      });
      
      $("#nuevaDeudaAccion").click(function(event) {
    	  var nombreAmigo = $("#campo_amigo").val();
    	  var concepto = $("#campo_concepto").val();
    	  var cantidad = $("#campo_cantidad").val();
    	  
    	  if ((nombreAmigo == "") || (concepto == "") || (cantidad == ""))
    	  {
    		  mostrarError("Los campos no pueden estar vacíos");  
    	  }
    	  else
    	  {
    	  	$("#submitButton").click();
    	  }
       });   
      
      $("#buttonBack").click(function(event) {
    	  javascript: history.go(-1);
         });        
      </script>    

      </main>
    </div>
    
   	<!-- Snackbar -->
	<%@include file="fragJSP/snackError.jspf"%>    
    
  </body>
</html>
