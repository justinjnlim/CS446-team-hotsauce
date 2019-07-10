package com.hotsauce.meem.state;

public class IntroGreetingState extends GreetingState {

    public IntroGreetingState(GreetingContext cntx) {
        super(cntx);
    }

    public String getGreetingsString(int numberOfMeems) {
        if (numberOfMeems > 0) {
            context.transitionToActive();
            return context.getGreetingsString(numberOfMeems);
        }

        return "Welcome to Meem, create a new meme today!";
    }
}
