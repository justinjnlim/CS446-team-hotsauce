package com.hotsauce.meem.state;

public class ActiveGreetingState extends GreetingState {

    public ActiveGreetingState(GreetingContext cntx) {
        super(cntx);
    }

    public String getGreetingsString(int numberOfMeems) {
        if (numberOfMeems == 0) {
            context.transitionToIntro();
            return context.getGreetingsString(numberOfMeems);
        }

        return "Great to see you back!";
    }
}
