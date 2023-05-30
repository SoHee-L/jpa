package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

//    //변경 감지 기능 사용 예시
//    @Transactional
//    public void updateItem(Long itemId, Book param){
//        //id 를 기반으로 실제 DB에 있는 영속상태를 찾아옴.
//        //findItem 으로 찾아온건 영속상태.
//        Item findItem = itemRepository.findOne(itemId);
//        findItem.setPrice(param.getPrice());
//        findItem.setName(param.getName());
//        findItem.setStockQuantity(param.getStockQuantity());
//        //값을 위와 같이 세팅하면 @Transactional 에 의해 값이 커밋이 됨. 커밋이 되면 jpa 는 플러쉬라는 걸 날림.
//        //플러쉬를 날리는 건 영속성 컨텍스트에 있는 변경된 애들것 까지 다 찾음. 찾아서 값이 바뀐 것에 대한 update 쿼리를 DB 에 날림.
//        //이게 변경 감지에 의해서 데이터를 변경하는 방법.
//    }

    //병합 사용 예시
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
