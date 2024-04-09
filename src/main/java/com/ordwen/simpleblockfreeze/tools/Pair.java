package com.ordwen.simpleblockfreeze.tools;

public record Pair<F, S>(F first, S second) {

    /**
     * Convert the pair to a string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}