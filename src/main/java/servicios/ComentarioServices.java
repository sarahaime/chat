package servicios;

import modelos.Comentario;
import modelos.Comentario;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComentarioServices extends GestionDb<Comentario>{

    public ComentarioServices(){
        super(Comentario.class);
    }

    public boolean crearComentario(String comentario, long usuarioId, long articuloId){

        Comentario coment = new Comentario();

        coment.setComentario(comentario);

        ArticuloServices as = new ArticuloServices();
        UsuarioServices us = new UsuarioServices();

        coment.setArticulo(as.find(articuloId));
        coment.setAutor(us.find(usuarioId));

        crear( coment );

        return true;

    }
    public Set<Comentario> getComentarioByArticuloID(long articuloID){
        EntityManager em = getEntityManager();
        Query queryList = em.createQuery("select c from Comentario c where c.articulo.id =:articuloid order by c.id desc");
        queryList.setParameter("articuloid", articuloID );
        List<Comentario> lista = queryList.getResultList();
        em.close();
        Set<Comentario> ans = new HashSet<>(lista);
        return ans;
    }
}
