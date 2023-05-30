package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {
//        //객체기 때문에 참조하는 스타일로 조인함.
//        return em.createQuery("select o from Order o join o.member m " +
//                "where o.status = :status " +
//                "and m.name like :name ", Order.class)
//                .setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                //만약 결과를 제한하고 싶으면 setMzxResults 사용.
//                .setMaxResults(1000) //최대 1000건
//                //페이징처리가 궁금하면 .setFirstResult(100) 사용 (start 포지션으로 100넣으면 100부터 가져온다는 뜻)
//                .getResultList();

//==========================위 와같은 것은 값이 다있다는 가정하게 예시이고 만약 동적쿼리를 사용해야 한다면?==========

//jpql 동적쿼리 사용 예시
        //1. 실무에서 사용 x
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            //값이 있으면 where 나 and 중에 하나를 붙임.
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    //2. jpa 가 동적쿼리를 작성할 수 있도록 표준으로 제공해줌. 이것도 실무에서 권장하진 않음.
    //얘를 빌드하면 결과적으로 jpql 이 만들어짐. 무슨 쿼리인지 어려워서 유지보수하기 힘들다는 큰 단점이 있음.
    //이러한 복잡한 이유들로 동적쿼리에서 QueryDSL을 사용함.
    /**
     * JPA Criteria
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order>cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>(); //동적쿼리에 대한 조합을 이걸 가지고 깔끔하게 만듬.
        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status =  cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray((new Predicate[criteria.size()]))));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

}





