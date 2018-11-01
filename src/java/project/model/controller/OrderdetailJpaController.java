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
import project.model.Orderlist;
import project.model.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import project.model.Orderdetail;
import project.model.controller.exceptions.IllegalOrphanException;
import project.model.controller.exceptions.NonexistentEntityException;
import project.model.controller.exceptions.PreexistingEntityException;
import project.model.controller.exceptions.RollbackFailureException;

/**
 *
 * @author SSirith
 */
public class OrderdetailJpaController implements Serializable {

    public OrderdetailJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orderdetail orderdetail) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        List<String> illegalOrphanMessages = null;
        Orderlist orderlistOrphanCheck = orderdetail.getOrderlist();
        if (orderlistOrphanCheck != null) {
            Orderdetail oldOrderdetailOfOrderlist = orderlistOrphanCheck.getOrderdetail();
            if (oldOrderdetailOfOrderlist != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Orderlist " + orderlistOrphanCheck + " already has an item of type Orderdetail whose orderlist column cannot be null. Please make another selection for the orderlist field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orderlist orderlist = orderdetail.getOrderlist();
            if (orderlist != null) {
                orderlist = em.getReference(orderlist.getClass(), orderlist.getOrdernumber());
                orderdetail.setOrderlist(orderlist);
            }
            Product productcode = orderdetail.getProductcode();
            if (productcode != null) {
                productcode = em.getReference(productcode.getClass(), productcode.getProductcode());
                orderdetail.setProductcode(productcode);
            }
            em.persist(orderdetail);
            if (orderlist != null) {
                orderlist.setOrderdetail(orderdetail);
                orderlist = em.merge(orderlist);
            }
            if (productcode != null) {
                productcode.getOrderdetailList().add(orderdetail);
                productcode = em.merge(productcode);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findOrderdetail(orderdetail.getOrdernumber()) != null) {
                throw new PreexistingEntityException("Orderdetail " + orderdetail + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orderdetail orderdetail) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Orderdetail persistentOrderdetail = em.find(Orderdetail.class, orderdetail.getOrdernumber());
            Orderlist orderlistOld = persistentOrderdetail.getOrderlist();
            Orderlist orderlistNew = orderdetail.getOrderlist();
            Product productcodeOld = persistentOrderdetail.getProductcode();
            Product productcodeNew = orderdetail.getProductcode();
            List<String> illegalOrphanMessages = null;
            if (orderlistNew != null && !orderlistNew.equals(orderlistOld)) {
                Orderdetail oldOrderdetailOfOrderlist = orderlistNew.getOrderdetail();
                if (oldOrderdetailOfOrderlist != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Orderlist " + orderlistNew + " already has an item of type Orderdetail whose orderlist column cannot be null. Please make another selection for the orderlist field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (orderlistNew != null) {
                orderlistNew = em.getReference(orderlistNew.getClass(), orderlistNew.getOrdernumber());
                orderdetail.setOrderlist(orderlistNew);
            }
            if (productcodeNew != null) {
                productcodeNew = em.getReference(productcodeNew.getClass(), productcodeNew.getProductcode());
                orderdetail.setProductcode(productcodeNew);
            }
            orderdetail = em.merge(orderdetail);
            if (orderlistOld != null && !orderlistOld.equals(orderlistNew)) {
                orderlistOld.setOrderdetail(null);
                orderlistOld = em.merge(orderlistOld);
            }
            if (orderlistNew != null && !orderlistNew.equals(orderlistOld)) {
                orderlistNew.setOrderdetail(orderdetail);
                orderlistNew = em.merge(orderlistNew);
            }
            if (productcodeOld != null && !productcodeOld.equals(productcodeNew)) {
                productcodeOld.getOrderdetailList().remove(orderdetail);
                productcodeOld = em.merge(productcodeOld);
            }
            if (productcodeNew != null && !productcodeNew.equals(productcodeOld)) {
                productcodeNew.getOrderdetailList().add(orderdetail);
                productcodeNew = em.merge(productcodeNew);
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
                Integer id = orderdetail.getOrdernumber();
                if (findOrderdetail(id) == null) {
                    throw new NonexistentEntityException("The orderdetail with id " + id + " no longer exists.");
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
            Orderdetail orderdetail;
            try {
                orderdetail = em.getReference(Orderdetail.class, id);
                orderdetail.getOrdernumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderdetail with id " + id + " no longer exists.", enfe);
            }
            Orderlist orderlist = orderdetail.getOrderlist();
            if (orderlist != null) {
                orderlist.setOrderdetail(null);
                orderlist = em.merge(orderlist);
            }
            Product productcode = orderdetail.getProductcode();
            if (productcode != null) {
                productcode.getOrderdetailList().remove(orderdetail);
                productcode = em.merge(productcode);
            }
            em.remove(orderdetail);
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

    public List<Orderdetail> findOrderdetailEntities() {
        return findOrderdetailEntities(true, -1, -1);
    }

    public List<Orderdetail> findOrderdetailEntities(int maxResults, int firstResult) {
        return findOrderdetailEntities(false, maxResults, firstResult);
    }

    private List<Orderdetail> findOrderdetailEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orderdetail.class));
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

    public Orderdetail findOrderdetail(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orderdetail.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderdetailCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orderdetail> rt = cq.from(Orderdetail.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
