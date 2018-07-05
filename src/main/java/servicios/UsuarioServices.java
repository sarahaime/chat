package servicios;

import modelos.Usuario;
import org.jasypt.util.password.StrongPasswordEncryptor;

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

public class UsuarioServices extends GestionDb<Usuario>{

    public UsuarioServices(){
        super(Usuario.class);
    }


    public List<Usuario> listaUsuarios() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u");
        List<Usuario> lista = query.getResultList();
        em.close();
        return lista;
    }

    public Usuario getUsuario(long id) {
        Usuario usuario =  find(id);
        return usuario;
    }

    public Usuario getUsuarioByUserName(String username) {
        Usuario usuario = null;
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u where u.username =:username");
        query.setParameter("username", username.trim());
        List<Usuario> lista = query.getResultList();
        if(lista.size() > 0) usuario = lista.get(0);
        em.close();
        return usuario;
    }

    public boolean crearUsuario(Usuario usuario){
        //para guardar la contraseña cifrada
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encryptPassword(usuario.getPassword());
        usuario.setPassword(encryptedPassword);

        //creo al usuario
        crear( usuario );

        return true;
    }

    public boolean actualizarUsuario(Usuario usuario){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "update Usuario set nombre=?, USERNAME=?, PASSWORD=?, ADMINISTRADOR=?, AUTOR=? where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, usuario.getNombre());
            prepareStatement.setString(2, usuario.getUsername());
            prepareStatement.setString(3, usuario.getPassword());
            prepareStatement.setBoolean(4, usuario.isAdministrador());
            prepareStatement.setBoolean(5, usuario.isAutor());
            //Indica el where...
            prepareStatement.setLong(6, usuario.getId());
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public boolean borrarUsuario(int id){
        boolean ok =false;

        Connection con = null;
        try {

            String query = "delete from Usuario where id = ?";
            con = DB.getInstancia().getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);

            //Indica el where...
            prepareStatement.setInt(1, id);
            //
            int fila = prepareStatement.executeUpdate();
            ok = fila > 0 ;

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ok;
    }

    public Usuario autenticarUsuario(String userName, String pass ){
        Usuario usuario = getUsuarioByUserName(userName);

        if( usuario == null ){ return  null; }

        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        if (passwordEncryptor.checkPassword(pass, usuario.getPassword())) {
            return usuario;
        } else {
            return null;
        }
    }

}
