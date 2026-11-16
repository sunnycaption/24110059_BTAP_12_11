package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.VideoDAO;
import vn.iotstar.entity.Video;

import java.util.List;

/**
 * Triển khai VideoDAO sử dụng Jakarta JPA EntityManager.
 */
public class VideoDAOImpl implements VideoDAO {

    @Override
    public void insert(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(video);
            trans.commit();
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void update(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(video);
            trans.commit();
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Video video = em.find(Video.class, videoId);
            if (video != null) {
                em.remove(video);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Video findById(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Video.class, videoId);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createNamedQuery("Video.findAll", Video.class);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Video> findTop10Latest() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createNamedQuery("Video.findTop10Latest", Video.class);
            query.setMaxResults(10);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Video> findByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createNamedQuery("Video.findByUserId", Video.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Video> findByUserIdWithPaging(int userId, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createNamedQuery("Video.findByUserId", Video.class);
            query.setParameter("userId", userId);
            int firstResult = Math.max(0, (page - 1) * pageSize);
            query.setFirstResult(firstResult);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public int countByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createNamedQuery("Video.countByUserId", Long.class);
            query.setParameter("userId", userId);
            Long count = query.getSingleResult();
            return count != null ? count.intValue() : 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void increaseViews(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Video video = em.find(Video.class, videoId);
            if (video != null) {
                video.setViews(video.getViews() + 1);
                em.merge(video);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans != null && trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
