package database;

import redis.clients.jedis.Jedis;

public class RedisConnection {

    private static Jedis jedis = null;

    // Redis Cloud connection details
    private static final String HOST = "redis-18529.c15.us-east-1-4.ec2.redns.redis-cloud.com";
    private static final int PORT = 18529;
    private static final String PASSWORD = "ONsaeOqBUNOb2KKEosKMtwznsO8NeE8n";

    // Method to connect to Redis
    public static void connect() {
        try {
            if (jedis == null) {
                jedis = new Jedis(HOST, PORT);
                jedis.auth(PASSWORD); // Authenticate
                System.out.println("Connected to Redis: " + jedis.ping());
            }
        } catch (Exception e) {
            System.err.println("Error connecting to Redis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to get the Jedis client
    public static Jedis getJedis() {
        if (jedis == null) {
            connect(); // Ensure connection is established
        }
        return jedis;
    }
}
