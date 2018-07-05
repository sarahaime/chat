package servicios;

import modelos.LikeComentario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class LikeComentarioServices extends GestionDb<LikeComentario>{

    private static LikeComentarioServices instancia;

    public LikeComentarioServices(){
        super(LikeComentario.class);
    }

    public static LikeComentarioServices getInstancia() {
        if (instancia == null) {
            instancia = new LikeComentarioServices();
        }
        return instancia;
    }


    //para like val = 1, para dislike val = 2
    public long getLikesByCoementarioID(long comentarioID, int val){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select count(*) from LikeComentario l where l.comentarioId =:comentarioID and l.val =:val");
        query.setParameter("comentarioID", comentarioID);
        query.setParameter("val", val);

        return (long) query.getSingleResult();
    }

    //para like val = 1, para dislike val = 2
    public int getLikesByComentarioYUsuarioID(long comentarioID, long usuarioID, int val){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikeComentario l where l.comentarioId =:comentarioID and l.val =:val and l.usuarioId =:usuarioID");
        query.setParameter("comentarioID", comentarioID);
        query.setParameter("val", val);
        query.setParameter("usuarioID", usuarioID);

        return query.getResultList().size();
    }

    //upsert likes
    public void setLikes(long comentarioID, long usuarioID, int val){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select l from LikeComentario l where l.comentarioId =:comentarioID and l.usuarioId =:usuarioID");
        query.setParameter("comentarioID", comentarioID);
        query.setParameter("usuarioID", usuarioID);

        List<LikeComentario> lista = query.getResultList();
        if(lista.size() > 0){
            LikeComentario valoracion = lista.get(0);
            valoracion.setVal(val);
            editar( valoracion  );
        }else{
            LikeComentario valoracion = new LikeComentario();
            valoracion.setVal(val);
            valoracion.setComentarioId(comentarioID);
            valoracion.setUsuarioId(usuarioID);
            crear(valoracion);
        }
        em.close();
        return;
    }
}
