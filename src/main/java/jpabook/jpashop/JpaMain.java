package jpabook.jpashop;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //사실 이렇게 양방향이 아닌
            Order order = new Order();
            order.addOrderItem(new OrderItem());

            //이렇게 단방향으로 orderItem을 다시 조회해서 가져와도 된다.
            //하지만 객체지향적도 그렇고 비즈니스 상 양쪽으로 걸리는게 애플리케이션 개발하기 더 순조로울 때가 있음.
//            Order order = new Order();
//            em.persist(order);
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//
//            em.persist(orderItem);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
