package service.operation;

import dao.FruitDao;
import db.Storage;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReturnOperationTest {
    private static OperationHandler operationHandler;

    @BeforeAll
    static void beforeAll() {
        operationHandler = new ReturnOperation(new FruitDao() {
            @Override
            public Integer getBalance(String fruit) {
                return Storage.fruitStock.get(fruit);
            }

            @Override
            public boolean addBalance(String fruit, int quantity) {
                Storage.fruitStock.put(fruit, quantity);
                return Storage.fruitStock.containsKey(fruit)
                        && Storage.fruitStock.get(fruit) == quantity;
            }

            @Override
            public void updateBalance(String fruit, int quantity) {
                Storage.fruitStock.put(fruit, quantity);
            }

            @Override
            public Set<Map.Entry<String, Integer>> getAllEntries() {
                return Storage.fruitStock.entrySet();
            }
        });
    }

    @BeforeEach
    void setUp() {
        Storage.fruitStock.put("apple", 120);
        Storage.fruitStock.put("banana", 120);
    }

    @Test
    void handle_validReturn_isOk() {
        String fruitName = "banana";
        int quantityToReturn = 50;
        operationHandler.handle(fruitName, quantityToReturn);
        int expectedQuantity = 170;
        int actualQuantity = Storage.fruitStock.get(fruitName);
        Assertions.assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    void handle_invalidQuantityReturn_notOk() {
        String fruitName = "apple";
        int quantityToReturn = 70;
        operationHandler.handle(fruitName, quantityToReturn);
        int expectedFruitQuantity = 70;
        int actualFruitQuantity = Storage.fruitStock.get(fruitName);
        Assertions.assertNotEquals(expectedFruitQuantity, actualFruitQuantity);
    }

    @AfterEach
    void tearDown() {
        Storage.fruitStock.clear();
    }
}
