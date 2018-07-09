
import modelos.*;
import servicios.*;

import static java.lang.Math.max;
import static spark.Spark.*;

import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import transformaciones.JsonTransformer;

import java.util.*;
import java.util.Set;





public class ManejoRutas {

    // Declaración para simplificar el uso del motor de template Thymeleaf.
    public static String renderThymeleaf(Map<String, Object> model, String templatePath) {
        return new ThymeleafTemplateEngine().render(new ModelAndView(model, templatePath));
    }


    public void rutas(){

        JsonTransformer jsonTransformerTransformer = new JsonTransformer();

        get("/home", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Usuario usuario = new Usuario();
            Session session = request.session(true);

            int pagina = Integer.parseInt(request.queryParamOrDefault("pagina", "1"));
            int sz = Integer.parseInt(request.queryParamOrDefault("sz", "5"));
            pagina = max(pagina,1);
            List<Articulo> listaArticulos = as.listaArticulos(pagina, sz);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);
            modelo.put("pagina", pagina);
            modelo.put("etiquetas", new EtiquetaServices().getEtiquetas());

            if(request.cookie("usuario") != null){
                UsuarioServices us = new UsuarioServices();
                usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
                session.attribute("usuario", usuario);
            }

            if(session.attribute("usuario") != null) usuario = session.attribute("usuario");

            modelo.put("registeredUser", usuario);

            return renderThymeleaf(modelo,"/home");
        });

        get("/registrar", (request, response) -> {
            Usuario u = new Usuario();
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("usuario", u);
            modelo.put("registeredUser", getLogUser(request));

            return renderThymeleaf(modelo,"/registrar");
        });


        get("/test", (request, response) -> {
            ArticuloServices as = new ArticuloServices();

            int pagina = Integer.parseInt(request.queryParamOrDefault("pagina", "1"));
            int sz = 5;
            pagina = max(pagina,1);
            List<Articulo> listaArticulos = as.listaArticulos(pagina, sz);

            for(Articulo i: listaArticulos){
                i.setComentarios(null);
                i.setEtiquetas(null);
            }

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);

            return modelo;
        }, jsonTransformerTransformer);

        post("/registrar", (request, response) -> {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.queryParams("nombre"));
            usuario.setUsername(request.queryParams("username"));
            usuario.setPassword(request.queryParams("password"));
            try {
                usuario.setAdministrador("on".equalsIgnoreCase(request.queryParams("administrador")));
            }catch (Exception e){
            }
            try {
                usuario.setAutor("on".equalsIgnoreCase(request.queryParams("autor")));
            }catch (Exception e){
            }

            UsuarioServices us = new UsuarioServices();
            us.crearUsuario(usuario);

            response.redirect("/registrar");
            return "";
        });


        get("/login", (request, response)->{
            Map<String, Object> modelo = new HashMap<>();
            return renderThymeleaf(modelo, "/login");
        });

        get("/ver", (request, response)->{
            long id = (long) Integer.parseInt(request.queryParams("id"));
            Usuario usuario = request.session(true).attribute("usuario");
            ArticuloServices as = new ArticuloServices();
            LikeArticuloServices las = new LikeArticuloServices();
            LikeComentarioServices cas = new LikeComentarioServices();
            Articulo articulo = as.getArticulo( id);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("articulo", articulo);

            modelo.put("autor", articulo.getAutor().getNombre());
            modelo.put("etiquetas", articulo.getEtiquetas());
            modelo.put("registeredUser", getLogUser(request));
            modelo.put("likesCount", las.getLikesByArticuloID(id, 1) );
            modelo.put("dislikesCount", las.getLikesByArticuloID(id, 2) );
            if(usuario != null) {
                if(las.getLikesByArticuloYUsuarioID(id, usuario.getId(), 1) + las.getLikesByArticuloYUsuarioID(id, usuario.getId(), 2) > 0)
                    modelo.put("haVotado", true);
                else  modelo.put("haVotado", false);

                if(las.getLikesByArticuloYUsuarioID(id, usuario.getId(), 1) > 0)
                    modelo.put("votoGusta", true);
                else  modelo.put("votoGusta", false);


                System.out.println("legusto?? : ******* " + las.getLikesByArticuloYUsuarioID(id, usuario.getId(), 1));

            }

            Set<Comentario> comentarios = articulo.getComentarios();

            for(Comentario comentario: comentarios){
                comentario.setLikesCount( cas.getLikesByCoementarioID( comentario.getId(), 1  ));
                comentario.setDislikesCount( cas.getLikesByCoementarioID( comentario.getId(), 2  ));
            }

            modelo.put("comentarios", comentarios);



            return renderThymeleaf(modelo, "/articulo");
        });

        /**
         * por si no ponen /home
         */
        get("",  (request, response) -> {
            response.redirect("/home?pagina=1");
            return "";
        });

        get("/",  (request, response) -> {
            response.redirect("/home?pagina=1");
            return "";
        });



        post("/comentar", (request, response) -> {
            ComentarioServices cs = new ComentarioServices();
            Session session = request.session(true);
            int id = Integer.parseInt(request.queryParams("articuloid"));
            int usuarioid = (int)( (Usuario)session.attribute("usuario")).getId();

            cs.crearComentario(request.queryParams("comentario"), (long)usuarioid, (long)id);
            response.redirect("/ver?id="+id);

            return "";
        });

        post("/borrarArticulo", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Session session = request.session(true);
            int articuloid = Integer.parseInt(request.queryParams("articuloid"));
            int usuarioid = (int)( (Usuario)session.attribute("usuario")).getId();
            as.borrarArticulo(articuloid, usuarioid);
            response.redirect("/home");
            return "";
        });

        post("/agregarArticulo", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Session session = request.session(true);

            int usuarioid = (int)( (Usuario)session.attribute("usuario")).getId();

            String titulo = request.queryParams("titulo");
            String cuerpo = request.queryParams("cuerpo");

            String tags = request.queryParams("etiquetas");

            as.crearArticulo(titulo, cuerpo, usuarioid, tags);

            response.redirect("/home");
            return "";
        });

        get("/editarArticulo", (request, response)->{
            int id = Integer.parseInt(request.queryParams("id"));
            ArticuloServices as = new ArticuloServices();
            Articulo articulo = as.getArticulo((long) id);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("registeredUser", getLogUser(request));
            modelo.put("articulo", articulo);
            String etiqueta = "";

            for(Etiqueta et: articulo.getEtiquetas()){
                etiqueta += et.getEtiqueta() + ", ";
            }

            modelo.put("etiqueta", etiqueta);

            return renderThymeleaf(modelo, "/editarArticuloForm");
        });

        post("/editarArticulo", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            String titulo = request.queryParams("titulo");
            String cuerpo = request.queryParams("cuerpo");
            String tags = request.queryParams("etiquetas");
            long id = (long) Integer.parseInt( request.queryParams("id"));

            as.actualizarArticulo(titulo, cuerpo, id, tags);

            response.redirect("/home");
            return "";
        });

        get("/articulosDeEtiqueta", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Usuario usuario = new Usuario();
            Session session = request.session(true);

            int etiqueta = Integer.parseInt(request.queryParamOrDefault("etiqueta", "1"));
            System.out.println(request);
            List<Articulo> listaArticulos =new ArrayList<>(as.listaArticulosByTag((long)etiqueta));
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);
            modelo.put("etiquetas", new EtiquetaServices().getEtiquetas());

            if(request.cookie("usuario") != null){
                UsuarioServices us = new UsuarioServices();
                usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
                session.attribute("usuario", usuario);
            }

            if(session.attribute("usuario") != null) usuario = session.attribute("usuario");

            modelo.put("registeredUser", usuario);
            modelo.put("etiqueta",new EtiquetaServices().getEtiquetaByID((long)etiqueta));

            return renderThymeleaf(modelo,"/home");
        });
    
    //ruta para el conteo de los likes
        post("/articulo/votar", (request, response) -> {
             long articuloid = (long) Integer.parseInt(request.queryParams("articuloid"));
             long usuarioid = (long) Integer.parseInt(request.queryParams("usuarioid"));
            LikeArticuloServices las = new LikeArticuloServices();

            //aqui true se refiere a me gusta
            if (request.queryMap().get("voto").value().equals("true")) {
                las.setLikes(articuloid, usuarioid, 1);
            } else {
                las.setLikes(articuloid, usuarioid, 2);
            }
            response.redirect("/ver?id=" + articuloid);
            return null;
        });


        post("/comentario/votar", (request, response) -> {

            long articuloid = (long) Integer.parseInt(request.queryParams("articuloid"));
            long usuarioid = (long) Integer.parseInt(request.queryParams("usuarioid"));
            long comentarioid = (long) Integer.parseInt(request.queryParams("comentarioid"));
            LikeComentarioServices las = new LikeComentarioServices();

            //aqui true se refiere a me gusta
            if (request.queryMap().get("voto").value().equals("true")) {
                las.setLikes(comentarioid, usuarioid, 1);
            } else {
                las.setLikes(comentarioid, usuarioid, 2);
            }
            response.redirect("/ver?id=" + articuloid);
            return null;
        });


        get("/pagina", (request, response) -> {
            ArticuloServices as = new ArticuloServices();
            Usuario usuario = new Usuario();
            Session session = request.session(true);

            System.out.println(request.body());
            System.out.println(request.queryParams("pagina"));

            int pagina = Integer.parseInt(request.queryParamOrDefault("pagina", "1"));
            int sz = Integer.parseInt(request.queryParamOrDefault("sz", "5"));
            pagina = max(pagina,1);
            List<Articulo> listaArticulos = as.listaArticulos(pagina, sz);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("listaArticulos", listaArticulos);
            modelo.put("pagina", pagina);
            modelo.put("etiquetas", new EtiquetaServices().getEtiquetas());

            if(request.cookie("usuario") != null){
                UsuarioServices us = new UsuarioServices();
                usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
                session.attribute("usuario", usuario);
            }

            if(session.attribute("usuario") != null) usuario = session.attribute("usuario");

            modelo.put("registeredUser", usuario);

            return renderThymeleaf(modelo,"/pagina");
        });






    }

    private static Object procesarParametros(Request request, Response response){
      //  System.out.println("Recibiendo mensaje por el metodo: "+request.requestMethod());
        Set<String> parametros = request.queryParams();
        String salida="";

        for(String param : parametros){
            salida += String.format("Parametro[%s] = %s <br/>", param, request.queryParams(param));
        }

        return salida;
    }

    private Usuario getLogUser(Request request){

            Usuario usuario = new Usuario();
            Session session = request.session(true);

            if(request.cookie("usuario") != null){
                UsuarioServices us = new UsuarioServices();
                usuario = us.getUsuario(Integer.parseInt(request.cookie("usuario")));
                session.attribute("usuario", usuario);
            }

            if(session.attribute("usuario") != null) usuario = session.attribute("usuario");




        return usuario;
    }



}
