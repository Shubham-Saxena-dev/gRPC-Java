package com.shopping.server;

import com.shopping.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserServer {

    private static final Logger LOG = Logger.getLogger(UserServer.class.getName());

    private Server server;

    public static void main(String[] args) {
        UserServer userServer = new UserServer();
        userServer.startServer();
        userServer.blockUntilShutdown();
    }

    public void startServer() {
        try {
            server = ServerBuilder.forPort(50051).addService(new UserServiceImpl()).build().start();
            Runtime.getRuntime().addShutdownHook(new Thread(() ->{
             LOG.info("Cleaning....");
             UserServer.this.stopServer();

            }));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to start user server");
        }

        LOG.info("Server started on port 50051");
    }

    public void stopServer() {
        if (Objects.nonNull(server)) {
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unable to stop user server");
            }
        }
    }

    public void blockUntilShutdown() {
        if (Objects.nonNull(server)) {
            try {
                server.awaitTermination();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Unable to stop user server");
            }
        }
    }
}
