package control;

import database.RedisConnection;
import model.BestSellingProduct;
import redis.clients.jedis.Jedis;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BestSellingProductController {
    private final Jedis jedis;
    private final Gson gson;

    public BestSellingProductController() {
        this.jedis = RedisConnection.getJedis();
        this.gson = new Gson();
    }

    public List<BestSellingProduct> getAllProducts() {
        List<BestSellingProduct> products = new ArrayList<>();
        // Iterate over all keys that match the BestSellingProducts:* pattern
        for (String key : jedis.keys("BestSellingProducts:*")) {
            String name = jedis.hget(key, "name");
            String category = jedis.hget(key, "category");
            double price = Double.parseDouble(jedis.hget(key, "price"));
            int sales = Integer.parseInt(jedis.hget(key, "sales"));
            double revenue = Double.parseDouble(jedis.hget(key, "revenue"));

            BestSellingProduct product = new BestSellingProduct(name, category, price, sales, revenue);
            products.add(product);
        }
        return products;
    }

    public void addProduct(BestSellingProduct product) {
        // Generate a unique ID for the product (e.g., based on a counter or timestamp)
        long id = jedis.incr("BestSellingProducts:ID");
        String key = "BestSellingProducts:" + id;

        jedis.hset(key, "name", product.getName());
        jedis.hset(key, "category", product.getCategory());
        jedis.hset(key, "price", String.valueOf(product.getPrice()));
        jedis.hset(key, "sales", String.valueOf(product.getSales()));
        jedis.hset(key, "revenue", String.valueOf(product.getRevenue()));
    }

    public void deleteProduct(String productName) {
        // Find the key of the product with the matching name
        for (String key : jedis.keys("BestSellingProducts:*")) {
            if (productName.equals(jedis.hget(key, "name"))) {
                jedis.del(key);
                break;
            }
        }
    }
}
