package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    //OrderRepository 는 orderEntity 조회 용도
    //OrderQueryRepository 는 화면이나 API 에 의존 관계가 있는 쿼리들을 구분하기 위해 패키지를 나눔
    private final EntityManager em;

    //orderItems 는 일대다여서 데이터를 바로 넣지 못함.
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders(); //query 1번 -> N개(2개)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId()); //Query N번(2번)
            o.setOrderItems(orderItems);
        });
        return result;
    }

    //findOrderQueryDtos 의 단점은 루프를 돌기 때문에 findAllByDto_optimization 은 한방에 가져옴.
    public List<OrderQueryDto> findAllByDto_optimization() {
        //루트를 다 조회.
        List<OrderQueryDto> result = findOrders();
        //주문 데이터 만큼 한방에 map 으로 메모리에 올림.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));
        //그 다음에 루프를 돌면서 모자랐던 컬렉션 데이터를 다 채워줌.
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result; //=> 이렇게 하면 쿼리 두번으로 다 가능함.
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id " +
                                "in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    //일대다이기 때문에 findOrderItems 에 대한 쿼리를 따로 짜야됨.
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }


    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address,i.name, oi.orderPrice, oi.count)from Order o" +
                                " join o.member m" +
                                " join o.delivery d " +
                                "join o.orderItems oi " +
                                "join oi.item i ", OrderFlatDto.class)
                .getResultList();
    }
}
