package org.riverdell.robotics.PedroAuto.PathsBucketFast;

import org.riverdell.robotics.PedroAuto.PathsBucketFast.initialAndRightSamples.*;
import org.riverdell.robotics.PedroAuto.PathsBucketFast.leftSample.toBasketFromLeft;
import org.riverdell.robotics.PedroAuto.PathsBucketFast.leftSample.toLeftSample;
import org.riverdell.robotics.PedroAuto.PathsBucketFast.middleSample.toBasketFromMiddle;
import org.riverdell.robotics.PedroAuto.PathsBucketFast.middleSample.toMiddleSample;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;

public class Paths {
    //test path
    public static PathChain goForward = org.riverdell.robotics.PedroAuto.PathsBucketFast.goForward.path();

    public static PathChain right_to_bucket = toBasketFromRight.path();
    public static PathChain middle_to_bucket = toBasketFromMiddle.path();
    public static PathChain bucket_to_middle = toMiddleSample.path();
    public static PathChain left_to_bucket = toBasketFromLeft.path();
    public static PathChain bucket_to_left = toLeftSample.path();
    public static PathChain slant_to_bucket = toBasketFromSlant.path();
    public static PathChain bucket_to_right = toRightSample.path();
    public static PathChain bucket_to_ascent = ascentPark.path();

}
