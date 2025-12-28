package com.location.voitures.dao;
import com.location.voitures.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {
    private final Class<T> entityClass;
    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    @Override
    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la sauvegarde de l'entité: " + entityClass.getSimpleName(), e);
        }
    }
    @Override
    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de l'entité: " + entityClass.getSimpleName(), e);
        }
    }
    @Override
    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(session.contains(entity) ? entity : session.merge(entity));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de l'entité: " + entityClass.getSimpleName(), e);
        }
    }
    @Override
    public Optional<T> findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.find(entityClass, id);
            return Optional.ofNullable(entity);
        }
    }
    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName();
            return session.createQuery(hql, entityClass).list();
        }
    }
    @Override
    public Optional<T> findOneByAttribute(String attributeName, Object value) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getName() + " WHERE " + attributeName + " = :value";
            Query<T> query = session.createQuery(hql, entityClass);
            query.setParameter("value", value);
            query.setMaxResults(1);
            return query.uniqueResultOptional();
        }
    }
    @Override
    public List<T> findAllByAttribute(String attributeName, Object value) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM " + entityClass.getSimpleName() + " WHERE " + attributeName + " = :value";
            Query<T> query = session.createQuery(hql, entityClass);
            query.setParameter("value", value);
            return query.list();
        }
    }
}
