package dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import util.JpaUtil;

public class Dao<T> implements Serializable {

    private final Class<T> classe;
    private EntityManager manager;

    public Dao(Class<T> classe) {
        this.classe = classe;
    }

    public T alterar(T objeto) {
        manager = JpaUtil.getEntityManager();
        manager.getTransaction().begin();
        objeto = manager.merge(objeto);
        manager.getTransaction().commit();
        manager.close();
        return objeto;
    }

    public T buscarPorId(Object id) {
        T objeto;
        objeto = manager.find(classe, id);
        manager = JpaUtil.getEntityManager();
        manager.close();
        return objeto;
    }

    public void excluir(Integer id) {
        manager = JpaUtil.getEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        T temp = manager.find(classe, id);
        manager.remove(temp);
        tx.commit();
        manager.close();
    }

    public void inserir(T objeto) {
        manager = JpaUtil.getEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        manager.persist(objeto);
        tx.commit();
    }

    public List<T> listarTodos() {
        manager = JpaUtil.getEntityManager();
        CriteriaQuery<T> query
                = manager.getCriteriaBuilder().createQuery(classe);
        query.select(query.from(classe));
        List<T> lista = manager.createQuery(query).getResultList();
        manager.close();
        return lista;
    }

}