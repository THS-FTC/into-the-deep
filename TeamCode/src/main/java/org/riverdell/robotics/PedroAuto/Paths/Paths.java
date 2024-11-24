package org.riverdell.robotics.PedroAuto.Paths;

import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;

public class Paths {
    public static PathChain right_to_basket = org.riverdell.robotics.PedroAuto.Paths.toBasketFromRight.path();
    public static PathChain slant_to_basket = org.riverdell.robotics.PedroAuto.Paths.toBasketFromSlant.path();
    public static PathChain basket_to_right = org.riverdell.robotics.PedroAuto.Paths.toRightSample.path();
    public static PathChain goForward = org.riverdell.robotics.PedroAuto.Paths.goForward.path();
}
