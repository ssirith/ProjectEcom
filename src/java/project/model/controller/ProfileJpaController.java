/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import project.model.Account;
import project.model.Profile;
import project.model.controller.exceptions.NonexistentEntityException;
import project.model.controller.exceptions.RollbackFailureException;

/**
 *
 * @author SSirith
 */
public class ProfileJpaController implements Serializable {

    public ProfileJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profile profile) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Account accountid = profile.getAccountid();
            if (accountid != null) {
                accountid = em.getReference(accountid.getClass(), accountid.getAccountid());
                profile.setAccountid(accountid);
            }
            em.persist(profile);
            if (accountid != null) {
                accountid.getProfileList().add(profile);
                accountid = em.merge(accountid);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profile profile) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Profile persistentProfile = em.find(Profile.class, profile.getProfileid());
            Account accountidOld = persistentProfile.getAccountid();
            Account accountidNew = profile.getAccountid();
            if (accountidNew != null) {
                accountidNew = em.getReference(accountidNew.getClass(), accountidNew.getAccountid());
                profile.setAccountid(accountidNew);
            }
            profile = em.merge(profile);
            if (accountidOld != null && !accountidOld.equals(accountidNew)) {
                accountidOld.getProfileList().remove(profile);
                accountidOld = em.merge(accountidOld);
            }
            if (accountidNew != null && !accountidNew.equals(accountidOld)) {
                accountidNew.getProfileList().add(profile);
                accountidNew = em.merge(accountidNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profile.getProfileid();
                if (findProfile(id) == null) {
                    throw new NonexistentEntityException("The profile with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Profile profile;
            try {
                profile = em.getReference(Profile.class, id);
                profile.getProfileid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profile with id " + id + " no longer exists.", enfe);
            }
            Account accountid = profile.getAccountid();
            if (accountid != null) {
                accountid.getProfileList().remove(profile);
                accountid = em.merge(accountid);
            }
            em.remove(profile);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profile> findProfileEntities() {
        return findProfileEntities(true, -1, -1);
    }

    public List<Profile> findProfileEntities(int maxResults, int firstResult) {
        return findProfileEntities(false, maxResults, firstResult);
    }

    private List<Profile> findProfileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profile.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Profile findProfile(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profile.class, id);
        } finally {
            em.close();
        }
    }
    public Profile findProfileAccount(Account accountid) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Profile.findByAccountid");
        q.setParameter("accountid", accountid);
        try {
            return (Profile) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getProfileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profile> rt = cq.from(Profile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
