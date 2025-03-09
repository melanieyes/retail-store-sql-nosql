package control;

import database.RedisConnection;
import model.RecentCustomer;
import redis.clients.jedis.Jedis;
import java.util.ArrayList;
import java.util.List;

public class RecentCustomerController {
    private final Jedis jedis;

    public RecentCustomerController() {
        this.jedis = RedisConnection.getJedis();
    }

    public List<RecentCustomer> getAllCustomers() {
        List<RecentCustomer> customers = new ArrayList<>();
        for (String key : jedis.keys("recent_customer:*")) {
            String firstName = jedis.hget(key, "FirstName");
            String lastName = jedis.hget(key, "LastName");
            String email = jedis.hget(key, "Email");
            String phone = jedis.hget(key, "Phone");
            String address = jedis.hget(key, "Address");
            String joinDate = jedis.hget(key, "JoinDate");

            RecentCustomer customer = new RecentCustomer(firstName, lastName, email, phone, address, joinDate);
            customers.add(customer);
        }
        return customers;
    }

    public void addCustomer(RecentCustomer customer) {
        // Create a unique key using customer name or another identifier
        String key = "recent_customer:" + customer.getFirstName().toLowerCase() + "_" + customer.getLastName().toLowerCase();

        jedis.hset(key, "FirstName", customer.getFirstName());
        jedis.hset(key, "LastName", customer.getLastName());
        jedis.hset(key, "Email", customer.getEmail());
        jedis.hset(key, "Phone", customer.getPhone());
        jedis.hset(key, "Address", customer.getAddress());
        jedis.hset(key, "JoinDate", customer.getJoinDate());
    }

    public void deleteCustomer(String key) {
        jedis.del(key);
    }
}
