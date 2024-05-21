package common.build.response;

import common.util.Commands;

public class SumOfImpactSpeedRes extends Response {
    public final long sum;

    public SumOfImpactSpeedRes(long sum, String error) {
        super(Commands.SUM_OF_IMPACT_SPEED, error);
        this.sum = sum;
    }
}