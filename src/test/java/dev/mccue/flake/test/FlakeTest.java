package dev.mccue.flake.test;

import java.util.*;

import dev.mccue.flake.Flake;
import dev.mccue.flake.FlakeFormatException;
import net.jqwik.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// ------------------------------------------------------------------------------------------
//  Copyright © 2019-2024 Bruno Bonacci
//  source: https://github.com/BrunoBonacci/mulog
// ------------------------------------------------------------------------------------------

class FlakeTest {
    @Test
    public void flakeStringRepr() {
        assertEquals(
                Flake.of(0, 0, 0).toString(),
                "--------------------------------"
        );
        assertEquals(
                Flake.of(-1, -1, -1).toString(),
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"
        );

        var bytes = new byte[24];
        Arrays.fill(bytes, (byte) 0);
        assertEquals(
                Flake.of(0, 0, 0),
                Flake.of(bytes)
        );

        bytes = new byte[24];
        Arrays.fill(bytes, (byte) -1);
        assertEquals(
                Flake.of(-1, -1, -1),
                Flake.of(bytes)
        );

        var flake = Flake.random();
        bytes = flake.getBytes();
        assertEquals(flake, Flake.of(bytes));
    }


    @Provide
    Arbitrary<Flake> flakes() {
        return Arbitraries.bigIntegers()
                .flatMap(l1 -> Arbitraries.bigIntegers()
                        .flatMap(l2 -> Arbitraries.bigIntegers()
                                .map(l3 -> Flake.of(l1.longValue(), l2.longValue(), l3.longValue()))));
    }

    @Property
    boolean homomorphic_representation(
            @ForAll("flakes") Flake flakeA,
            @ForAll("flakes") Flake flakeB
    ) {
        // For all flakes, if f1 < f2 -> (str f1) < (str f2)
        var comparison = flakeA.compareTo(flakeB);
        if (comparison < 0) {
            return flakeA.toString().compareTo(flakeB.toString()) < 0;
        }
        else if (comparison > 0) {
            return flakeA.toString().compareTo(flakeB.toString()) > 0;
        }
        else {
            return flakeA.toString().compareTo(flakeB.toString()) == 0;
        }
    }

    @Provide
    Arbitrary<Flake> randomFlakes() {
        return Arbitraries.create(Flake::random);
    }

    @Property(shrinking = ShrinkingMode.OFF)
    boolean monotonic_property(
            @ForAll("randomFlakes") Flake flakeA,
            @ForAll("randomFlakes") Flake flakeB
    ) {
        // "For all flakes, if f1 happens before f2 -> then f1 < f2"
        return flakeA.compareTo(flakeB) < 0;
    }

    @Provide
    Arbitrary<String> randomFlakesHex() {
        return Arbitraries.create(Flake::random)
                .map(Flake::formatHex);
    }

    @Property(shrinking = ShrinkingMode.OFF)
    boolean monotonic_property_hex(
            @ForAll("randomFlakesHex") String flakeA,
            @ForAll("randomFlakesHex") String flakeB
    ) {
        // "For all flakes, if f1 happens before f2 -> then f1 < f2"
        return flakeA.compareTo(flakeB) < 0;
    }

    @Property
    boolean always_parse(
            @ForAll("flakes") Flake flake
    ) {
        return flake.equals(Flake.parse(flake.toString()));
    }

    @Test
    public void invalidParse() {
        assertThrows(FlakeFormatException.class, () -> Flake.parse("Invalid Value"));    assertThrows(FlakeFormatException.class, () -> Flake.parse("Invalid Value"));
        assertThrows(FlakeFormatException.class, () -> Flake.parse(null));
    }


}
