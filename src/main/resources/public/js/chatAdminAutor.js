
$(document).ready(function () {
    console.log("asdff");
    var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/mensajeServidor");

    webSocket.onopen  = function(e){
        var info = get_info(true);
        console.log(info.usuario_origen);
        webSocket.send(JSON.stringify(info));
    };

    webSocket.onmessage  = function(msg){
        var data = JSON.parse(msg.data);
        console.log('chat_window_'+msg.usuario_origen);
        if(document.getElementById('chat_window_'+data.usuario_origen)==null){
            inyectarChat(data);
        }

        inyectarHtml(data);
    };

    webSocket.onclose = function (msg) {

    }

    function inyectarHtml(mensaje) {
        var html = '<div class="row msg_container base_sent">';
        html+=    '<div class="col-md-10 col-xs-10 ">';
        html+=    '<div class="messages msg_sent">';
        html+=    '<p><b>'+ mensaje.usuario_origen +' dice:</b></p><p>' + mensaje.mensaje + '</p>';
        html+=     '</div> </div> </div>';
        var div = document.createElement('div');
        div.innerHTML = html;
        document.getElementById("chat_panel_"+mensaje.usuario_origen).appendChild(div);

    }

    function inyectarSelf(mensaje) {
        var html = '<div class="row msg_container base_sent">';
        html+=    '<div class="col-md-10 col-xs-10 ">';
        html+=    '<div class="messages msg_sent">';
        html+=    '<p><b>'+ mensaje.nombre_origen +' dice:</b></p><p>' + mensaje.mensaje + '</p>';
        html+=     '</div> </div> </div>';
        var div = document.createElement('div');
        div.innerHTML = html;
        document.getElementById("chat_panel_"+mensaje.usuario_destino).appendChild(div);

    }



    function inyectarChat(mensaje){
        var html = '<div class="row  col-xs-11 col-md-11" id="chat_window_'+mensaje.usuario_origen+'" style="margin-top:20px;margin-left:5%" >'
            +'<div class="col-xs-12 col-md-12">'
            +'<div class=" panel panel-default">'
            +'<div class="panel-heading top-bar">'
            +'<div class="col-md-8 col-xs-8">'
            +'<h3 class="panel-title"><span class="glyphicon glyphicon-comment"></span> '+mensaje.usuario_origen+'</h3>'
            +'</div>'
            +'<div class="col-md-4 col-xs-4" style="text-align: right;"> <a href="#"><span class="glyphicon glyphicon-remove icon_close" data-id="chat_window_'+mensaje.usuario_origen+'"></span></a> </div></div>'
            +'<div class="panel-body  msg_container_base">'
            +'<div id="chat_panel_'+mensaje.usuario_origen+'"></div>'
            +'</div></div>'
            +'<div class="panel-footer">'
            +'<div class="input-group">'
            +'<input id="btn-input-'+mensaje.usuario_origen+'" type="text" class="form-control input-sm chat_input" placeholder="Escriba su mensaje..." />'
            +'<span class="input-group-btn">'
            +'<button class="btn btn-primary btn-sm" id="send-msg-'+mensaje.usuario_origen+'" value="'+mensaje.usuario_origen+'">Enviar</button>'
            +'</span></div></div></div></div>';

        var div = document.createElement('div');
        div.innerHTML = html;
        document.getElementById("body").appendChild(div);
        $(".msg_container_base").mCustomScrollbar();
        $("#send-msg-"+mensaje.usuario_origen).click(function(){
            var usuario_destino = $(this).attr("value")
            var info = {
                inicializando: false,
                usuario_origen:$("#usuario").val(),
                usuario_destino: usuario_destino,
                mensaje: $("#btn-input-"+usuario_destino).val(),
                nombre_origen: $("#nombre").val()

            };
            console.log(info);
            webSocket.send(JSON.stringify(info));
            inyectarSelf(info);
            $("#btn-input-"+usuario_destino).val("");
        });
    }

});

function get_info (inicializando) {
    var info = {
        inicializando: inicializando,
        usuario_origen:$("#usuario").val(),
        usuario_destino: $("#usuario_destino").val(),
        mensaje: "asfdasdfa"
    };
    return info
}