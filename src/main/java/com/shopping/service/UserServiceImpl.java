package com.shopping.service;

import com.shopping.client.OrderClient;
import com.shopping.db.UserDao;
import com.shopping.model.User;
import com.shopping.stubs.order.Order;
import com.shopping.stubs.user.Gender;
import com.shopping.stubs.user.UserRequest;
import com.shopping.stubs.user.UserResponse;
import com.shopping.stubs.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());
    private UserDao dao = new UserDao();

    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = dao.getDetails(request.getUsername());

        UserResponse.Builder response = UserResponse.newBuilder()
                .setId(user.getId())
                .setAge(user.getAge())
                .setGender(Gender.valueOf(user.getGender()))
                .setName(user.getName())
                .setUsername(user.getUsername());

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:50052").usePlaintext().build();

        List<Order> orders = new OrderClient(channel).getOrders(response.getId());

        try {
            channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, "Channel shutdown failed.", e);
        }
        response.setNoOfOrders(orders.size());
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
