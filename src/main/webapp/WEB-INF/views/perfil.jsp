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
    <title>Perfil</title>

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
        <div class="mdl-layout__header-row">
          <span class="mdl-layout-title">Perfil</span>
          <!-- Add spacer, to align navigation to the right -->
          <div class="mdl-layout-spacer"></div>
          <nav class="mdl-navigation">
            <label class="mdl-button mdl-js-button mdl-button--icon">
                <i id="editarAccion" class="material-icons">done</i>
            </label>
          </nav>          
        </div>
      </header>
      <div class="mdl-layout__drawer">
        <span class="mdl-layout-title">Perfil</span>
        <nav class="mdl-navigation">
          <a class="mdl-navigation__link" href="deudasDeben.do">Me deben</a>
          <a class="mdl-navigation__link" href="deudasDebo.do">Mis deudas</a>
          <a class="mdl-navigation__link" href="resumen.do">Resumen</a>
          <a class="mdl-navigation__link" href="historial.do">Historial</a>
          <a class="mdl-navigation__link" href="amigos.do">Amigos</a>
          <a class="mdl-navigation__link" href="cerrarSesion.do">Cerrar sesión</a>          
        </nav>
      </div>
      
      <!-- Contenido-->
      <main class="mdl-layout__content">
        <div class="page-content mdl-grid">
        
            <!-- Tarjeta 1 -->
           <div class="mdl-cell mdl-cell--12-col">
            <div class="demo-card-wide mdl-card mdl-shadow--2dp">
              <div class="mdl-card__supporting-text">
                <!-- Formulario nueva deuda-->
                <form id="formPerfil" action="editarPerfil.do" method="POST">
                 <input type="hidden" name="idUsuario" value="${sessionScope.id}">
                 <div class="mdl-grid">
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input required minlength=3 class="mdl-textfield__input" type="text" name="campo_nombre" id="campo_nombre" value="${usuario.nombre}">
                    <label class="mdl-textfield__label" for="campo_nombre">Nombre</label>
                  </div>
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input required minlength=3 class="mdl-textfield__input" type="text" name="campo_apellidos" id="campo_apellidos" value="${usuario.apellidos}">
                    <label class="mdl-textfield__label" for="campo_apellidos">Apellidos</label>
                  </div>
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input disabled class="mdl-textfield__input" type="email" id="campo_email" value="${usuario.email}">
                    <label class="mdl-textfield__label" for="campo_email">Correo electrónico</label>
                  </div> 
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input required minlength=3 class="mdl-textfield__input" type="password" name="campo_password" id="campo_password" value="${sessionScope.password}">
                    <label class="mdl-textfield__label" for="campo_password">Contraseña</label>
                  </div>
                  <div class="mdl-cell mdl-cell--12-col mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input minlength=3 required class="mdl-textfield__input" type="password" id="campo_password_c" value="${sessionScope.password}">
                    <label class="mdl-textfield__label" for="campo_passowrd_c">Repetir contraseña</label>
                    <input type="submit" value="" id="submitButton" />
                  </div>                                                                                                      
                </div>    
                </form> 
              </div>
  
            </div> 
          </div>
       </div>   
                             
      </main>
    </div>
    
    
    <script>
    	$(document).ready(function() {
  	  		$("#submitButton").hide();
    	});
    	//Enviar formulario
    	$("#editarAccion").click(function(event)
    	{
    		$("#submitButton").click();
    	});
    </script>
   	<!-- Snackbar -->
	<%@include file="fragJSP/snackError.jspf"%>   
    
  </body>
</html>
