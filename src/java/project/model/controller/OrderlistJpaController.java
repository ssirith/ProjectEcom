/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.model.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import project.model.Account;
import project.model.Orderdetail;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import project.model.Orderlist;
import project.model.controller.exceptions.IllegalOrphanException;
import project.model.controller.exceptions.NonexistentEntityException;
import project.model.controller.exceptions.RollbackFailureException;

/**
 *
 * @author SSirith
 */
public class OrderlistJpaController implements Serializable {

    public OrderlistJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orderlist orderlist) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Account accountid = orderlist.getAccountid();
            if (accountid != null) {
                accountid = em.getReference(accountid.getClass(), accountid.getAccountid());
                orderlist.setAccountid(accountid);
            }
            Orderdetail orderdetail = orderlist.getOrderdetail();
            if (orderdetail != null) {
                orderdetail = em.getReference(orderdetail.getClass(), orderdetail.getOrdernumber());
                orderlist.setOrderdetail(orderdetail);
            }
            em.persist(orderlist);
            if (accountid != null) {
                accountid.getOrderlistList().add(orderlist);
                accountid = em.merge(accountid);
            }
            if (orderdetail != null) {
                Orderlist oldOrderlistOfOrderdetail = orderdetail.getOrderlist();
                if (oldOrderlistOfOrderdetail != null) {
                    oldOrderlistOfOrderdetail.setOrderdetail(null);
                    oldOrderlistOfOrderdetail = em.merge(oldOrderlistOfOrderdetail);
                }
                orderdetail.setOrderlist(orderlist);
                orderdetail = em.merge(orderdetail);
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

    public void edit(Orderlist orderlist) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orderlist persistentOrderlist = em.find(Orderlist.class, orderlist.getOrdernumber());
            Account accountidOld = persistentOrderlist.getAccountid();
            Account accountidNew = orderlist.getAccountid();
            Orderdetail orderdetailOld = persistentOrderlist.getOrderdetail();
            Orderdetail orderdetailNew = orderlist.getOrderdetail();
            List<String> illegalOrphanMessages = null;
            if (orderdetailOld != null && !orderdetailOld.equals(orderdetailNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Orderdetail " + orderdetailOld + " since its orderlist field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (accountidNew != null) {
                accountidNew = em.getReference(accountidNew.getClass(), accountidNew.getAccountid());
                orderlist.setAccountid(accountidNew);
            }
            if (orderdetailNew != null) {
                orderdetailNew = em.getReference(orderdetailNew.getClass(), orderdetailNew.getOrdernumber());
                orderlist.setOrderdetail(orderdetailNew);
            }
            orderlist = em.merge(orderlist);
            if (accountidOld != null && !accountidOld.equals(accountidNew)) {
                accountidOld.getOrderlistList().remove(orderlist);
                accountidOld = em.merge(accountidOld);
            }
            if (accountidNew != null && !accountidNew.equals(accountidOld)) {
                accountidNew.getOrderlistList().add(orderlist);
                accountidNew = em.merge(accountidNew);
            }
            if (orderdetailNew != null && !orderdetailNew.equals(orderdetailOld)) {
                Orderlist oldOrderlistOfOrderdetail = orderdetailNew.getOrderlist();
                if (oldOrderlistOfOrderdetail != null) {
                    oldOrderlistOfOrderdetail.setOrderdetail(null);
                    oldOrderlistOfOrderdetail = em.merge(oldOrderlistOfOrderdetail);
                }
                orderdetailNew.setOrderlist(orderlist);
                orderdetailNew = em.merge(orderdetailNew);
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
                Integer id = orderlist.getOrdernumber();
                if (findOrderlist(id) == null) {
                    throw new NonexistentEntityException("The orderlist with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orderlist orderlist;
            try {
                orderlist = em.getReference(Orderlist.class, id);
                orderlist.getOrdernumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderlist with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Orderdetail orderdetailOrphanCheck = orderlist.getOrderdetail();
            if (orderdetailOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Orderlist (" + orderlist + ") cannot be destroyed since the Orderdetail " + orderdetailOrphanCheck + " in its orderdetail field has a non-nullable orderlist field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Account accountid = orderlist.getAccountid();
            if (accountid != null) {
                accountid.getOrderlistList().remove(orderlist);
                accountid = em.merge(accountid);
            }
            em.remove(orderlist);
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

    public List<Orderlist> findOrderlistEntities() {
        return findOrderlistEntities(true, -1, -1);
    }

    public List<Orderlist> findOrderlistEntities(int maxResults, int firstResult) {
        return findOrderlistEntities(false, maxResults, firstResult);
    }

    private List<Orderlist> findOrderlistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orderlist.class));
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

    public Orderlist findOrderlist(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orderlist.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderlistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orderlist> rt = cq.from(Orderlist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
