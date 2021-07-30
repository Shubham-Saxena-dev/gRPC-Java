package com.shopping.service;

import com.google.protobuf.util.Timestamps;
import com.shopping.db.OrderDao;
import com.shopping.model.Order;
import com.shopping.stubs.order.OrderRequest;
import com.shopping.stubs.order.OrderResponse;
import com.shopping.stubs.order.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private OrderDao orderDao = new OrderDao();

    @Override
    public void getOrdersForUser(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {

        List<Order> orders = orderDao.getOrders(request.getUserId());
        List<com.shopping.stubs.order.Order> orderList = orders.stream().map(order ->
                com.shopping.stubs.order.Order.newBuilder()
                        .setOrderId(order.getOrderId())
                        .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime()))
                        .setUserId(order.getUserId())
                        .setNoOfItems(order.getNoOfItems())
                        .setTotalAmount(order.getTotalAmount()).build()).collect(Collectors.toList());

        responseObserver.onNext(OrderResponse.newBuilder().addAllOrder(orderList).build());
        responseObserver.onCompleted();
    }
}
