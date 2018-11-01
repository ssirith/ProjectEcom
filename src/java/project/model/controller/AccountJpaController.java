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
import project.model.Profile;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import project.model.Account;
import project.model.Orderlist;
import project.model.Card;
import project.model.controller.exceptions.IllegalOrphanException;
import project.model.controller.exceptions.NonexistentEntityException;
import project.model.controller.exceptions.RollbackFailureException;

/**
 *
 * @author SSirith
 */
public class AccountJpaController implements Serializable {

    public AccountJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Account account) throws RollbackFailureException, Exception {
        if (account.getProfileList() == null) {
            account.setProfileList(new ArrayList<Profile>());
        }
        if (account.getOrderlistList() == null) {
            account.setOrderlistList(new ArrayList<Orderlist>());
        }
        if (account.getCardList() == null) {
            account.setCardList(new ArrayList<Card>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Profile> attachedProfileList = new ArrayList<Profile>();
            for (Profile profileListProfileToAttach : account.getProfileList()) {
                profileListProfileToAttach = em.getReference(profileListProfileToAttach.getClass(), profileListProfileToAttach.getProfileid());
                attachedProfileList.add(profileListProfileToAttach);
            }
            account.setProfileList(attachedProfileList);
            List<Orderlist> attachedOrderlistList = new ArrayList<Orderlist>();
            for (Orderlist orderlistListOrderlistToAttach : account.getOrderlistList()) {
                orderlistListOrderlistToAttach = em.getReference(orderlistListOrderlistToAttach.getClass(), orderlistListOrderlistToAttach.getOrdernumber());
                attachedOrderlistList.add(orderlistListOrderlistToAttach);
            }
            account.setOrderlistList(attachedOrderlistList);
            List<Card> attachedCardList = new ArrayList<Card>();
            for (Card cardListCardToAttach : account.getCardList()) {
                cardListCardToAttach = em.getReference(cardListCardToAttach.getClass(), cardListCardToAttach.getCardid());
                attachedCardList.add(cardListCardToAttach);
            }
            account.setCardList(attachedCardList);
            em.persist(account);
            for (Profile profileListProfile : account.getProfileList()) {
                Account oldAccountidOfProfileListProfile = profileListProfile.getAccountid();
                profileListProfile.setAccountid(account);
                profileListProfile = em.merge(profileListProfile);
                if (oldAccountidOfProfileListProfile != null) {
                    oldAccountidOfProfileListProfile.getProfileList().remove(profileListProfile);
                    oldAccountidOfProfileListProfile = em.merge(oldAccountidOfProfileListProfile);
                }
            }
            for (Orderlist orderlistListOrderlist : account.getOrderlistList()) {
                Account oldAccountidOfOrderlistListOrderlist = orderlistListOrderlist.getAccountid();
                orderlistListOrderlist.setAccountid(account);
                orderlistListOrderlist = em.merge(orderlistListOrderlist);
                if (oldAccountidOfOrderlistListOrderlist != null) {
                    oldAccountidOfOrderlistListOrderlist.getOrderlistList().remove(orderlistListOrderlist);
                    oldAccountidOfOrderlistListOrderlist = em.merge(oldAccountidOfOrderlistListOrderlist);
                }
            }
            for (Card cardListCard : account.getCardList()) {
                Account oldAccountidOfCardListCard = cardListCard.getAccountid();
                cardListCard.setAccountid(account);
                cardListCard = em.merge(cardListCard);
                if (oldAccountidOfCardListCard != null) {
                    oldAccountidOfCardListCard.getCardList().remove(cardListCard);
                    oldAccountidOfCardListCard = em.merge(oldAccountidOfCardListCard);
                }
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

    public void edit(Account account) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Account persistentAccount = em.find(Account.class, account.getAccountid());
            List<Profile> profileListOld = persistentAccount.getProfileList();
            List<Profile> profileListNew = account.getProfileList();
            List<Orderlist> orderlistListOld = persistentAccount.getOrderlistList();
            List<Orderlist> orderlistListNew = account.getOrderlistList();
            List<Card> cardListOld = persistentAccount.getCardList();
            List<Card> cardListNew = account.getCardList();
            List<String> illegalOrphanMessages = null;
            for (Profile profileListOldProfile : profileListOld) {
                if (!profileListNew.contains(profileListOldProfile)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Profile " + profileListOldProfile + " since its accountid field is not nullable.");
                }
            }
            for (Orderlist orderlistListOldOrderlist : orderlistListOld) {
                if (!orderlistListNew.contains(orderlistListOldOrderlist)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderlist " + orderlistListOldOrderlist + " since its accountid field is not nullable.");
                }
            }
            for (Card cardListOldCard : cardListOld) {
                if (!cardListNew.contains(cardListOldCard)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Card " + cardListOldCard + " since its accountid field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Profile> attachedProfileListNew = new ArrayList<Profile>();
            for (Profile profileListNewProfileToAttach : profileListNew) {
                profileListNewProfileToAttach = em.getReference(profileListNewProfileToAttach.getClass(), profileListNewProfileToAttach.getProfileid());
                attachedProfileListNew.add(profileListNewProfileToAttach);
            }
            profileListNew = attachedProfileListNew;
            account.setProfileList(profileListNew);
            List<Orderlist> attachedOrderlistListNew = new ArrayList<Orderlist>();
            for (Orderlist orderlistListNewOrderlistToAttach : orderlistListNew) {
                orderlistListNewOrderlistToAttach = em.getReference(orderlistListNewOrderlistToAttach.getClass(), orderlistListNewOrderlistToAttach.getOrdernumber());
                attachedOrderlistListNew.add(orderlistListNewOrderlistToAttach);
            }
            orderlistListNew = attachedOrderlistListNew;
            account.setOrderlistList(orderlistListNew);
            List<Card> attachedCardListNew = new ArrayList<Card>();
            for (Card cardListNewCardToAttach : cardListNew) {
                cardListNewCardToAttach = em.getReference(cardListNewCardToAttach.getClass(), cardListNewCardToAttach.getCardid());
                attachedCardListNew.add(cardListNewCardToAttach);
            }
            cardListNew = attachedCardListNew;
            account.setCardList(cardListNew);
            account = em.merge(account);
            for (Profile profileListNewProfile : profileListNew) {
                if (!profileListOld.contains(profileListNewProfile)) {
                    Account oldAccountidOfProfileListNewProfile = profileListNewProfile.getAccountid();
                    profileListNewProfile.setAccountid(account);
                    profileListNewProfile = em.merge(profileListNewProfile);
                    if (oldAccountidOfProfileListNewProfile != null && !oldAccountidOfProfileListNewProfile.equals(account)) {
                        oldAccountidOfProfileListNewProfile.getProfileList().remove(profileListNewProfile);
                        oldAccountidOfProfileListNewProfile = em.merge(oldAccountidOfProfileListNewProfile);
                    }
                }
            }
            for (Orderlist orderlistListNewOrderlist : orderlistListNew) {
                if (!orderlistListOld.contains(orderlistListNewOrderlist)) {
                    Account oldAccountidOfOrderlistListNewOrderlist = orderlistListNewOrderlist.getAccountid();
                    orderlistListNewOrderlist.setAccountid(account);
                    orderlistListNewOrderlist = em.merge(orderlistListNewOrderlist);
                    if (oldAccountidOfOrderlistListNewOrderlist != null && !oldAccountidOfOrderlistListNewOrderlist.equals(account)) {
                        oldAccountidOfOrderlistListNewOrderlist.getOrderlistList().remove(orderlistListNewOrderlist);
                        oldAccountidOfOrderlistListNewOrderlist = em.merge(oldAccountidOfOrderlistListNewOrderlist);
                    }
                }
            }
            for (Card cardListNewCard : cardListNew) {
                if (!cardListOld.contains(cardListNewCard)) {
                    Account oldAccountidOfCardListNewCard = cardListNewCard.getAccountid();
                    cardListNewCard.setAccountid(account);
                    cardListNewCard = em.merge(cardListNewCard);
                    if (oldAccountidOfCardListNewCard != null && !oldAccountidOfCardListNewCard.equals(account)) {
                        oldAccountidOfCardListNewCard.getCardList().remove(cardListNewCard);
                        oldAccountidOfCardListNewCard = em.merge(oldAccountidOfCardListNewCard);
                    }
                }
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
                Integer id = account.getAccountid();
                if (findAccount(id) == null) {
                    throw new NonexistentEntityException("The account with id " + id + " no longer exists.");
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
            Account account;
            try {
                account = em.getReference(Account.class, id);
                account.getAccountid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The account with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Profile> profileListOrphanCheck = account.getProfileList();
            for (Profile profileListOrphanCheckProfile : profileListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Account (" + account + ") cannot be destroyed since the Profile " + profileListOrphanCheckProfile + " in its profileList field has a non-nullable accountid field.");
            }
            List<Orderlist> orderlistListOrphanCheck = account.getOrderlistList();
            for (Orderlist orderlistListOrphanCheckOrderlist : orderlistListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Account (" + account + ") cannot be destroyed since the Orderlist " + orderlistListOrphanCheckOrderlist + " in its orderlistList field has a non-nullable accountid field.");
            }
            List<Card> cardListOrphanCheck = account.getCardList();
            for (Card cardListOrphanCheckCard : cardListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Account (" + account + ") cannot be destroyed since the Card " + cardListOrphanCheckCard + " in its cardList field has a non-nullable accountid field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(account);
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

    public List<Account> findAccountEntities() {
        return findAccountEntities(true, -1, -1);
    }

    public List<Account> findAccountEntities(int maxResults, int firstResult) {
        return findAccountEntities(false, maxResults, firstResult);
    }

    private List<Account> findAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Account.class));
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

    public Account findAccount(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }
    public Account findAccountEmail(String email) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Account.findByEmail");
        q.setParameter("email",email);
        try {
            return (Account) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Account> rt = cq.from(Account.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
