<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
    <title>Login</title>

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
    <div class="demo-layout mdl-layout mdl-layout--fixed-header mdl-js-layout mdl-color--grey-100">
      <header class="demo-header mdl-layout__header mdl-layout__header--scroll mdl-color--grey-100 mdl-color-text--grey-800">
        <div class="mdl-layout__header-row">
          <span class="mdl-layout-title">Login</span>
          <div class="mdl-layout-spacer"></div>
        </div>
      </header>
      <div class="demo-ribbon"></div>
      <main class="demo-main mdl-layout__content">
        <div class="demo-container mdl-grid">
          <div class="mdl-cell mdl-cell--2-col mdl-cell--hide-tablet mdl-cell--hide-phone"></div>
          <div class="demo-content mdl-color--white mdl-shadow--4dp content mdl-color-text--grey-800 mdl-cell mdl-cell--8-col">

            <!-- Formulario login -->
            <form id="formLogin" action="login.do" method="POST">
             <div class="mdl-grid">
              <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input required class="mdl-textfield__input" type="email" name="campo_email" id="campo_email">
                <label class="mdl-textfield__label" for="campo_email">Correo electrónico</label>
              </div>
              <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                <input required class="mdl-textfield__input" type="password" name="campo_password" id="campo_password">
                <label class="mdl-textfield__label" for="campo_password">Contraseña</label>
              </div>
              <div class="mdl-cell mdl-cell--6-col">
                <input id="submitButton" type="submit" class="mdl-button mdl-js-button mdl-button--accent" value="Acceder">
              </div>
              <div class="mdl-cell mdl-cell--6-col">
                <button id="botonRegistro" class="mdl-button mdl-js-button mdl-button--accent">Registrarse</button>
              </div>
              <div class="mdl-cell mdl-cell--12-col">
              	<a href="restablecer.do">He olvidado mi contraseña</a>
              </div>                           
            </div>    
            </form>
            
          <!-- Snackbar -->
		  <%@include file="fragJSP/snackError.jspf"%>                            

          </div>
        </div>
        <footer class="demo-footer mdl-mini-footer">
          <div class="mdl-mini-footer--left-section">
            <ul class="mdl-mini-footer--link-list">
              <li><a href="#">Help</a></li>
              <li><a href="#">Privacy and Terms</a></li>
              <li><a href="#">User Agreement</a></li>
            </ul>
          </div>
        </footer>
      </main>
    </div>
       
       <script>
        $("#botonRegistro").click(function(event) {
        	$("#formLogin").attr("action", "registro.do");
        	$("#submitButton").click();
        });        
       </script>
  </body>
</html>
    
    