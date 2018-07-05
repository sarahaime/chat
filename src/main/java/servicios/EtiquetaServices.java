package servicios;

import modelos.Articulo;
import modelos.Etiqueta;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EtiquetaServices extends GestionDb<Etiqueta>{

    public EtiquetaServices(){
        super(Etiqueta.class);
    }

    public boolean crearEtiqueta(String etiqueta){
        Etiqueta et = new Etiqueta();
        et.setEtiqueta(etiqueta);
        crear( et );
        return true;
    }

    public List<Etiqueta> getEtiquetaByArticuloID(long articuloID){
        Articulo articulo = new ArticuloServices().find(articuloID);
        return new ArrayList<>(articulo.getEtiquetas());
    }

    public boolean agregarEtiquetaArticulo(long etiquetaID, long articuloID){
        Articulo articulo = new ArticuloServices().find(articuloID);
        Etiqueta etiqueta = find(etiquetaID);
        articulo.agregarEtiqueta( etiqueta );
        new ArticuloServices().editar(articulo);

        return true;
    }

     public Etiqueta getEtiquetaByID(long id){
        return find(id);
    }

    //si no existe la etiqueta, la crea y la busca,
    //podria tener problemas de concurrencia pera para este caso es OK

     public Etiqueta getEtiquetaByName(String name){
         Etiqueta etiqueta = null;

         EntityManager em = getEntityManager();
         Query query = em.createQuery("select e from Etiqueta e where e.etiqueta =:name");
         query.setParameter("name", name.trim().toUpperCase());
         List<Etiqueta> lista = query.getResultList();
         em.close();

         if(lista.size() > 0){
             etiqueta = lista.get(0);
         }else{
             etiqueta = new Etiqueta();
             etiqueta.setEtiqueta(name.toUpperCase());
             crear(etiqueta);
             return getEtiquetaByName(name);
         }
        return etiqueta;
    }

    public List<Etiqueta> getEtiquetas( ){
        Etiqueta etiqueta = null;
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Etiqueta e");
        List<Etiqueta> lista = query.getResultList();
        em.close();
        return lista;
    }

}
