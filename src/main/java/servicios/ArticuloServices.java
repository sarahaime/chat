package servicios;

import modelos.Articulo;
import modelos.Etiqueta;
import modelos.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ArticuloServices extends GestionDb<Articulo>{

    public ArticuloServices(){
        super(Articulo.class);
    }

    public List<Articulo> listaArticulos() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select a from Articulo a order by a.id desc");
        List<Articulo> lista = query.getResultList();
        em.close();
        return lista;
    }

    public List<Articulo> listaArticulos(int pagina, int sz) {
        pagina = max(pagina,1);
        EntityManager em = getEntityManager();
        Query queryList = em.createQuery("select a from Articulo a order by a.id desc");
        queryList.setFirstResult((pagina-1)*sz);
        queryList.setMaxResults(sz);
        List<Articulo> lista = queryList.getResultList();
        em.close();
        return lista;
    }

    public Set<Articulo> listaArticulosByTag(long tagID) {
        EntityManager em = getEntityManager();
        Query queryList = em.createQuery("select e from Etiqueta e where e.id =:tagID");
        queryList.setParameter("tagID", tagID);
        Etiqueta et = (Etiqueta) queryList.getSingleResult();
        em.close();
        return et.getArticulos();
    }


    public long getCantidadArticulos(){
        EntityManager em = getEntityManager();
        Query queryCount= em.createQuery("select count(a.id) from Articulo a");
        long cant = (long) queryCount.getSingleResult();
        em.close();
        return cant;
    }


    public Articulo getArticulo(long id) {
        Articulo articulo = find(id);
        articulo.setComentarios( new ComentarioServices().getComentarioByArticuloID(id));
        return articulo;
    }

    public boolean crearArticulo(String titulo, String cuerpo, long usuarioid, String tags){
        Articulo articulo = new Articulo();
        articulo.setAutor( new UsuarioServices().getUsuario(usuarioid) );
        articulo.setTitulo(titulo);
        articulo.setCuerpo(cuerpo);
        articulo.setEtiquetas(generarEtiquetas(tags));
        crear(articulo);
        return true;
    }


    //Actualizar los Articulos.
    public boolean actualizarArticulo(String titulo, String cuerpo, long id, String tags){
        Articulo articulo = find(id);
        articulo.setTitulo(titulo);
        articulo.setCuerpo(cuerpo);
        articulo.setEtiquetas(generarEtiquetas(tags));
        crear(articulo);
        return true;
    }

    //Metodo borrar los Articulos
    public boolean borrarArticulo(long id, long usuarioid){
        eliminar(id);
        return true;
    }

    private Set<Etiqueta>generarEtiquetas(String etiquetas){
        List<String> tagsList = Arrays.asList(etiquetas.split(",[ ]*"));
        Set<Etiqueta> ans = new HashSet<>();

        EtiquetaServices es = new EtiquetaServices();

        for (String tagName : tagsList){
            ans.add(  es.getEtiquetaByName( tagName ) );
        }

        return ans;
    }
}
