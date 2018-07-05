
"use strict";


var paginaActual = $('#pagina').val();


$('#panterior').on('click', function () {
    paginaActual++;


    cargarArticuloPorPagina(paginaActual);
});

$('#preciente').on('click', function () {
    paginaActual--;
    cargarArticuloPorPagina(paginaActual);
});

function cargarArticuloPorPagina() {
//https://stackoverflow.com/questions/22037062/how-can-we-repeat-the-table-row-in-jquery-like-angularjs-does-with-ng-repeat/22039804

    $('#pagina').val(paginaActual);

    window.location.href = "/home?pagina="+paginaActual;

    console.log(paginaActual);
    // if( paginaActual > total ) {
    //     $('#reciente').prop('disabled', true);
    // }else{
    //     $('#reciente').prop('disabled', false);
    // }

    if( paginaActual < 1 ) {
        //  $('#anterior').prop('disabled', true);
        paginaActual= 1;
    }else{
        //$('#anterior').prop('disabled', false);
    }


}


