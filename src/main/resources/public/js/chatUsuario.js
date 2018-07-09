
$(document).ready(function () {
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/mensajeServidor");
    console.log("asf");
    webSocket.onopen  = function(e){
        var info = get_info(true);
        console.log(info);
        webSocket.send(info);
    };
    $("#btn-chat").click(function(){
        var info = get_info(false);
        webSocket.send(info);
        inyectarHtml(info,true);
        $("#btn-input").val("");
    });

    webSocket.onmessage  = function(msg){
        console.log(msg);
        inyectarHtml(msg.data,false);
    };

    function inyectarHtml(mensaje,self) {
        var info = JSON.parse(mensaje);
        var html = '<div class="row msg_container base_sent">';
        html+=    '<div class="col-md-10 col-xs-10 ">';
        html+=    '<div class="messages msg_sent">';
        if(self){
            html+=    '<p><b>'+ info.usuario_origen +' dice:</b></p><p>' + info.mensaje + '</p>';
        }
        else if (info.usuario_origen === "servidor"){
            html+=    '<p><b><span style="color:red">'+ info.mensaje +' </b></span></p>';
        }
        else
            html+=    '<p><b>'+ info.nombre_origen +' dice:</b></p><p>' + info.mensaje + '</p>';


        html+=     '</div></div></div>';
        var div = document.createElement('div');
        div.innerHTML = html;
        document.getElementById("chat_panel").appendChild(div);

    }
});



function get_info (inicializando) {
    var info = {
        inicializando: inicializando,
        usuario_origen:$("#usuario").val(),
        usuario_destino: $("#usuario_destino").val(),
        mensaje: $('#btn-input').val(),
    };
    return  JSON.stringify(info);
}