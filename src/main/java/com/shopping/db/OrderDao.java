package com.shopping.db;

import com.shopping.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDao {

    private static final Logger LOG = Logger.getLogger(OrderDao.class.getName());

    public List<Order> getOrders(int userId) {
        List<Order> orderList = new ArrayList<>();

        try {
            Connection conn = H2DatabaseConnection.getConnectionToDatabase();

            PreparedStatement ps = conn.prepareStatement("select * from orders where user_id =?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setOrderDate(rs.getDate("order_date"));
                order.setNoOfItems(rs.getInt("no_of_items"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setUserId(rs.getInt("user_id"));
                orderList.add(order);
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to order details from order table for user_id :{}", userId);
        }
        return orderList;
    }
}
