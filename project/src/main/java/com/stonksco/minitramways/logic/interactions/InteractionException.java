package com.stonksco.minitramways.logic.interactions;

// Used for interaction errors: not enough money, location already occupied, etc.
public class InteractionException extends Exception {
    public InteractionException(String money) {
        super(money);
    }
}
