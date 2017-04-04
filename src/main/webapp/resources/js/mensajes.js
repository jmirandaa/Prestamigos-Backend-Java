
/**
 * Mostrar mensaje de error de Snackbar
 * SÓLO FUNCIONA SI EL SNACKBAR ESTÁ IMPORTADO DE snackError.jspf
 * @param mensaje
 */
function mostrarError(mensaje)
{
	(function() {
		  'use strict';
		  var snackbarContainer = document.querySelector('#snackbar-container');
		  var handler = function(event) {
			  };	  
		  var data = {
		  	      message: mensaje,
		  	      timeout: 10000
		  	    };
		  snackbarContainer.MaterialSnackbar.showSnackbar(data);	  
		}());
}