<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
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
    <title>Historial</title>

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
          <span class="mdl-layout-title">Historial</span>
          <!-- Add spacer, to align navigation to the right -->
          <div class="mdl-layout-spacer"></div>
          <!-- Navigation. We hide it in small screens. -->
          <nav class="mdl-navigation">

          </nav>
        </div>
      </header>
      <div class="mdl-layout__drawer">
        <span class="mdl-layout-title">Historial</span>
        <nav class="mdl-navigation">
          <a class="mdl-navigation__link" href="deudasDeben.do">Me deben</a>
   		  <a class="mdl-navigation__link" href="deudasDebo.do">Mis deudas</a>
          <a class="mdl-navigation__link" href="resumen.do">Resumen</a>
          <a class="mdl-navigation__link" href="amigos.do">Amigos</a>
          <a class="mdl-navigation__link" href="perfil.do">Perfil</a>
          <a class="mdl-navigation__link" href="cerrarSesion.do">Cerrar sesión</a>          
        </nav>
      </div>
      
      <!-- Contenido-->
      <main class="mdl-layout__content">
        <div class="page-content mdl-grid">
                  
          <c:forEach var="deuda" items="${deudas}">
           <div data-id-deuda="${deuda.id}" class="mdl-cell mdl-cell--12-col">
            <div class="demo-card-wide mdl-card mdl-shadow--2dp">
              <div class="mdl-card__title">
                <h2 class="mdl-card__title-text"><c:if test="${deuda.usuario.id == sessionScope.id}"><c:out value="${fn:trim(deuda.usuarioDestino.nombre)}" /> <c:out value="${fn:trim(deuda.usuarioDestino.apellidos)}" /></c:if><c:if test="${deuda.usuario.id != sessionScope.id}"><c:out value="${fn:trim(deuda.usuario.nombre)}" /> <c:out value="${fn:trim(deuda.usuario.apellidos)}" /></c:if>                
                </h2>
              </div>
              <div class="mdl-card__supporting-text">
                <div><fmt:formatDate pattern="dd/MM/yyyy" value="${deuda.fechaRegistro}" /></div>
                <div><c:out value="${fn:trim(deuda.concepto)}" /></div>
                <div class="card-cantidad"><c:out value="${deuda.cantidad}" />€ <c:if test="${deuda.usuario.id == sessionScope.id}">(Le debía)</c:if><c:if test="${deuda.usuarioDestino.id == sessionScope.id}">(Me debía)</c:if></div>
              </div>
            </div> 
           </div>
          </c:forEach>                                                                 
                        
        </div>
      </main>
    </div>
     
   	<!-- Snackbar -->
	<%@include file="fragJSP/snackError.jspf"%>  
    
  </body>
</html>
