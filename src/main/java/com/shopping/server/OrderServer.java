package com.shopping.server;

import com.shopping.service.OrderServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {

    private static final Logger LOG = Logger.getLogger(OrderServer.class.getName());

    private Server server;

    public static void main(String[] args) {
        OrderServer orderServer = new OrderServer();
        orderServer.startServer();
        orderServer.blockUntilShutdown();
    }

    public void startServer() {
        try {
            server = ServerBuilder.forPort(50052).addService(new OrderServiceImpl()).build().start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOG.info("Cleaning....");
                OrderServer.this.stopServer();

            }));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to start order server");
        }

        LOG.info("Server started on port 50052");
    }

    public void stopServer() {
        if (Objects.nonNull(server)) {
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unable to stop order server");
            }
        }
    }

    public void blockUntilShutdown() {
        if (Objects.nonNull(server)) {
            try {
                server.awaitTermination();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unable to stop order server");
            }
        }
    }
}
