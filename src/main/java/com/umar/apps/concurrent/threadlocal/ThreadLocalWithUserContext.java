package com.umar.apps.concurrent.threadlocal;

public class ThreadLocalWithUserContext implements Runnable{

    private static final ThreadLocal<Context> userContext = new ThreadLocal<>();

    private final Integer userId;

    private final UserRepository userRepository = new UserRepository();

    ThreadLocalWithUserContext(Integer userId) {
        this.userId = userId;
    }

    @Override
    public void run() {
        String username = userRepository.getUserNameForUserId(userId);
        userContext.set(new Context(username));
        System.out.printf("Thread context for the given userId: %d is %s\n", userId, userContext.get());
    }
}
