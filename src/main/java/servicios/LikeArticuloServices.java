package servicios;

import modelos.Articulo;
import modelos.Etiqueta;
import modelos.LikeArticulo;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.max;

public class LikeArticuloServices extends GestionDb<LikeArticulo>{

    private static LikeArticuloServices instancia;

    public LikeArticuloServices(){
        super(LikeArticulo.class);
    }

    public static LikeArticuloServices getInstancia() {
        if (instancia == null) {
            instancia = new LikeArticuloServices();
        }
        return instancia;
    }


    //para like val = 1, para dislike val = 2
    public long getLikesByArticuloID(long articuloID, int val){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select count(*) from LikeArticulo l where l.articuloId =:articuloID and l.val =:val");
        query.setParameter("articuloID", articuloID);
        query.setParameter("val", val);

        return (long) query.getSingleResult();
    }

    //para like val = 1, para dislike val = 2
    public int getLikesByArticuloYUsuarioID(long articuloID, long usuarioID, int val){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikeArticulo l where l.articuloId =:articuloID and l.val =:val and l.usuarioId =:usuarioID");
        query.setParameter("articuloID", articuloID);
        query.setParameter("val", val);
        query.setParameter("usuarioID", usuarioID);

        return  query.getResultList().size();
    }

    //upsert likes
    public void setLikes(long articuloID, long usuarioID, int val){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikeArticulo l where l.articuloId =:articuloID and l.usuarioId =:usuarioID");
        query.setParameter("articuloID", articuloID);
        query.setParameter("usuarioID", usuarioID);

        List<LikeArticulo> lista = query.getResultList();
        if(lista.size() > 0){
            LikeArticulo valoracion = lista.get(0);
            valoracion.setVal(val);
            editar( valoracion  );
        }else{
            LikeArticulo valoracion = new LikeArticulo();
            valoracion.setVal(val);
            valoracion.setArticuloId(articuloID);
            valoracion.setUsuarioId(usuarioID);
            crear(valoracion);
        }
        em.close();
        return;
    }
}
