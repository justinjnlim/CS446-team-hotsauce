package com.hotsauce.meem.state;

public abstract class GreetingState {

    public GreetingContext context;

    public GreetingState(GreetingContext cntx) {
        context = cntx;
    }

    abstract String getGreetingsString(int numberOfMeems);
}
