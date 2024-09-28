//package dat.dao;

import dat.daos.IDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;


// Hvordan laver man det så man tager DTO'er ind, laver det om til entites og tilbage til DTO?

/*

public abstract class AbstractDAO<T> implements IDAO<T> {
    protected EntityManagerFactory emf;
    protected Class<T> entityClass;

    public AbstractDAO(EntityManagerFactory emf, Class<T> entityClass) {
        this.emf = emf;
        this.entityClass = entityClass;
    }

    @Override
    public Set<T> getAll() {
        try(EntityManager em = emf.createEntityManager()){
            List<T> entities = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
            em.close();
            return entities;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public T getById(Long id) {
       try( EntityManager em = emf.createEntityManager()){
           T entity = em.find(entityClass, id);
           em.close();
           return entity;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;

    }

    @Override
    public T create(T t) {

        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            em.close();
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public T update(T t) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
            em.close();
            return t;
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T delete(Long id) {
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
            em.close();
            return entity;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

 */
