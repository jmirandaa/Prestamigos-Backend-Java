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
    <title>Amigos</title>

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
          <span class="mdl-layout-title">Amigos</span>
          <!-- Add spacer, to align navigation to the right -->
          <div class="mdl-layout-spacer"></div>
          <!-- Navigation. We hide it in small screens. -->
          <nav class="mdl-navigation">
            <label class="mdl-button mdl-js-button mdl-button--icon">
                <i id="nuevoAmigo" class="material-icons">add</i>
            </label>
          </nav>
        </div>
      </header>
      <div class="mdl-layout__drawer">
        <span class="mdl-layout-title">Amigos</span>
        <nav class="mdl-navigation">
          <a class="mdl-navigation__link" href="deudasDeben.do">Me deben</a>
          <a class="mdl-navigation__link" href="deudasDebo.do">Mis deudas</a>
          <a class="mdl-navigation__link" href="resumen.do">Resumen</a>
          <a class="mdl-navigation__link" href="historial.do">Historial</a>
          <a class="mdl-navigation__link" href="perfil.do">Perfil</a>
          <a class="mdl-navigation__link" href="cerrarSesion.do">Cerrar sesión</a>
        </nav>
      </div>
      
      <!-- Contenido-->
      <main class="mdl-layout__content">
        <div class="page-content mdl-grid">
        
		<ul class="list-wide mdl-list">
		  <c:forEach var="amigo" items="${amigos}">
		  <li data-id-amigo="${amigo.id}" class="mdl-list__item">
		    <span class="mdl-list__item-primary-content">
		      <i class="material-icons  mdl-list__item-avatar">person</i>
		      ${amigo.nombre} ${amigo.apellidos}
		    </span>
		  </li>
		  </c:forEach>
		</ul>

                
                                  
        </div>
      </main>
    </div>
    
      <script>
          //Acción nueva deuda
          $("#nuevoAmigo").click(function(event) {
            console.log("nuevo amigo");
          });
          
          //Detalles amigo
          $(".mdl-list__item").click(function(event) {
            	var idAmigo = $(this).attr("data-id-amigo");
              	window.location = "detallesAmigo.do?idAmigo="+idAmigo;        	  
          });
      </script>    
      
   	<!-- Snackbar -->
	<%@include file="fragJSP/snackError.jspf"%> 
    
  </body>
</html>
