package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery(
                        //select o 는 OrderSimpleQueryDto 에 맵핑될 수 없음. 이때는 entity 나 value object 만 jpa 는 반환할 수 있음.
                        //dto 는 안되기 때문에 하려면 new Operation 을 꼭해줘야됨.
//                "select o from Order o " +
//                "join o.member " +
//                "join o.delivery d", OrderSimpleQueryDto.class)
//                .getResultList();

                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
