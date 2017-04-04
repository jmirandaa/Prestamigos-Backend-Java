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
    <title>Detalles deuda</title>

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
          <span class="mdl-layout-title">Histórico de la deuda</span>
          <!-- Add spacer, to align navigation to the right -->
          <div class="mdl-layout-spacer"></div>
          <nav class="mdl-navigation">
          </nav>          
        </div>
      </header>

      
      <!-- Contenido-->
      <main class="mdl-layout__content">
        <div class="page-content mdl-grid">
        
            <!-- Deuda -->
           <div class="mdl-cell mdl-cell--12-col">
            <div class="demo-card-wide mdl-card mdl-shadow--2dp">
              <div class="mdl-card__title">
                <h2 class="mdl-card__title-text"><c:if test="${deuda.usuario.id == sessionScope.id}"><c:out value="${fn:trim(deuda.usuarioDestino.nombre)}" /> <c:out value="${fn:trim(deuda.usuarioDestino.apellidos)}" /></c:if><c:if test="${deuda.usuario.id != sessionScope.id}"><c:out value="${fn:trim(deuda.usuario.nombre)}" /> <c:out value="${fn:trim(deuda.usuario.apellidos)}" /></c:if></h2>
              </div>
              <div style="line-height: 3px;padding-left:0;" class="mdl-card__supporting-text">
                <div class="mdl-grid">
                  <div class="mdl-cell mdl-cell--12-col"><fmt:formatDate pattern="dd/MM/yyyy" value="${deuda.fechaRegistro}" /></div>
                  <div class="mdl-cell mdl-cell--12-col"><c:out value="${fn:trim(deuda.concepto)}" /></div>
                  <div class="mdl-cell mdl-cell--6-col mdl-cell--6-col mdl-cell--2-col-phone">
                    <div>Total</div>
                  </div>
                  <div class="mdl-cell mdl-cell--6-col mdl-cell--6-col mdl-cell--2-col-phone">
                    <div class="card-cantidad-detalle"><c:out value="${deuda.cantidad}" />€</div>
                  </div>
                  <div class="mdl-cell mdl-cell--6-col mdl-cell--6-col mdl-cell--2-col-phone">
                    <div>Pagado</div>
                  </div>
                  <div class="mdl-cell mdl-cell--6-col mdl-cell--6-col mdl-cell--2-col-phone">
                    <div class="card-cantidad-detalle"><c:out value="${deuda.saldado}" />€</div>
                  </div>                                    
                </div>
              </div>
            </div> 
          </div>

		 <c:forEach var="operacion" items="${operaciones}">
          <!-- Operacion 1 -->
           <div class="mdl-cell mdl-cell--12-col">
            <div style="min-height: 0px;" class="demo-card-wide mdl-card mdl-shadow--2dp">
              <div class="mdl-card__title">
                <h2 class="mdl-card__title-text">Pagado</h2>
              </div>
              <div class="mdl-card__supporting-text">
                <div><fmt:formatDate pattern="dd/MM/yyyy" value="${operacion.fechaRegistro}" /></div>
                <div class="card-cantidad">${operacion.cantidad}€</div>
              </div>
            </div> 
          </div> 
         </c:forEach>  
                   
       </div>
        
      <script>
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
