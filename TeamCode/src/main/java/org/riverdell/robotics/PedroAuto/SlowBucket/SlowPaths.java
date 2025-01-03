package org.riverdell.robotics.PedroAuto.SlowBucket;

import org.riverdell.robotics.PedroAuto.SlowBucket.initialSample.toBasketFromPre;
import org.riverdell.robotics.PedroAuto.SlowBucket.initialSample.toPreBasketFromSlant;
import org.riverdell.robotics.PedroAuto.SlowBucket.leftSample.toBasketFromLeft;
import org.riverdell.robotics.PedroAuto.SlowBucket.leftSample.toLeftSample;
import org.riverdell.robotics.PedroAuto.SlowBucket.leftSample.toPreBasketFromLeft;
import org.riverdell.robotics.PedroAuto.SlowBucket.middleSample.toBasketFromMiddle;
import org.riverdell.robotics.PedroAuto.SlowBucket.middleSample.toMiddleSample;
import org.riverdell.robotics.PedroAuto.SlowBucket.middleSample.toPreBasketFromMiddle;
import org.riverdell.robotics.PedroAuto.SlowBucket.rightSample.toBasketFromRight;
import org.riverdell.robotics.PedroAuto.SlowBucket.rightSample.toPreBasketFromRight;
import org.riverdell.robotics.PedroAuto.SlowBucket.rightSample.toRightSample;
import org.riverdell.robotics.pedroPathing.pathGeneration.PathChain;

public class SlowPaths {
    //test path
    public static PathChain goForward = org.riverdell.robotics.PedroAuto.SlowBucket.goForward.path();

    public static PathChain slant_to_pre = toPreBasketFromSlant.path();

    public static PathChain right_to_pre = toPreBasketFromRight.path();
    public static PathChain middle_to_pre = toPreBasketFromMiddle.path();
    public static PathChain left_to_pre = toPreBasketFromLeft.path();

    public static PathChain rightPre_to_bucket = toBasketFromRight.path();
    public static PathChain leftPre_to_bucket = toBasketFromLeft.path();
    public static PathChain middlePre_to_bucket = toBasketFromMiddle.path();
    public static PathChain initialPre_to_bucket = toBasketFromPre.path();


    public static PathChain bucket_to_preascent = preAscentPark.path();
    public static PathChain pre_to_park = ascentPark.path();

    public static PathChain bucket_to_right = toRightSample.path();
    public static PathChain bucket_to_left = toLeftSample.path();
    public static PathChain bucket_to_middle = toMiddleSample.path();

}
