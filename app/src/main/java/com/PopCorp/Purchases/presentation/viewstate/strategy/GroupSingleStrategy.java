package com.PopCorp.Purchases.presentation.viewstate.strategy;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.Pair;
import com.arellomobile.mvp.viewstate.ViewCommand;
import com.arellomobile.mvp.viewstate.strategy.StateStrategy;

import java.util.Iterator;
import java.util.List;

public class GroupSingleStrategy implements StateStrategy {

    @Override
    public <View extends MvpView> void beforeApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState) {
        Iterator<Pair<ViewCommand<View>, Object>> iterator = currentState.iterator();

        while (iterator.hasNext())
        {
            Pair<ViewCommand<View>, Object> entry = iterator.next();

            if (entry.first.getTag().equals(incomingState.first.getTag()))
            {
                iterator.remove();
            }
        }

        currentState.add(incomingState);
    }

    @Override
    public <View extends MvpView> void afterApply(List<Pair<ViewCommand<View>, Object>> currentState, Pair<ViewCommand<View>, Object> incomingState) {

    }
}